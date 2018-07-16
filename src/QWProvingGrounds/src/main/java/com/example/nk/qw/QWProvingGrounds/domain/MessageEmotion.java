package com.example.nk.qw.QWProvingGrounds.domain;


public class MessageEmotion extends Message {
    public MessageEmotion(String type, String payload) {
        super(type, payload);
    }

    @Override
    public boolean isValidMessage() {
        return this.payload.matches("^[\\D*]{2,10}$");
    }
}
