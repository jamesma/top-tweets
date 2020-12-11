package com.jamesma.spring;

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

    private Long getNextId() {
        return tweetsDB.keySet()
                .stream()
                .mapToLong(value -> value)
                .max()
                .orElse(0) + 1;
    }
}
