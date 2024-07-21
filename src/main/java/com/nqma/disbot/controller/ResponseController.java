package com.nqma.disbot.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ResponseController {

    @GetMapping("/test")
    public Status test() {
        System.out.println("recieved");
        return Status.OK;
    }


}
