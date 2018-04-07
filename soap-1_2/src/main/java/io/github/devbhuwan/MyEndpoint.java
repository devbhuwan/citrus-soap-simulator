package io.github.devbhuwan;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class MyEndpoint {

    @PayloadRoot(localPart = "findMe")
    @ResponsePayload
    public Object findMe(@RequestPayload Object o) {

        return null;
    }
}
