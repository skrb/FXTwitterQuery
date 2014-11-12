package net.javainthebox.twitter.fx;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class FXTwtterQuery extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        BorderPane root = FXMLLoader.load(getClass().getResource("TwitterQueryView.fxml"));
        Scene scene = new Scene(root);

        stage.setTitle("Twitter Query");
        stage.setScene(scene);
        stage.setMinWidth(200);
        stage.setMinHeight(200);
        
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
