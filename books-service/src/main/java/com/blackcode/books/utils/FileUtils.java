package com.blackcode.books.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class FileUtils {

    public static String saveBase64ImageToFile(String base64Image) throws IOException {
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }

        String cleanedBase64 = base64Image
                .replaceAll("^data:image/[^;]+;base64,", "")
                .replaceAll("\\s", "");

        byte[] imageBytes = Base64.getDecoder().decode(cleanedBase64);

        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        
        String fileName = "book_" + System.currentTimeMillis() + ".png";
        Path filePath = uploadDir.resolve(fileName);

        Files.write(filePath, imageBytes);

        return filePath.toString();
    }

}
