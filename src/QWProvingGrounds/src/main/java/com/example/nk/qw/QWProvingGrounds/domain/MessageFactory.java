package com.example.nk.qw.QWProvingGrounds.domain;

public class MessageFactory {

    public MessageFactory() {
    }

    public Message createMessage(String type, String payload) {
        switch (type) {
            case "send_text": {
                return new MessageText(type, payload);
            }
            case "send_emotion": {
                return new MessageEmotion(type, payload);
            }

            //<- Add more types here

            default: {
                return new UnrecognisedMessage(type, payload);
            }

        }
    }
}
