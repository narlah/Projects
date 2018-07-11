package com.example.nk.qw.QWProvingGrounds.domain;

public class MessageText extends Message {

    public MessageText(String type, String payload) {
        super(type, payload);
    }

    @Override
    public boolean isValidMessage() {
        System.out.println(this.getLen());
        return this.getLen() >= 1 && this.getLen() <= 160; //this.payload.matches("^.{1,160}$");
    }
}
