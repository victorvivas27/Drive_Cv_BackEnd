package com.drive_cv.backend.secret;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component

public class Secret {
    private static String ID_CARPETA_DESTINO;

    public Secret(@Value("${drive.cv.carpeta.destino}") String carpetaDestino) {
        ID_CARPETA_DESTINO = carpetaDestino;
    }

    public static String getIdCarpetaDestino() {
        return ID_CARPETA_DESTINO;
    }
}
