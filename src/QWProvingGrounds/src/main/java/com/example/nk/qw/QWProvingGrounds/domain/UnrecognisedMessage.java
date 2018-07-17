package com.example.nk.qw.QWProvingGrounds.domain;

import org.springframework.data.repository.CrudRepository;

public class UnrecognisedMessage extends Message {

    public UnrecognisedMessage(String type, String payload, CrudRepository messageRepo) {
        super(type, payload, messageRepo);
    }

    @Override
    public boolean isValidMessage() {
        return false;
    }
}
