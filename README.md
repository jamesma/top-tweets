```
  __                __                 __    
 / /____  ___  ____/ /__    _____ ___ / /____
/ __/ _ \/ _ \/___/ __/ |/|/ / -_) -_) __(_-<
\__/\___/ .__/    \__/|__,__/\__/\__/\__/___/
       /_/                                   
```

Simple Twitter website which maintains top 10 retweeted tweets.

Environment Set Up
------------------

### Pre-requisites

* Stable and recent version of Docker Desktop installed. Tested with Docker Server + Client `19.03.13`.
* At least Maven version `3.0.5` installed. Tested with Maven `3.6.3`.
* Java version `11` installed. Tested with AdoptOpenJDK `11.0.9`.

### Bring up the stack

1. Build the image: `mvn clean package`
2. Bring up the stack comprising of **jamesma/top-tweets** using Docker Compose: `docker-compose up`

### Try out the stack

**top-tweets** is a Spring Boot application served at `http://localhost:8080`. There are a few pages accessible.

* `/tweets/all` displays all tweets available.
* `/tweets/top` displays top tweets by retweet count. Currently top 10 are displayed.
* `/tweets/create` displays page to post a new tweet.

### Bring down the stack

1. Hit `ctrl-C` to stop the stack that was started by `docker-compose up`
2. All-in-one command to rebuild image and restart the service `mvn clean package && docker-compose rm -f && docker-compose up`

Considerations
--------------

The current implementation to hold all tweets is an in-memory database using a `HashMap`. This is due to time-constrain.
Due to this, all data is lost on every restart which makes this implementation not ideal in any way for production.
For future improvements, this can be moved to persistence such as MySQL. The bounded priority queue should also be moved
to persistence (similarly MySQL will suffice). If this is a product that is intended to have high reliability and 
availability, it is recommended to host the DB instances in at least 2 different data centers (AZs) with sub-minute 
replication.

With the current schema of `Tweet`, whereby it contains only Id, Content (140 chars), Retweet count, we can store each
Tweet in roughly 8 bytes (long) + 560 bytes (chars) + 4 bytes (int) respectively, without substantial optimization. This
means that a single MySQL DB instance of 1TB should be able to hold close to 2 Million Tweets, which is do-able with
current offerings available through Cloud Providers such as AWS RDS.

That said, if we foresee data volume to reach a point in the future (months? years?) whereby all the data cannot be 
stored in a single instance, we should start with partitioning the data across DB instances. `Tweet ID` can be used as
the Key for this partitioning strategy. There are multiple partitioning algorithms to ensure new Tweets are distributed
evenly across shards. One of them is by taking a hash (MD5 checksum) of the key.

With regards to maintaining the top 10 tweets when we see data volumes reach 1K, 1M, 1T tweets.
* With an implementation of a bounded priority queue in persistence, rendering the top 10 page means that web server
nodes will have to query the bounded priority queue through a Database call over the network.
* Assuming that this use-case doesn't need to be updated in real-time on the order of seconds, it is sufficient to have
at most 5 minutes delay. As such, aforementioned expensive network calls can be optimized away by caching the top 10 
results for 5 minutes rolling windows in each web server node. This is a tradeoff of having lower latency and lower 
database utilization for an acceptable delay in consistency of the top 10 retweeted tweets.
* Assuming that retweets happen a lot more frequently than newly posted tweets. Another improvement is the 
implementation of a backend service that specifically handles the updating of retweet count. Web server nodes will not
process retweets, instead these retweet requests are produced to a queue (e.g. Apache Kafka) whereby the backend server
will consume from. This allows retweet counts to be aggregated and more efficiently processed as database updates,
consuming less database CPU/memory resources than having web servers perform these database updates.
* The point above scales well for future additions to the website which involves more fields in the `Tweet` schema,
requiring more expensive database updates and yet can be made *asynchronously*. Synchronous features such as deletion of
Tweets will still have to be made directly from the web server node to the database.

Usually, I'd implement unit tests and integration tests to ensure there's at least 75% test coverage, but this isn't
done due to the time constraint. The ideal approach is to take on a TDD approach by implementing Red (failing) tests,
then implementing the interfaces and implementations to make them pass.