package com.example.nk.qw.QWProvingGrounds.dbEntities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="messageRequests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageRequest {

    @Id
    @GeneratedValue
    long id;

    public MessageRequest(String type, String payload, Date created_at) {
        this.type = type;
        this.payload = payload;
        this.created_at = created_at;
    }

    @Column
    String type;

    @Column
    String payload;

    @Column
    Date created_at;
}
