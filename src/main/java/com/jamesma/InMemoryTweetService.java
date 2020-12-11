package com.jamesma;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InMemoryTweetService implements TweetService {

    static Map<Long, Tweet> tweetsDB = new HashMap<>();

    @Override
    public List<Tweet> findAll() {
        return new ArrayList<>(tweetsDB.values());
    }

    @Override
    public void save(TweetForm tweetForm) {
        Tweet tweet = new Tweet(tweetForm.getContent());
        tweet.setId(getNextId());
        tweetsDB.put(tweet.getId(), tweet);
    }

    @Override
    public void retweet(long tweetId) {
        Tweet tweet = tweetsDB.get(tweetId);
        if (tweet != null) {
            tweet.incrementRetweets();
        }
    }

    private Long getNextId() {
        return tweetsDB.keySet()
                .stream()
                .mapToLong(value -> value)
                .max()
                .orElse(0) + 1;
    }
}
