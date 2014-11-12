package net.javainthebox.twitter.fx;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class TwitterQueryViewController implements Initializable {

    private Twitter twitter;
    private ObservableList<StatusView> statuses = FXCollections.observableArrayList();
    private DateFormat formatter = new SimpleDateFormat("YY/MM/dd hh:mm");

    @FXML
    private TextField field;
    @FXML
    private ImageView searchImage;

    @FXML
    private TableView<StatusView> table;
    @FXML
    private TableColumn<StatusView, Date> dateColumn;
    @FXML
    private TableColumn<StatusView, User> userColumn;
    @FXML
    private TableColumn<StatusView, String> textColumn;

    private Timeline glowAnimation;
    private Timeline nonglowAnimation;

    @FXML
    private void searchImagePressed(MouseEvent event) {
        search();
    }

    @FXML
    private void searchActionPerformed(ActionEvent event) {
        search();
    }

    private void search() {
        glowAnimation.play();

        String queryText = field.getText();

        if (!queryText.isEmpty()) {
            QueryService service = new QueryService(queryText);
            service.setOnSucceeded(e -> {
                QueryResult result = service.getValue();

                statuses.clear();

                result.getTweets().forEach(status -> {
                    statuses.add(new StatusView(status.getCreatedAt(),
                                                status.getUser(), 
                                                status.getText()));
                });

                nonglowAnimation.play();
            });
            service.setOnFailed(e -> nonglowAnimation.play());

            service.start();
        } else {
            nonglowAnimation.play();
        }
    }

    class QueryService extends Service<QueryResult> {

        private String queryText;

        public QueryService(String queryText) {
            this.queryText = queryText;
        }

        @Override
        protected Task<QueryResult> createTask() {
            Task<QueryResult> task = new Task<QueryResult>() {
                @Override
                protected QueryResult call() throws TwitterException {
                    Query query = new Query(queryText);
                    query.setCount(100);
                    return twitter.search(query);
                }
            };

            return task;
        }
    };

    private void initTable() {
        table.setPlaceholder(new Label());

        dateColumn.prefWidthProperty().bind(Bindings.multiply(table.widthProperty(), 0.2));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(new Callback<TableColumn<StatusView, Date>, TableCell<StatusView, Date>>() {
            @Override
            public TableCell<StatusView, Date> call(TableColumn<StatusView, Date> param) {
                return new TableCell<StatusView, Date>() {
                    @Override
                    public void updateItem(Date date, boolean empty) {
                        super.updateItem(date, empty);

                        if (!isEmpty()) {
                            Text text = new Text(formatter.format(date));
                            setGraphic(text);
                        }
                    }
                };
            }
        });

        userColumn.prefWidthProperty().bind(Bindings.multiply(table.widthProperty(), 0.3));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        userColumn.setCellFactory(new Callback<TableColumn<StatusView, User>, TableCell<StatusView, User>>() {
            @Override
            public TableCell<StatusView, User> call(TableColumn<StatusView, User> param) {
                return new TableCell<StatusView, User>() {
                    @Override
                    public void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
                        if (!isEmpty()) {
                            ImageView icon = new ImageView(new Image(user.getMiniProfileImageURLHttps(), true));
                            Label label = new Label(user.getName() + "\n@" + user.getScreenName(), icon);

                            setGraphic(label);
                        }
                    }
                };
            }
        });

        textColumn.prefWidthProperty().bind(Bindings.multiply(table.widthProperty(), 0.45));
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
        textColumn.setCellFactory(new Callback<TableColumn<StatusView, String>, TableCell<StatusView, String>>() {
            @Override
            public TableCell<StatusView, String> call(TableColumn<StatusView, String> param) {
                return new TableCell<StatusView, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setPadding(new Insets(2));
                        if (!isEmpty()) {
                            Text text = new Text(item.toString());
                            text.wrappingWidthProperty().bind(Bindings.subtract(textColumn.widthProperty(), 4));
                            this.setWrapText(true);

                            setGraphic(text);
                        }
                    }
                };
            }
        });

        table.setItems(statuses);
    }

    private void initAnimation() {
        DropShadow shadow = new DropShadow(0.0, 0.0, 0.0, Color.CYAN);
        searchImage.setEffect(shadow);

        glowAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(shadow.radiusProperty(), 0.0)),
                new KeyFrame(Duration.millis(200), new KeyValue(shadow.radiusProperty(), 3.0))
        );

        nonglowAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(shadow.radiusProperty(), 3.0)),
                new KeyFrame(Duration.millis(200), new KeyValue(shadow.radiusProperty(), 0.0))
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        twitter = new TwitterFactory().getInstance();

        initTable();
        initAnimation();
    }
}
