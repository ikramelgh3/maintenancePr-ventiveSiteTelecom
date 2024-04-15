package net.elghz.siteservice.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {
    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ex) {
            throw new IOException("Le fichier ne peut pas etre enregistrer");
        }
    }

    public static void cleanDir(String dir){
        Path dirPath = Paths.get(dir);
        try{
            Files.list(dirPath).forEach(file->{
                if(!Files.isDirectory(file)){
                    try{
                        Files.delete(file);
                    }
                    catch(IOException ex){
                        System.out.println("Le dosssier ne peut pas étre supprime");
                    }
                }
            });
            if (Files.list(dirPath).count() == 0) {
                try {
                    Files.delete(dirPath);
                    System.out.println("Le dossier a été supprimé: " + dir);
                } catch (IOException ex) {
                    System.out.println("Le dossier ne peut pas être supprimé: " + dir);
                }
            }

        }
        catch (IOException ex){
            System.out.println("Pas d'accés au dossier: "+dirPath);
        }
    }
}
