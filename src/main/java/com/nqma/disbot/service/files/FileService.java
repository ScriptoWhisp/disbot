package com.nqma.disbot.service.files;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Service
public abstract class FileService {

    public static List<String> listFiles(String directoryPath) throws IOException {
        List<String> fileList = new ArrayList<>();
        System.out.println(directoryPath);
        Path path = Paths.get(directoryPath);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path file : stream) {
                fileList.add(directoryPath + "/" + file.getFileName().toString());
            }
        } catch (IOException | DirectoryIteratorException ex) {
            throw new IOException("Error reading directory: " + directoryPath, ex);
        }

        return fileList;
    }
}
