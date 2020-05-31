package com.vn.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/* Fichero de configuración de la aplicación, equivalente a web.xml */
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	/* Carga cualquier otro tipo de configuración, por ejemplo, la de base de datos */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {
	            PersistenceJPAConfig.class
	        };
	}

	/* Configuración principal de acceso WEB, cualquier petición a / será gestionada por los beans configurados en WebMvcConfig */
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
