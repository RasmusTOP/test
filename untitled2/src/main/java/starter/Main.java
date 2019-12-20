package starter;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javafx.application.Platform;


public class Main extends Application {
    private Model myModel;
    private Controller myController;
    private Text errorMessageText;
    private VBox contentContainer = new VBox();
    private VBox categoryButtonContainer = new VBox();

    TextField searchField = new TextField();

    private CheckBox movieButton = new CheckBox("Movies");
    private CheckBox seriesButton = new CheckBox("Series");

    protected PasswordField passwordField = new PasswordField();
    protected TextField userNameField = new TextField();

    protected PasswordField createPasswordField = new PasswordField();
    protected PasswordField createRePasswordField = new PasswordField();
    protected TextField createUserNameField = new TextField();

    private ArrayList<Stage> stages = new ArrayList<>();
    protected MediaPlayer player;

    //private InputStream filmStream  = getClass().getResourceAsStream("/resources/HackingTimeV1.mp4").;
    //Makes model, loads content, and makes controller
    public Main() throws Exception,IOException, URISyntaxException {
        //final JFXPanel fxPanel = new JFXPanel();
        System.out.println("loaded");
       myModel = new Model();
       myModel.loadUsers();
       myModel.loadContent();
       myModel.generateCategoryList();

       myController = new Controller(myModel, this);
        System.out.println("loadedtroll");
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        createLoginStage();
        createMainStage();
        createErrorStage();
        createUserCreationStage();
        createPickEpisodeStage();
        createMediaPlayerStage();

        // Show the Stage (window)
        showSpecificStage(stages.get(0));
    }

    public static void main(String[] args) {
        launch(args);
    }
    

    //Gets shownContent from model, and displays each media-object in contentContainer
    void updateShownContent(){
        contentContainer.getChildren().clear();
        for(Object i : myModel.getShownContent()){
            HBox mediaDisplay = new HBox();
            Medie media = (Medie) i;
            mediaDisplay.getChildren().add(new ImageView(media.getPictureFile()));

            VBox mediaInfo = new VBox();
            for(String content : (media.display())){
                mediaInfo.getChildren().add(new Label(content));
            }
            Button playButton = new Button("Play");
            if(i instanceof Movie){playButton.setOnAction(myController.playMovieButtonClick);}
            if(i instanceof Series){playButton.setOnAction(myController.playSeriesButtonClick);}
            playButton.setUserData(i);
            mediaInfo.getChildren().add(playButton);
            if(myModel.getCurrentUser().myList.values().contains(i)){
                Button myListAdder = new Button("Remove from my list");
                myListAdder.setUserData(i);
                myListAdder.setOnAction(myController.removeFromMyListButtonClick);
                mediaInfo.getChildren().add(myListAdder);
            }
            else if (!myModel.getCurrentUser().myList.values().contains(i)){
                Button myListAdder = new Button("Add to my list");
                myListAdder.setUserData(i);
                myListAdder.setOnAction(myController.addToMyListButtonClick);
                mediaInfo.getChildren().add(myListAdder);
            }

            mediaDisplay.getChildren().add(mediaInfo);

            contentContainer.getChildren().add(mediaDisplay);
        }
    }

    // resets all checkboxes and the search field
    void resetFilters(){
        for(Node node : categoryButtonContainer.getChildren()){
            CheckBox checkBox = (CheckBox) node;
            checkBox.setSelected(false);
        }
        movieButton.setSelected(true);
        seriesButton.setSelected(true);

        searchField.clear();
    }

    //Creates and adds main-window to list of stages
    void createMainStage(){
        Stage mainStage = new Stage();

        // Create non-category buttons
        Button allButton = new Button("My list");
        movieButton.setSelected(true);
        seriesButton.setSelected(true);
        Button resetButton = new Button("reset");
        Button logoutButton = new Button("Log out");

        // Set actions on the non-category buttons
        allButton.setOnAction(myController.myListButtonClick);
        movieButton.setOnAction(myController.mediaTypeButtonClick);
        seriesButton.setOnAction(myController.mediaTypeButtonClick);
        resetButton.setOnAction(myController.resetButtonClick);
        logoutButton.setOnAction(myController.logoutButtonClick);

        // Creates and adds actions to all category buttons, also makes category buttons scrollable
        ScrollPane categoryScroll = new ScrollPane();
        categoryScroll.setPrefViewportWidth(100);
        for(Object categoryName : myModel.getCategoryList()){
            CheckBox categoryButton = new CheckBox((String) categoryName);
            categoryButton.setOnAction(myController.categoryButtonClick);
            categoryButtonContainer.getChildren().add(categoryButton);
        }
        categoryScroll.setContent(categoryButtonContainer);

        // add action to searchField
        searchField.setOnKeyReleased(myController.searchFunction);

        // Create a new grid pane, to become primary pane
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setMinSize(300, 300);

        // Create scrollPane, add contentContainer to it, for contentContainer to be scrollable
        ScrollPane scroll = new ScrollPane();
        scroll.setPrefViewportWidth(500);
        scroll.setContent(contentContainer);

        // Adds the non-category buttons into the pane
        pane.add(allButton, 0, 0);
        pane.add(movieButton, 1, 0);
        pane.add(seriesButton, 1, 1);
        pane.add(resetButton, 0, 2);
        pane.add(logoutButton, 5,0);

        // Adds category buttons into the pane
        pane.add(categoryScroll, 0, 3);

        // Adds Scrollable VBox to pane, to show media objects
        pane.add(scroll, 4, 3);

        // Adds searchFiled to pane
        pane.add(searchField, 4, 0);

        // JavaFX must have a Scene (window content) inside a Stage (window)
        Scene scene = new Scene(pane, 1000,600);
        mainStage.setTitle("Main Stage");
        mainStage.setScene(scene);

        stages.add(mainStage);
        mainStage.setOnCloseRequest(myController.mainStageCloser);
    }

    //Creates and adds login-window to list of stages
    void createLoginStage(){
        Stage loginStage = new Stage();
        GridPane pane = new GridPane();
        HBox buttonArrangement = new HBox();
        Label usernameLable = new Label("Username");
        Label passwordLable = new Label("Password");
        pane.add(passwordLable, 0,2);
        pane.add(usernameLable,0,0);
        pane.add(userNameField, 0,1);
        pane.add(passwordField,0,3);

        Button loginButton = new Button("Log In");
        loginButton.setOnAction(myController.loginButtonClick);
        Button createUserButton = new Button("Create New User");
        createUserButton.setOnAction(myController.createUserButtonClick);
        buttonArrangement.getChildren().addAll(createUserButton,loginButton);
        pane.add(buttonArrangement,0,4);

        Scene scene = new Scene(pane, 200, 130);
        loginStage.setTitle("Login Stage");
        loginStage.setScene(scene);

        stages.add(loginStage);
    }

    //Creates and adds error-window to list of stages
    void createErrorStage() {
        Stage errorStage = new Stage();
        stages.add(errorStage);
    }

    //Creates and adds episodePicker-window to list of stages
    void createPickEpisodeStage(){
        Stage pickEpisodeStage = new Stage();
        stages.add(pickEpisodeStage);
        pickEpisodeStage.focusedProperty().addListener((ov, onHidden, onShown) -> {
            if(!pickEpisodeStage.isFocused())
                Platform.runLater(() -> pickEpisodeStage.close());
        });

    }

    //Creates and adds createUser-window to list of stages
    void createUserCreationStage() {
        Stage UserCreation = new Stage();

        GridPane pane = new GridPane();
        HBox buttonArrangement = new HBox();
        Button createUserButton = new Button("Create");
        createUserButton.setOnAction(myController.createNewUserButtonClick);
        Button backButton = new Button("Back");
        backButton.setOnAction(myController.backButtonClick);

        pane.add(createUserNameField, 0,1);
        pane.add(createPasswordField,0,3);
        pane.add(createRePasswordField,0,5);
        Label usernameLable = new Label("Username");
        Label passwordLable = new Label("Password");
        Label repasswordLable = new Label("Reenter Password");
        pane.add(passwordLable, 0,2);
        pane.add(repasswordLable, 0,4);
        pane.add(usernameLable,0,0);
        buttonArrangement.getChildren().addAll(backButton,createUserButton);
        buttonArrangement.setSpacing(70);
        pane.add(buttonArrangement,0,6);
        Scene scene = new Scene(pane, 200, 170);
        UserCreation.setTitle("User Creation Stage");
        UserCreation.setScene(scene);

        stages.add(UserCreation);
    }

    //Creates and adds player-window to list of stages
    void createMediaPlayerStage() throws URISyntaxException {
        //System.out.println(getClass().getResource("/resources/HackingTimeV1.mp4").toURI().toString());
        Media m = new Media(getClass().getClassLoader().getResource("HackingTimeV1.mp4").toURI().toString());
        Stage mediaPlayerStage = new Stage();
        StackPane pane = new StackPane();
        //File appDir = new File(System.getProperty("user.dir"));
        //URI uri = new URI("/Mp3/HackingTimeV1.mp4");
        // just to check if the file exists
        //Media m = new Media(uri.toString());
        //System.out.println(String.valueOf(filmStream));
        //Media m = new Media(new File(String.valueOf(filmStream)).toURI().toString());
        //StackPane pane = new StackPane();

        player = new MediaPlayer(m);
        MediaView mediaView = new MediaView(player);
        mediaView.setOnMouseClicked(myController.mediaClick);

        pane.getChildren().add(mediaView);
        Scene scene = new Scene(pane, 1000, 720);
        mediaPlayerStage.setScene(scene);
        mediaPlayerStage.setOnCloseRequest(myController.mediaStageCloser);
        stages.add(mediaPlayerStage);
    }

    //Opens the player
    void showMediaPlayerStage(String mediaName) {
        stages.get(5).setTitle(mediaName);
        stages.get(5).show();
        player.play();
    }

    //Makes an error-window pop-up
    void showError(String errorMessage) {
        GridPane pane = new GridPane();
        errorMessageText = new Text(10, 50, errorMessage);
        pane.add(errorMessageText,0,0);
        Button okButton = new Button("Ok");
        pane.add(okButton,0,1);
        okButton.setOnAction(myController.okErrorButtonClick);
        Scene  errorScene = new Scene(pane, 200, 100);

        stages.get(2).setTitle("Error");
        stages.get(2).setScene(errorScene);
        stages.get(2).show();
    }

    //Generates all elements of the episodePicker and show the window
    void showPickEpisode(Series series){
        ScrollPane seasonScroll = new ScrollPane();
        HBox seasons = new HBox();
        for(int seasonNum = 0; seasonNum < series.seasons.length; seasonNum++){
            VBox seasonContent = new VBox();
            Label seasonLabel = new Label("Season " + (seasonNum+1));
            seasonContent.getChildren().add(seasonLabel);
            for(int episodeNum = 0; episodeNum < series.seasons[seasonNum].length; episodeNum++){
                EpisodeButton episodeButton = new EpisodeButton("Episode " + (episodeNum+1), seasonNum+1, episodeNum+1);
                episodeButton.setPrefWidth(90);
                episodeButton.setUserData(series);
                episodeButton.setOnAction(myController.episodeButtonClick);
                seasonContent.getChildren().add(episodeButton);
            }
            seasons.getChildren().add(seasonContent);
        }
        seasonScroll.setContent(seasons);
        seasonScroll.setMaxWidth(90 * 15 + 20);
        seasonScroll.setPrefWidth(90 * series.seasons.length);
        Scene pickEpisodeScene;
        if(series.seasons.length < 16){
            pickEpisodeScene = new Scene(seasonScroll,90*series.seasons.length+20,400);
        }
        else{
            pickEpisodeScene = new Scene(seasonScroll, 90 * 15 + 20, 400);
        }

        stages.get(4).setTitle(series.getTitle());
        stages.get(4).setScene(pickEpisodeScene);
        stages.get(4).show();
    }

    //Makes sure the main-window starts "fresh" when ever a user logs in
    void login(){
        myModel.showMovies();
        myModel.showSeries();
        myModel.updateValidContent();
        myModel.searchFor("");
        updateShownContent();

        showSpecificStage(stages.get(1));
    }

    //clears login page of inputs
    void logout(){
        userNameField.clear();
        passwordField.clear();

        showSpecificStage(stages.get(0));
    }

    //add user to users and shift to login stage
    void signUp() {
        showSpecificStage(stages.get(0));
        createPasswordField.clear();
        createRePasswordField.clear();
        createUserNameField.clear();
        userNameField.clear();
        passwordField.clear();

    }

    //shows createUser-window
    void signUpNU() {
        showSpecificStage(stages.get(3));
    }

    //Closes all stages, then opens the given stage
    void showSpecificStage(Stage stage) {
        for (Stage stg : stages) {
            stg.close();
        }
        stage.show();
    }

    void closeSpecificStage(int i) {
        stages.get(i).close();
    }
    ArrayList<Stage> getStages()
    {
        return stages;
    }
}