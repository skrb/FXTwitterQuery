package net.javainthebox.twitter.fx;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import twitter4j.QueryResult;
import twitter4j.TwitterException;

public class QueryService extends Service<QueryResult> {
    private TwitterQuery twitterQuery;
    private String keyword;

    public QueryService(TwitterQuery twitterQuery, String keyword) {
        this.twitterQuery = twitterQuery;
        this.keyword = keyword;
    }

    @Override
    protected Task<QueryResult> createTask() {
        Task<QueryResult> task = new Task<QueryResult>() {
            @Override
            protected QueryResult call() throws TwitterException {
                return twitterQuery.search(keyword);
            }
        };

        return task;
    }
}
