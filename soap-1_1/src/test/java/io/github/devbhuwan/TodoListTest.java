package io.github.devbhuwan;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;
import com.consol.citrus.ws.client.WebServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class TodoListTest extends TestNGCitrusTestDesigner {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private WebServiceClient cbsClient;

    @Test
    @CitrusTest
    public void testTodo() {
        soap()
                .client(cbsClient)
                .send()
                .soapAction("urn:getTodo")
                .payload("<todo:getTodoRequest xmlns:todo=\"http://citrusframework.org/samples/todolist\"></todo:getTodoRequest>");

        soap()
                .client(cbsClient)
                .receive()
                .payload("<getTodoResponse xmlns=\"http://citrusframework.org/samples/todolist\">" +
                        "<todoEntry>" +
                        "<id>${todoId}</id>" +
                        "<title>${todoName}</title>" +
                        "<description>${todoDescription}</description>" +
                        "<done>false</done>" +
                        "</todoEntry>" +
                        "</getTodoResponse>");
    }

    @Test
    @CitrusTest
    public void testTodoList() {
        soap()
                .client(cbsClient)
                .send()
                .soapAction("urn:getTodoList")
                .payload("<todo:getTodoListRequest xmlns:todo=\"http://citrusframework.org/samples/todolist\"></todo:getTodoListRequest>");

        soap()
                .client(cbsClient)
                .receive()
                .payload("<getTodoListResponse xmlns=\"http://citrusframework.org/samples/todolist\">" +
                        "<list>" +
                        "<todoEntry>" +
                        "<id>${todoId}</id>" +
                        "<title>${todoName}</title>" +
                        "<description>${todoDescription}</description>" +
                        "<done>false</done>" +
                        "</todoEntry>" +
                        "</list>" +
                        "</getTodoListResponse>");
    }


}
