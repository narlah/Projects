package com.example.nk.qw.QWProvingGrounds;

import com.example.nk.qw.QWProvingGrounds.controller.RestEndpoints;
import com.example.nk.qw.QWProvingGrounds.dbEntities.MessageRequest;
import com.example.nk.qw.QWProvingGrounds.domain.MessageFactory;
import com.example.nk.qw.QWProvingGrounds.repositories.MessageRequestRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration(value = "")
public class QwProvingGroundsApplicationTests {

    private MessageRequestRepository repositoryStub;
    MessageFactory factoryMock = new MessageFactory();
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        repositoryStub = new StubMessageRequestRepository();
        factoryMock.setMessageRepository(repositoryStub);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        ((RestEndpoints) wac.getBean("restEndpoints")).setFactory(factoryMock);
    }

    @Test
    public void testTheORM() throws Exception {
        String[] strArr = new String[]{"something0", "something1", "something2", "something3"};
        for (String text : strArr) {
            this.mockMvc.perform(
                    post("/messages/send_text")
                            .contentType("application/json;charset=UTF-8")
                            .content("{ \"payload\": \"" + text + "\"}"))
                    .andDo(print()).andExpect(status().is(HttpStatus.CREATED.value()))
                    .andExpect(content().contentType("text/plain;charset=UTF-8"));
        }

        System.out.println("All Elements saved so far : ");
        repositoryStub.findAll().forEach(s -> System.out.println(s));
        Assert.assertTrue(repositoryStub.existsById(1L));
        Assert.assertTrue(repositoryStub.existsById(2L));
        Assert.assertTrue(repositoryStub.existsById(3L));
        Assert.assertTrue(repositoryStub.existsById(4L));

        Assert.assertFalse(repositoryStub.existsById(5L));

        Optional<MessageRequest> optional = repositoryStub.findById(1L);
        Assert.assertTrue(optional.isPresent());
        Assert.assertTrue(optional.get().getPayload().equals("something0"));

        Optional<MessageRequest> optional4 = repositoryStub.findById(4L);
        Assert.assertTrue(optional4.isPresent());
        Assert.assertTrue(optional4.get().getPayload().equals("something3"));
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = wac.getServletContext();
        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean("restEndpoints"));
    }

    //messages/{type}-"send_text" "send_emotion"
    @Test
    public void correctTextSend1() throws Exception {
        String payload = "something1";
        this.mockMvc.perform(
                post("/messages/send_text")
                        .contentType("application/json;charset=UTF-8")
                        .content("{ \"payload\": \"" + payload + "\"}"))
                .andDo(print()).andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
        //.andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void inCorrectTextSend1() throws Exception {
        String payload = "";
        this.mockMvc.perform(
                post("/messages/send_text")
                        .contentType("application/json;charset=UTF-8")
                        .content("{ \"payload\": \"" + payload + "\"}"))
                .andDo(print()).andExpect(status().is(HttpStatus.PRECONDITION_FAILED.value()))
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    public void correctEmotionSend1() throws Exception {
        String payload = "corEmotion"; //length exactly 10
        this.mockMvc.perform(
                post("/messages/send_emotion")
                        .contentType("application/json;charset=UTF-8")
                        .content("{ \"payload\": \"" + payload + "\"}"))
                .andDo(print()).andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    public void inCorrectEmotionSend1() throws Exception {
        String payload = "corEmotion-------"; //longer than length 10
        this.mockMvc.perform(
                post("/messages/send_emotion")
                        .contentType("application/json;charset=UTF-8")
                        .content("{ \"payload\": \"" + payload + "\"}"))
                .andDo(print()).andExpect(status().is(HttpStatus.PRECONDITION_FAILED.value()))
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    public void unrecognizedMessageTypeSend1() throws Exception {
        String payload = "lorem ipsum";  //anything really
        this.mockMvc.perform(
                post("/messages/bungalow")
                        .contentType("application/json;charset=UTF-8")
                        .content("{ \"payload\": \"" + payload + "\"}"))
                .andDo(print()).andExpect(status().is(HttpStatus.PRECONDITION_FAILED.value()))
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }


}



