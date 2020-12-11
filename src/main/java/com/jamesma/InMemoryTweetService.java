package com.jamesma;

import com.jamesma.util.BoundedPriorityQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jamesma.Config.NUM_TOP_RETWEETS_TO_DISPLAY;

/**
 * In-memory database implementation of Twitter. All Tweets are held in a HashMap. For future
 * optimizations (not done due to time-constrain), this can be moved to persistence such as MySQL.
 * See README.md for discussion on this.
 */
@Slf4j
@Service
public class InMemoryTweetService implements TweetService {

    static Map<Long, Tweet> tweetsDB = new HashMap<>();
    static BoundedPriorityQueue<Tweet> boundedPriorityQueue = new BoundedPriorityQueue<>(NUM_TOP_RETWEETS_TO_DISPLAY);

    @Override
    public List<Tweet> findAll() {
        return new ArrayList<>(tweetsDB.values());
    }

    @Override
    public List<Tweet> findByTopRetweets(int numTweets) {
        Object[] topTweets = boundedPriorityQueue.toArray();
        if (topTweets == null) {
            return Collections.emptyList();
        }

        Arrays.sort(topTweets, Collections.reverseOrder());
        return Stream.of(topTweets).map(o -> (Tweet) o).collect(Collectors.toList());
    }

    @Override
    public void save(TweetForm tweetForm) {
        Tweet tweet = new Tweet(tweetForm.getContent());
        tweet.setId(getNextId());
        tweetsDB.put(tweet.getId(), tweet);
        boundedPriorityQueue.offer(tweet);
    }

    @Override
    public void retweet(long tweetId) {
        Tweet tweet = tweetsDB.get(tweetId);
        if (tweet != null) {
            tweet.incrementRetweets();
            if (!boundedPriorityQueue.contains(tweet)) {
                log.info("PQ doesn't contain retweeted {}, offering it.", tweet);
                boundedPriorityQueue.offer(tweet);
            }
        }
    }

    // Monotonically increasing ID.
    private Long getNextId() {
        return tweetsDB.keySet()
                .stream()
                .mapToLong(value -> value)
                .max()
                .orElse(0) + 1;
    }
}
