package com.drive_cv.backend.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;

import javax.imageio.spi.IIORegistry;

public class GoogleDriveService {
    // Nombre de la aplicación
    private static final String APPLICATION_NAME = "DriveCvApp"; // Asegúrate de que no esté vacío ni nulo
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    // Métodos para cargar las credenciales y crear un cliente de Google Drive

    public Drive getDriveService(Credential credential) throws Exception {
        // Verifica el valor de APPLICATION_NAME
        System.out.println("APPLICATION_NAME: " + APPLICATION_NAME);
        // Asegúrate de que el APPLICATION_NAME esté configurado correctamente
        if (APPLICATION_NAME == null || APPLICATION_NAME.isEmpty()) {
            throw new IllegalStateException("El nombre de la aplicación no está configurado.");
        }

        // Crear e inicializar el servicio de Google Drive
        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential) // Asegúrate de que las credenciales estén bien configuradas
                .setApplicationName(APPLICATION_NAME) // Se establece el nombre de la aplicación aquí
                .build();
    }
}
