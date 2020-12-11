package com.jamesma.spring;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Tweet {

    private long id;
    private String content;
    private long retweets;

    protected Tweet() {}

    public Tweet(String content) {
        this.content = content;
    }

    public void incrementRetweets() {
        retweets++;
    }
}
