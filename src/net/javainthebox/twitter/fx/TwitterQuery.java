package net.javainthebox.twitter.fx;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterQuery {

    private Twitter twitter;

    public TwitterQuery() {
        twitter = new TwitterFactory().getInstance();
    }

    public QueryResult search(String keyword) throws TwitterException {
        Query query = new Query(keyword);
        query.setCount(100);
        
        return twitter.search(query);
    }
}
