package com.nqma.disbot.service;

import com.nqma.responseservice.controller.RequestController;
import com.nqma.responseservice.controller.requests.Request;
import com.nqma.responseservice.controller.requests.SongPutRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class ExternalService {

    private final RequestController<Request> requestController = new RequestController<>();

    @Value("${audio-service-url}")
    private String audioServiceUrl;

    public void addSong(SongPutRequest songPutRequest) {
        System.out.println("Sending song add request");
        System.out.println(audioServiceUrl);
        URI uri = null;
        try {
            uri = new URI("http://localhost:8082/" + "song/add");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        if (!uri.isAbsolute()) {
            throw new IllegalArgumentException("URI is not absolute aboba");
        }
        requestController.sendJSONRequest("http://localhost:8082/song/add", songPutRequest);
    }

    public void getNextSong() {
        requestController.sendRequest(audioServiceUrl + "song/getnext");
    }

    public void test() {
        requestController.sendRequest(audioServiceUrl + "test");
    }

}
