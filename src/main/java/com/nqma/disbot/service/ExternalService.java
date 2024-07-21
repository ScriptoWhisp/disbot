package com.nqma.disbot.service;

import com.nqma.disbot.controller.RequestController;
import com.nqma.disbot.controller.requests.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExternalService {

    private final RequestController<Request> requestController = new RequestController<>();


}
