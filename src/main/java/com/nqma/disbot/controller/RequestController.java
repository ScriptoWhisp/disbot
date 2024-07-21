package com.nqma.disbot.controller;

import com.nqma.disbot.controller.requests.Request;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RequestController<T extends Request> {


    private final RestTemplate restTemplate = new RestTemplate();

    public void sendRequest(String url) {
        restTemplate.getForObject(url, String.class);
    }

    @Async
    public void sendJSONRequest(String url, T data) {
        String respond = restTemplate.postForObject(url, data, String.class);
        System.out.println(respond);
    }
}
