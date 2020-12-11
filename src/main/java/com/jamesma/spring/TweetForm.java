package com.jamesma.spring;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class TweetForm {

    @NotNull
    @Size(min = 1, max = 140)
    private String content;
}
