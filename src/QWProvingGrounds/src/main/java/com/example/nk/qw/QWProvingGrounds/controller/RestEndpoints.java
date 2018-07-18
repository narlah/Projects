package com.example.nk.qw.QWProvingGrounds.controller;

import com.example.nk.qw.QWProvingGrounds.domain.Message;
import com.example.nk.qw.QWProvingGrounds.domain.MessageFactory;
import com.example.nk.qw.QWProvingGrounds.domain.PayloadBody;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@NoArgsConstructor
@RestController
public class RestEndpoints {
    MessageFactory factory;

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
}
