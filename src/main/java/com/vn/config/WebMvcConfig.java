package com.vn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration // Fichero de configuraci�n
@EnableWebMvc // Habilita el uso de Spring MVC
@ComponentScan(basePackages = {"com.vn.controller"}) // Define la ubicac�on de los controladores que resolveran las peticiones */
public class WebMvcConfig implements WebMvcConfigurer {

	/* Resolvedor de vistas, dado el nombre l�gico a�adir� el prefijo y sufijo para resolver la ubicaci�n de los JSP (InternalResourceViewResolver) */
	@Bean
    public InternalResourceViewResolver resolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

	/* Define la ubicaci�n del resto de ficheros est�ticos (css y js) */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/resources/**")
            .addResourceLocations("/resources/");
    }

}
