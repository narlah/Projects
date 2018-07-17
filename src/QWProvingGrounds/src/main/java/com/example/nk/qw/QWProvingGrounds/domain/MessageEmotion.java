package com.example.nk.qw.QWProvingGrounds.domain;


import org.springframework.data.repository.CrudRepository;

public class MessageEmotion extends Message {
    public MessageEmotion(String type, String payload, CrudRepository messageRepo) {
        super(type, payload, messageRepo);
    }

    @Override
    public boolean isValidMessage() {
        return this.payload.matches("^[\\D*]{2,10}$");
    }
}
