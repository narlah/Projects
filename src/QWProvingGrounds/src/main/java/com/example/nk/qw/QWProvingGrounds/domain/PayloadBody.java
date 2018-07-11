package com.example.nk.qw.QWProvingGrounds.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;


@ToString
public class PayloadBody {

    @Setter
    @Getter
    @NotNull
    public String payload;
}
