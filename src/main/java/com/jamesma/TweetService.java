package com.jamesma;

import java.util.List;

public interface TweetService {

    List<Tweet> findAll();

    void save(TweetForm tweetForm);

    void retweet(long tweetId);
}
