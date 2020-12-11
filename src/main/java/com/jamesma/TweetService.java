package com.jamesma;

import java.util.List;

public interface TweetService {

    List<Tweet> findAll();

    List<Tweet> findByTopRetweets(int numTweets);

    void save(TweetForm tweetForm);

    void retweet(long tweetId);
}
