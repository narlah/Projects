package com.example.nk.qw.QWProvingGrounds.controller;

import com.example.nk.qw.QWProvingGrounds.domain.Message;
import com.example.nk.qw.QWProvingGrounds.domain.MessageFactory;
import com.example.nk.qw.QWProvingGrounds.domain.PayloadBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@NoArgsConstructor
@RestController
public class RestEndpoints {
    private MessageFactory factory;

    @Autowired
    public void setFactory(MessageFactory factory) {
        this.factory = factory;
    }

    @RequestMapping(value = "/messages/{type}", method = RequestMethod.POST)
    public ResponseEntity<?> postPayload(@Valid @RequestBody PayloadBody body, @PathVariable String type) {
        Message message = factory.createMessage(type, body.getPayload());
        message.persist();
        return message.getResponseEntity();//override in a message class to get different responses
    }

    @RequestMapping(value = "listAll", method = RequestMethod.GET)
    public String listThemAll() throws IOException {
        String str = factory.getMessageRepository().findAll().toString();

        ObjectMapper mapper = new ObjectMapper();
        Object json = mapper.readValue(str, Object.class);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        System.out.println(result );
        return result ;
    }
}
/*
ex :
curl  localhost:8080/messages/send_text   --data '{"payload":"1"}' -H "Content-Type: application/json;charset=UTF-8"
curl  localhost:8080/listAll
 */