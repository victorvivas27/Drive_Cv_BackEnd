package com.drive_cv.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DriveCvApplication {
	static final Logger LOGGER = LoggerFactory.getLogger(DriveCvApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(DriveCvApplication.class, args);
		LOGGER.info("¡Aplicación de carga a Google Drive iniciada correctamente! Accesible en: http://localhost:5050");
	}

}
