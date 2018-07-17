package com.example.nk.qw.QWProvingGrounds.domain;

import com.example.nk.qw.QWProvingGrounds.dbEntities.MessageRequest;
import com.example.nk.qw.QWProvingGrounds.repositories.MessageRequestRepository;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;


public abstract class Message {
    String type;
    String payload;
    @Getter(AccessLevel.PACKAGE)
    int len;
    @Getter(AccessLevel.PACKAGE)
    boolean valid;

    public CrudRepository messageRepo;

    public Message(String type, String payload, CrudRepository messageRepo) {
        this.type = type;
        this.payload = payload;
        this.valid = this.isValidMessage();
        this.len = payload.length();
        this.messageRepo = messageRepo;
    }


    public abstract boolean isValidMessage();

    public void persist() {
        messageRepo.save(new MessageRequest(this.type, this.payload, new Date()));
    }

    public ResponseEntity<String> getResponseEntity() {
        HttpStatus status;
        if (this.valid)
            status = HttpStatus.CREATED; //201
        else
            status = HttpStatus.PRECONDITION_FAILED; //412
        return new ResponseEntity<>("", status);
    }
}
