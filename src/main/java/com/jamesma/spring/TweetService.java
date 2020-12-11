package com.jamesma.spring;

import java.util.List;

public interface TweetService {

    List<Tweet> findAll();

    void save(TweetForm tweetForm);
}
