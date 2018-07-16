package com.example.nk.qw.QWProvingGrounds.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;


@ToString
@Setter
@Getter
public class PayloadBody {


    @NotNull
    public String payload;
}
