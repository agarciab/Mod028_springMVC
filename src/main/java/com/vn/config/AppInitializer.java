package com.vn.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/* Fichero de configuraci�n de la aplicaci�n, equivalente a web.xml */
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	/* Carga cualquier otro tipo de configuraci�n, por ejemplo, la de base de datos */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {
	            PersistenceJPAConfig.class
	        };
	}

	/* Configuraci�n principal de acceso WEB, cualquier petici�n a / ser� gestionada por los beans configurados en WebMvcConfig */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] {
			WebMvcConfig.class
		};
	}

	@Override
	protected String[] getServletMappings() {
		return new String [] {
				"/"
		};
	}

}
