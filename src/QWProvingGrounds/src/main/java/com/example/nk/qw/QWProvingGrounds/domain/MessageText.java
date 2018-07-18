package com.example.nk.qw.QWProvingGrounds.domain;

import org.springframework.data.repository.CrudRepository;

public class MessageText extends Message {

    public MessageText(String type, String payload, CrudRepository messageRepo) {
        super(type, payload, messageRepo);
    }

    @Override
    public boolean isValidMessage() {
        int len = this.getLen();
        return len >= 1 && len <= 160; //this.payload.matches("^.{1,160}$"); is slower i think
    }
}
