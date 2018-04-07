package io.github.devbhuwan;

import com.consol.citrus.config.CitrusSpringConfig;
import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.endpoint.EndpointAdapter;
import com.consol.citrus.endpoint.adapter.RequestDispatchingEndpointAdapter;
import com.consol.citrus.endpoint.adapter.StaticResponseEndpointAdapter;
import com.consol.citrus.endpoint.adapter.mapping.HeaderMappingKeyExtractor;
import com.consol.citrus.endpoint.adapter.mapping.SimpleMappingStrategy;
import com.consol.citrus.endpoint.adapter.mapping.SoapActionMappingKeyExtractor;
import com.consol.citrus.variable.GlobalVariables;
import com.consol.citrus.ws.client.WebServiceClient;
import com.consol.citrus.ws.interceptor.LoggingClientInterceptor;
import com.consol.citrus.ws.server.WebServiceServer;
import com.sun.xml.messaging.saaj.soap.ver1_2.SOAPMessageFactory1_2Impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ImportResource("classpath:before.xml")
@Import(CitrusSpringConfig.class)
@ComponentScan(basePackageClasses = EndpointConfig.class)
public class EndpointConfig {

    private static final int PORT = 8080;
    private static final Logger LOG = LoggerFactory.getLogger(EndpointConfig.class);
    private static final String WS_CBS_URI = "/services/ws/CBS/v1";


    @Bean
    public GlobalVariables globalVariables() {
        GlobalVariables variables = new GlobalVariables();
        variables.getVariables().put("todoId", "702c4a4e-5c8a-4ce2-a451-4ed435d3604a");
        variables.getVariables().put("todoName", "todo_1871");
        variables.getVariables().put("todoDescription", "Description: todo_1871");
        return variables;
    }

    @Bean
    public LoggingClientInterceptor clientLoggingInterceptor() {
        return new LoggingClientInterceptor();
    }

    @Bean("messageFactory")
    public WebServiceMessageFactory messageFactory() {
        return new SaajSoapMessageFactory(new SOAPMessageFactory1_2Impl());
    }

    @Bean
    public WebServiceClient cbsClient() {
        String uri = "http://localhost:" + PORT + WS_CBS_URI;
        LOG.info("------------------CBS CLIENT---------------------");
        LOG.info("cbsClient uri=[{}]", uri);
        LOG.info("------------------CBS CLIENT---------------------");
        return CitrusEndpoints.soap()
                .client()
                .defaultUri(uri)
                .messageFactory(messageFactory())
                .interceptor(clientLoggingInterceptor())
                .build();
    }

    @Bean
    public WebServiceServer cbsServer(WebServiceMessageFactory messageFactory, List<EndpointInterceptor> interceptors) throws Exception {
        LOG.info("------------------CBS SERVER---------------------");
        LOG.info("Running CBS Server at port [{}]", PORT);
        LOG.info("------------------CBS SERVER---------------------");
        return CitrusEndpoints.soap()
                .server()
                .port(PORT)
                .endpointAdapter(dispatchingEndpointAdapter())
                .interceptors(interceptors)
                .timeout(10000)
                .messageFactory("messageFactory")
                .autoStart(true)
                .rootParentContext(true)
                .build();
    }

    @Bean
    public RequestDispatchingEndpointAdapter dispatchingEndpointAdapter() {
        RequestDispatchingEndpointAdapter dispatchingEndpointAdapter = new RequestDispatchingEndpointAdapter();
        dispatchingEndpointAdapter.setMappingKeyExtractor(mappingKeyExtractor());
        dispatchingEndpointAdapter.setMappingStrategy(mappingStrategy());
        return dispatchingEndpointAdapter;
    }

    @Bean
    public HeaderMappingKeyExtractor mappingKeyExtractor() {
        return new SoapActionMappingKeyExtractor();
    }

    @Bean
    public SimpleMappingStrategy mappingStrategy() {
        SimpleMappingStrategy mappingStrategy = new SimpleMappingStrategy();

        Map<String, EndpointAdapter> mappings = new HashMap<>();

        mappings.put("urn:getTodo", todoResponseAdapter());
        mappings.put("urn:getTodoList", todoListResponseAdapter());

        mappingStrategy.setAdapterMappings(mappings);
        return mappingStrategy;
    }

    @Bean
    public EndpointAdapter todoResponseAdapter() {
        StaticResponseEndpointAdapter endpointAdapter = new StaticResponseEndpointAdapter();
        endpointAdapter.setMessagePayload("<getTodoResponse xmlns=\"http://citrusframework.org/samples/todolist\">" +
                "<todoEntry xmlns=\"http://citrusframework.org/samples/todolist\">" +
                "<id>${todoId}</id>" +
                "<title>${todoName}</title>" +
                "<description>${todoDescription}</description>" +
                "<done>false</done>" +
                "</todoEntry>" +
                "</getTodoResponse>");
        return endpointAdapter;
    }

    @Bean
    public EndpointAdapter todoListResponseAdapter() {
        StaticResponseEndpointAdapter endpointAdapter = new StaticResponseEndpointAdapter();
        endpointAdapter.setMessagePayload("<getTodoListResponse xmlns=\"http://citrusframework.org/samples/todolist\">" +
                "<list>" +
                "<todoEntry>" +
                "<id>${todoId}</id>" +
                "<title>${todoName}</title>" +
                "<description>${todoDescription}</description>" +
                "<done>false</done>" +
                "</todoEntry>" +
                "</list>" +
                "</getTodoListResponse>");
        return endpointAdapter;
    }

}
