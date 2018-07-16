package com.example.nk.qw.QWProvingGrounds.domain;

public class UnrecognisedMessage extends Message {


    public UnrecognisedMessage(String type, String payload) {
        super(type, payload);
    }

    @Override
    public boolean isValidMessage() {
        return false;
    }
}
