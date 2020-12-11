package com.jamesma;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Tweet implements Comparable<Tweet> {

    private long id;
    private String content;
    private int retweets;

    protected Tweet() {}

    public Tweet(String content) {
        this.content = content;
    }

    public void incrementRetweets() {
        retweets++;
    }

    @Override
    public int compareTo(Tweet o) {
        return retweets - o.getRetweets();
    }
}
