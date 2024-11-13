package com.drive_cv.backend.controller;

import com.drive_cv.backend.service.DriveService;
import com.drive_cv.backend.domain.Res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api")
public class ControllerDrive {
    @Autowired
    private DriveService driveService;

    /**
     * Endpoint para subir archivos a Google Drive.
     *
     * @param file El archivo a subir.
     * @return Una respuesta con el resultado de la operación.
     */
    @PostMapping("/uploadToGoogleDrive")
    public ResponseEntity<Res> subirArchivo(@RequestParam("file") MultipartFile file) throws IOException, GeneralSecurityException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(new Res(400, "El archivo está vacío", null));
        }

        Res respuesta = driveService.subirArchivoADrive(file);
        return ResponseEntity.status(respuesta.getStatus()).body(respuesta);
    }

}
