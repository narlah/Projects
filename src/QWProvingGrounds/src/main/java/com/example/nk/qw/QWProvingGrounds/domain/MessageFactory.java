package com.example.nk.qw.QWProvingGrounds.domain;

import com.example.nk.qw.QWProvingGrounds.repositories.MessageRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageFactory {

    @Autowired
    MessageRequestRepository messageRepository;
    //<- Can Autowire additional crud repos, and can overload the persist method to store the info
    //in different formats and/or tables/dbs

    public MessageFactory() {
    }

    public Message createMessage(String type, String payload) {
        switch (type) {
            case "send_text": {
                return new MessageText(type, payload,messageRepository);
            }
            case "send_emotion": {
                return new MessageEmotion(type, payload, messageRepository);
            }

            //<- Add more types here

            default: {
                return new UnrecognisedMessage(type, payload, messageRepository);
            }

        }
    }
}
