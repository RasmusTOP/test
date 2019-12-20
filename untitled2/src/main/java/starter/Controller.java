package starter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.WindowEvent;

public class Controller{
    private Model myModel;
    private Main myMain;

    public Controller(Model myModel, Main myMain){
        this.myModel = myModel;
        this.myMain = myMain;
    }

    // makes myModel reset its filters, adds all of the current user myList to shownContent, then makes myMain reset filter and update shownContent
    EventHandler<ActionEvent> myListButtonClick = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event1){
            myModel.resetFilters();
            myModel.showMyList();

            myMain.resetFilters();
            myMain.updateShownContent();
        }
    };

    //makes myModel add or remove Movies or Series from it mediaTypeFilter, updates its validContent, search for the current search input, and makes myMain update shownContent
    EventHandler<ActionEvent> mediaTypeButtonClick = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event2){
            CheckBox chk = (CheckBox) event2.getSource();
            if(chk.getText().equals("Movies")){
                if(chk.isSelected()){myModel.showMovies();}
                if(!chk.isSelected()){myModel.notShowMovies();}
            }
            if(chk.getText().equals("Series")){
                if(chk.isSelected()){myModel.showSeries();}
                if(!chk.isSelected()){myModel.notShowSeries();}
            }
            myModel.updateValidContent();
            myModel.searchFor(myMain.searchField.getText());
            myMain.updateShownContent();
        }
    };

    // makes myModel add all content of a category to its shownContent ArrayList, then updates the view
    EventHandler<ActionEvent> categoryButtonClick = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event4){
            CheckBox chk = (CheckBox) event4.getSource();
            if(chk.isSelected()){ myModel.showCategory(chk.getText());}
            if(!chk.isSelected()){ myModel.notShowCategory(chk.getText());}
            myModel.updateValidContent();
            myModel.searchFor(myMain.searchField.getText());
            myMain.updateShownContent();
        }
    };

    // makes myModel run its search method with the input string, then updates the view
    EventHandler<KeyEvent> searchFunction = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event5) {
            myModel.searchFor(myMain.searchField.getText());
            myMain.updateShownContent();
        }
    };

    // myModel resets its filters, updates the valid content and searches for nothing, then myMain resets all its checkboxes and the searchField, then updates shownContent.
    EventHandler<ActionEvent> resetButtonClick = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event6){
            myModel.resetFilters();
            myModel.updateValidContent();
            myModel.searchFor("");

            myMain.resetFilters();
            myMain.updateShownContent();
        }
    };

    //Checks with myModel that the login is valid, if so logs user in, else makes myMain show an error pop-up
    EventHandler<ActionEvent> loginButtonClick = new EventHandler<ActionEvent>(){
        public void handle(ActionEvent event7) {
            if(myModel.login(myMain.userNameField.getText(),myMain.passwordField.getText()) == "Logged in") {
                myMain.login();
            }
            else{
                myMain.showError(myModel.login(myMain.userNameField.getText(),myMain.passwordField.getText()));
            }
        }
    };

    //Updates all saved user information, resets filters of myMain and myModel then logs user out of myMain and MyModel
    EventHandler<ActionEvent> logoutButtonClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event8) {
            myModel.updateUsers();
            myModel.resetFilters();
            myMain.resetFilters();
            myModel.logout();
            myMain.logout();
        }
    };

    //Changes the clicked buttons text and EventHandler, then adds the relevant media to users myList
    EventHandler<ActionEvent> addToMyListButtonClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event9) {
            Button btn = (Button) event9.getSource();
            btn.setText("Remove from my list");
            btn.setOnAction(removeFromMyListButtonClick);
            if(!myModel.getCurrentUser().myList.values().contains((Medie) btn.getUserData())){
                myModel.getCurrentUser().myList.put(((Medie) btn.getUserData()).title,(Medie) btn.getUserData());
            }
        }
    };

    //Changes the clicked buttons text and EventHandler, then removes the relevant media to users myList, updates shownContent if user is looking at its myList
    EventHandler<ActionEvent> removeFromMyListButtonClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event10) {
            Button btn = (Button) event10.getSource();
            btn.setText("Add to my list");
            btn.setOnAction(addToMyListButtonClick);
            if(myModel.getCurrentUser().myList.values().contains(btn.getUserData())){
                Medie thisMedia = (Medie) btn.getUserData();
                myModel.getCurrentUser().myList.remove(thisMedia.getTitle());
            }
            if(myModel.showingMyList){
                myModel.showMyList();
                myMain.updateShownContent();
            }
        }
    };

    //Makes myMain open the createUserWindow
    EventHandler<ActionEvent> createUserButtonClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event11) {
            myMain.signUpNU();
        }
    };

    //makes myMain play the relevant Movie
    EventHandler<ActionEvent> playMovieButtonClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            Button btn = (Button) actionEvent.getSource();
            Movie mvi = (Movie) btn.getUserData();
            myMain.getStages().get(1).hide();
            myMain.showMediaPlayerStage(mvi.getTitle());
        }
    };

    //Makes myMain open the EpisodePickerWindow for the relevant series
    EventHandler<ActionEvent> playSeriesButtonClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            Button btn = (Button) actionEvent.getSource();
            Series sri = (Series) btn.getUserData();
            myMain.showPickEpisode(sri);
        }
    };

    //makes myMain play the relevant episode
    EventHandler<ActionEvent> episodeButtonClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            EpisodeButton btn = (EpisodeButton) actionEvent.getSource();
            Series sri = (Series) btn.getUserData();
            myMain.getStages().get(1).hide();
            myMain.showMediaPlayerStage(sri.getTitle() + " Season " + btn.getSeason() + " Episode " + btn.getEpisode());
            myMain.getStages().get(4).hide();
        }
    };

    //pauses or plays current media.
    EventHandler<MouseEvent> mediaClick = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            MediaView mediaView = (MediaView) event.getSource();
            MediaPlayer mediaPlayer = mediaView.getMediaPlayer();
            if(mediaPlayer.getStatus().toString().equals("PLAYING")) {
                mediaPlayer.pause();
            }
            else if(mediaPlayer.getStatus().toString().equals("PAUSED")){
                mediaPlayer.play();
            }
        }
    };

    //Tries to generate and save a user, fails is something is not up to standards
    EventHandler<ActionEvent> createNewUserButtonClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event12) {
            if(!myMain.createUserNameField.getText().contains(";") && !myMain.createPasswordField.getText().contains(";") && !myMain.createRePasswordField.getText().contains(";") &&  myModel.getUser(myMain.createUserNameField.getText())== null && !myMain.createUserNameField.getText().equals("")  && !myMain.createPasswordField.getText().equals("") && myMain.createPasswordField.getText().equals( myMain.createRePasswordField.getText()) ) {
                myModel.addUser(myMain.createUserNameField.getText(), myMain.createPasswordField.getText());
                myMain.signUp();
                myModel.updateUsers();
            }
            else if (myModel.getUser(myMain.createUserNameField.getText()) != null && !myMain.createUserNameField.getText().equals("")) {
                myMain.showError("User already exists");
            }
            else if ( myMain.createUserNameField.getText().equals("")  || myMain.createPasswordField.getText().equals("") || myMain.createRePasswordField.getText().equals("")) {
                myMain.showError("Info missing");
            }
            else if(myMain.createUserNameField.getText().contains(";") || myMain.createPasswordField.getText().contains(";") || myMain.createRePasswordField.getText().contains(";")) {
                myMain.showError("; is not allowed in username or password");
            }
            else if (!myMain.createPasswordField.getText().equals( myMain.createRePasswordField.getText()) ) {
                myMain.showError("Your passwords does not match");
            }
        }
    };

    //closes error pop-up
    EventHandler<ActionEvent> okErrorButtonClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event13) {
            myMain.closeSpecificStage(2);
        }
    };

    //goes back from createUser to login-window
    EventHandler<ActionEvent> backButtonClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event14) {
            myMain.signUp();
        }
    };

    //updates users if main is closed
    EventHandler<WindowEvent> mainStageCloser = new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event16) {
            myModel.updateUsers();
        }
    };

    //opens main and episodePicker, if watching a series, if player is closed
    EventHandler<WindowEvent> mediaStageCloser = new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event16) {
            myMain.getStages().get(1).show();
            if(myMain.getStages().get(5).getTitle().contains("Season")){myMain.getStages().get(4).show();}
            myMain.player.stop();
        }
    };
}