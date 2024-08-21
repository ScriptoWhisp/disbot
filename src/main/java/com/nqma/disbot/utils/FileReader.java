package com.nqma.disbot.utils;

import discord4j.common.JacksonResources;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileReader {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ResourcePatternResolver resourcePatternResolver;

    private static final JacksonResources MAPPER = JacksonResources.create();

    public List<ApplicationCommandRequest> listFilesOfAppComReq(String directory) throws IOException {
//        List<String> fileList = new ArrayList<>();
//        System.out.println(directoryPath);
//        Path path = Paths.get(directoryPath);
//
//        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
//            for (Path file : stream) {
//                fileList.add(directoryPath + "/" + file.getFileName().toString());
//            }
//        } catch (IOException | DirectoryIteratorException ex) {
//            System.out.println("Error reading files from directory: " + directoryPath);
//            System.out.println("Starting in server mode...");
//        }
//
//        return fileList;

        List<ApplicationCommandRequest> jsonContents = new ArrayList<>();

        // Look for all JSON files in the given directory inside the JAR
        String pattern = "classpath*:" + directory + "/*.json";
        Resource[] resources = resourcePatternResolver.getResources(pattern);

        for (Resource resource : resources) {
            try (InputStream is = resource.getInputStream()) {
                ApplicationCommandRequest commandRequest = MAPPER.getObjectMapper().readValue(is, ApplicationCommandRequest.class);
                jsonContents.add(commandRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Found " + jsonContents.size() + " JSON files in " + directory);

        return jsonContents;
    }
}
