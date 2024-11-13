package com.drive_cv.backend.service;

import com.drive_cv.backend.domain.Res;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class DriveService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriveService.class);
    private static final GsonFactory FABRICA_JSON = GsonFactory.getDefaultInstance();
    private static final String RUTA_CREDENCIALES_CUENTA_SERVICIO = obtenerRutaCredenciales();
    private static final String ID_CARPETA_DESTINO = "1ZgW8Zgtw1iSwCnVfgW-9Axax8yY9L1gl"; // ID de la carpeta de destino en Drive

    /**
     * Obtiene la ruta absoluta al archivo de credenciales de la cuenta de servicio.
     *
     * @return La ruta completa del archivo de credenciales.
     */
    private static String obtenerRutaCredenciales() {
        String directorioActual = System.getProperty("user.dir");
        Path rutaArchivo = Paths.get(directorioActual, "credentials.json");
        return rutaArchivo.toString();
    }

    /**
     * Sube un archivo al Google Drive en la carpeta especificada.
     *
     * @param file El archivo a subir.
     * @return Una instancia de Res que contiene el estado de la operación, el mensaje y la URL del archivo en Drive.
     * @throws IOException              Si ocurre un error de entrada/salida.
     * @throws GeneralSecurityException Si ocurre un error de seguridad.
     */
    public Res subirArchivoADrive(MultipartFile file) throws IOException, GeneralSecurityException {
        Res respuesta = new Res();

        // Guardamos el archivo temporalmente en el sistema de archivos
        File archivoTemporal = File.createTempFile("temp", null);
        file.transferTo(archivoTemporal);

        try {
            Drive drive = crearServicioDrive();

            // Crear metadata del archivo
            com.google.api.services.drive.model.File metadatosArchivo = new com.google.api.services.drive.model.File();
            metadatosArchivo.setName(file.getOriginalFilename());
            metadatosArchivo.setParents(Collections.singletonList(ID_CARPETA_DESTINO));

            // Determinar el tipo MIME del archivo
            String tipoMime = obtenerTipoMime(archivoTemporal);
            FileContent mediaContent = new FileContent(tipoMime, archivoTemporal);

            // Subir el archivo a Google Drive
            com.google.api.services.drive.model.File archivoSubido = drive.files().create(metadatosArchivo, mediaContent)
                    .setFields("id")
                    .execute();

            // Obtener la URL pública del archivo subido
            String urlArchivo = "https://drive.google.com/uc?export=view&id=" + archivoSubido.getId();
            LOGGER.info("Archivo subido: " + archivoSubido.getId());

            // Eliminar el archivo temporal
            archivoTemporal.delete();

            // Respuesta de éxito
            respuesta.setStatus(200);
            respuesta.setMessage("Archivo subido exitosamente");
            respuesta.setUrl(urlArchivo);

        } catch (Exception e) {
            LOGGER.error("Error al subir el archivo: ", e);
            respuesta.setStatus(500);
            respuesta.setMessage("Error al subir el archivo: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * Obtiene el tipo MIME del archivo proporcionado.
     *
     * @param file El archivo del cual se quiere obtener el tipo MIME.
     * @return El tipo MIME del archivo.
     */
    private String obtenerTipoMime(File file) {
        String nombreArchivo = file.getName();
        if (nombreArchivo.endsWith(".pdf")) {
            return "application/pdf";
        } else if (nombreArchivo.endsWith(".docx") || nombreArchivo.endsWith(".doc")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else if (nombreArchivo.endsWith(".txt")) {
            return "text/plain";
        } else if (nombreArchivo.endsWith(".jpg") || nombreArchivo.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (nombreArchivo.endsWith(".png")) {
            return "image/png";
        }
        return "application/octet-stream"; // Por defecto, si no se encuentra el tipo MIME
    }

    /**
     * Crea el servicio de Google Drive utilizando las credenciales de la cuenta de servicio.
     *
     * @return Una instancia del servicio Drive.
     * @throws IOException              Si ocurre un error de entrada/salida.
     * @throws GeneralSecurityException Si ocurre un error de seguridad.
     */
    private Drive crearServicioDrive() throws IOException, GeneralSecurityException {
        GoogleCredential credencial = GoogleCredential.fromStream(new FileInputStream(RUTA_CREDENCIALES_CUENTA_SERVICIO))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));
        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                FABRICA_JSON,
                credencial)
                .build();
    }
}
