package org.example.utils;

import lombok.experimental.UtilityClass;

import java.nio.file.Path;
import java.nio.file.Paths;

@UtilityClass
public class TomcatUtils {

   public Path UPLOAD_PATH = getUploadPath();

    private Path getUploadPath() {
        String tomcatDir = System.getProperty("catalina.base");
        return Paths.get(tomcatDir, "uploads", "images");
    }
}
