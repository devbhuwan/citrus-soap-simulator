package io.github.devbhuwan;

import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.interceptor.EndpointInterceptorAdapter;

/**
 * @author Bhuwan Prasad Upadhyay
 */
@Component
public class CbsEndpointInterceptor extends EndpointInterceptorAdapter {

    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
        return super.handleRequest(messageContext, endpoint);
    }
}
