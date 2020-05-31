package com.vn.config;

import java.sql.SQLException;
import java.util.Properties;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration // Fichero de configuraci�n
@EnableTransactionManagement // Habilitamos la transaccionalidad
@PropertySource({"classpath:database.properties"}) // Cargamos las propiedades del fichero database.properties
@ComponentScan({"com.vn"}) // Le indicamos a Spring que los beans se encuentran en el paquete com.vn y subpaquetes
@EnableJpaRepositories(basePackages = "com.vn.repository") // Habilitamos Spring Data JPA e indicamos d�nde se encuentran los @Repository
public class PersistenceJPAConfig {

	/* Bean que representa el contexto de Spring */
	@Autowired
	private Environment env;

	/* Adicionalmente podemos configurar una consola para poder ver los registros y tablas en BD */
	private Server H2Console;

	/* Forma equivalente de cargar propiedades*/
	@Value("${jdbc.driverClassName}")
	private String driverCalss;

	/* Para bases de datos embedidas como H2, usaremos un EmbeddedDatabaseBuilder*/
	@Bean
	public DataSource dataSource() {
		DataSource ds = new EmbeddedDatabaseBuilder()
				.generateUniqueName(false)
				//.setName("DATA_FRM;MODE=Oracle;INIT=CREATE SCHEMA IF NOT EXISTS DATA_FRM\\; SET SCHEMA DATA_FRM")
				.setType(EmbeddedDatabaseType.H2)
				//.setScriptEncoding("UTF-8")
				//.addScript("db/H2/Compatibility.sql")
				//.ignoreFailedDrops(true)
				.build();
		return ds;
	}
	
	/* Para bases de datos externas, Postgres, Oracle, DB2, etc usaremos DriverManagerDataSource */
	/*@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverCalss);
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.user"));
		dataSource.setPassword(env.getProperty("jdbc.pass"));
		return dataSource;
	}*/

	/* Configuramos el gestor de transacciones */
	@Bean
	public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}

	/* Configuramos el Entity Manager, componente principal del JPA (equivalente a la SessionFactory en Hibernate) */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource());
		entityManagerFactoryBean.setPackagesToScan(new String[] {"com.vn.entity" }); // Hay que indicar d�nde se ubican las @Entity

		final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
		/* 
		 * Configuraci�n para la creaci�n autom�tica dle schema en base de datos
		 * y tambien otro tipo de configuraci�n fina de Hibernate 
		 */
		entityManagerFactoryBean.setJpaProperties(additionalProperties()); 

		return entityManagerFactoryBean;
	}

	/* Configuraci�n fina de Hibernate */
	final Properties additionalProperties() {
		final Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto")); //Opci�n create-drop para generar autom�ticamente el schema
		hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", env.getProperty("hibernate.cache.use_second_level_cache"));
		hibernateProperties.setProperty("hibernate.cache.use_query_cache", env.getProperty("hibernate.cache.use_query_cache"));

		return hibernateProperties;
	}

	/* Configuraci�n necesaria para la consola de BD. El puerto debe ser diferente al de la aplicaci�n 
	 * Accediendo a localhost:8084 tenemos acceso a la consola de BD y podemos ver tablas, hacer consultas, inserciones y dem�s
	 */
	@Bean
	public Server h2WebServer() throws SQLException {
		H2Console = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8084").start();
		return H2Console;
	}

	// Para Java 11 hay que a�adir la dependencia en el POM javax.annotation-api (y poder usar @PreDestroy) ya que no se distribuye conjuntamente */
	@PreDestroy
	public void onDestroy() throws Exception {
		if (H2Console != null && H2Console.isRunning(true)) {
			H2Console.stop();
		}
	}

}
