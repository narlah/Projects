package com.example.nk.qw.QWProvingGrounds.domain;

import org.springframework.data.repository.CrudRepository;

public class MessageText extends Message {

    public MessageText(String type, String payload, CrudRepository messageRepo) {
        super(type, payload, messageRepo);
    }

    @Override
    public boolean isValidMessage() {
        System.out.println(this.getLen());
        return this.getLen() >= 1 && this.getLen() <= 160; //this.payload.matches("^.{1,160}$");
    }
}
