package starter;

import javafx.scene.image.Image;

import javax.swing.*;
import java.net.URL;
import java.util.*;
import java.io.*;
import java.lang.*;
//java --module-path file:///Users/rasmuspedersen/Downloads/javafx-sdk-13.0.1/lib --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web -jar IdeaProjects/untitled2/build/libs/untitled2-1.0-SNAPSHOT.jar
public class Model{
    private JPanel mjp = new JPanel();
    private List<InputStream> mediaTypes = new ArrayList<InputStream>();
    private InputStream newUserFile;
    //= Model.class.getResourceAsStream("resources/NewUserData.txt");
    private InputStream userFile ;
   // =  Model.class.getResourceAsStream("resources/Users.txt");
    //private File userFile  = new File(userURL.getFile());
    private Formatter userFormat;
    private List<Medie> totalContent = new ArrayList<>();
    private List<Medie> validContent = new ArrayList<>();
    private List<Medie> shownContent = new ArrayList<>();

    private List<String> mediaTypeFilter = new ArrayList<>();
    private List<String> categoryFilter = new ArrayList<>();

    private ArrayList<String> presentCategories = new ArrayList<>();

    private User currentUser;
    private ArrayList<User> users  = new ArrayList<>();

    public boolean showingMyList = false;



    public Model() throws IOException {
        InputStream in = getClass().getResourceAsStream("/Users.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine();
        System.out.println(line);
        line = reader.readLine();
        System.out.println(line);
        line = reader.readLine();
        System.out.println(line);
        line = reader.readLine();
        System.out.println(line);
        line = reader.readLine();
        System.out.println(line);
        System.out.println(new Image(Thread.currentThread().getContextClassLoader().getResource( "Serier-billeder/Braveheart.jpg").toString()).getHeight());
        //URL fileUrl = ClassLoader.getSystemResource("/film.txt");
        //System.out.println(fileUrl.toString());
        //System.out.println(fileUrl + "" + new File(fileUrl.getFile()).canRead() + new File(fileUrl.getFile()).length());
        //InputStream movieFile = Model.class.getResourceAsStream("META-INF/resource/META-INF/film.txt");
       // mediaTypes.add(in);
        //if(mediaTypes.get(0) == null){System.out.println("fuck!!");}
        //System.out.println(mediaTypes.get(0).isFile() + "f?" + mediaTypes.get(0).canRead() + mediaTypes.get(0).toString());

        //InputStream seriesFile = Model.class.getResourceAsStream("resources/serier.txt");
      //  mediaTypes.add(seriesFile);
        //System.out.println(movieFile.toString());
        openFile();

    }
    //Reads the given .txt-files, creates media-objects (dynamic type depending on what .txt-file), and adds the media-objects to totalContent-ArrayList
    public void loadContent() throws IOException {
        System.out.println("loaded??");
                InputStream in1 = getClass().getResourceAsStream("/film.txt");
        mediaTypes.add(in1);
                InputStream in = getClass().getResourceAsStream("/serier.txt");
        mediaTypes.add(in);
            for(InputStream file : mediaTypes){
                BufferedReader reader = new BufferedReader(new InputStreamReader(file , "windows-1252"));
                String line = reader.readLine();
                System.out.println(line);
                while(line != null){
                    String[] data = line.split(";");
                    URL pictureFileLocation = Thread.currentThread().getContextClassLoader().getResource( "Serier-billeder/" + data[0] +".jpg");
                    System.out.println(pictureFileLocation.toString());
                    if(file == mediaTypes.get(0)){
                        //InputStream pictureFileLocation = getClass().getClassLoader().getResourceAsStream( "Film - billeder/" + data[0] +".jpg");
                        //File pictureFileLocationFile = new File(String.valueOf(pictureFileLocation));
                        totalContent.add(new Movie(data[0],data[3],data[1],data[2], new Image(Model.class.getResourceAsStream( "Film-billeder/" +data[0] +".jpg"))));
                    }
                    if(file == mediaTypes.get(1)){
                        //URL pictureFileLocation = Thread.currentThread().getContextClassLoader().getResource( "Serier-billeder/" + data[0] +".jpg");
                        //File pictureFileLocationFile = new File(String.valueOf(pictureFileLocation));
                        //totalContent.add(new Series(data[0],data[3],data[1],data[2], new Image(pictureFileLocation), data[4]));
                    }
                    System.out.println(line);
                    System.out.println("loaded1");
                    line = reader.readLine();
                }
                System.out.println("loaded55");
                reader.close();
            }


    }

    //loads all users from Users.txt, adds then to a list of users
    void loadUsers(){
        try{
            InputStream in = getClass().getResourceAsStream("/Users.txt");
            System.out.println("fuck!!fffffff" );
            int i = 0;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "windows-1252"));
            String line = reader.readLine();
            System.out.println(line + "well");
            while(line != null){
                System.out.println("fuck!!fffffff");
                String[] userInfo = line.split(";");
                users.add(new User(userInfo[0], userInfo[1]));
                if(userInfo.length > 2 ) {
                    for (int iT = 0; iT < userInfo.length - 2; iT++) {
                        users.get(i).myList.put(userInfo[iT + 2], null);
                    }
                }
                line = reader.readLine();
                i++;
            }
            reader.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //adds a single string for each found category in medias in totalContent, to the list of categories
    public void generateCategoryList(){
        for(Medie media : totalContent) {
            for (Object u : media.getCategories()) {
                if (!presentCategories.contains(u)) {
                    presentCategories.add((String) u);
                }
            }
        }
    }

    //return content to be shown
    public ArrayList getShownContent(){return (ArrayList) shownContent;}

    //return list of all categories
    public ArrayList getCategoryList(){return presentCategories;}


    //Different methods, for manipulating filters for valid content
    public void showMovies(){
        if(!mediaTypeFilter.contains("Movies")){
            mediaTypeFilter.add("Movies");
        }
    }
    public void notShowMovies(){
        if(mediaTypeFilter.contains("Movies")){
            mediaTypeFilter.remove("Movies");
        }
    }

    public void showSeries(){
        if(!mediaTypeFilter.contains("Series")){
            mediaTypeFilter.add("Series");
        }
    }
    public void notShowSeries(){
        if(mediaTypeFilter.contains("Series")){
            mediaTypeFilter.remove("Series");
        }
    }

    public void showCategory(String category){
        if(!mediaTypeFilter.contains(category)){
            categoryFilter.add(category);
        }
    }
    public void notShowCategory(String category) {
        if(categoryFilter.contains(category)){
            categoryFilter.remove(category);
        }
    }

    public void updateValidContent(){
        validContent.clear();
        for(Medie media : totalContent){
            if((mediaTypeFilter.contains("Movies") && media instanceof Movie) || (mediaTypeFilter.contains("Series") && media instanceof  Series)){
                validContent.add(media);
            }
        }
        validContent.removeIf(i -> (!i.getCategories().containsAll(categoryFilter)));
        showingMyList = false;
    }

    public void searchFor(String input){
        shownContent.clear();

        if(input.equals("")){shownContent.addAll(validContent);}

        String[] searchWords = input.toLowerCase().split(" ");

        // mediaTitle starts with input?
        for(Medie media : validContent){
            if(media.getTitle().toLowerCase().startsWith(input.toLowerCase()) && !shownContent.contains(media)){
                shownContent.add(media);
            }
        }

        // input wordSet exists in mediaTitleWordSet?
        for(Medie media : validContent){
            String[] mediaWords = media.getTitle().toLowerCase().split(" ");
            for (String searchWord : searchWords) {
                for (String mediaWord : mediaWords) {
                    if (mediaWord.equals(searchWord) && !shownContent.contains(media)) {
                        shownContent.add(media);
                    }
                }
            }
        }

        // inputs CharSet exists anywhere in mediaTitleCharSet?
        for(Medie media : validContent){
            if(media.getTitle().toLowerCase().contains(input.toLowerCase()) && !shownContent.contains(media)){
                shownContent.add(media);
            }
        }
        showingMyList = false;
    }

    // makes sure both series and movies are in the mediaTypeFilter and clears the categoryFilter.
    public void resetFilters(){
        showSeries();
        showMovies();
        categoryFilter.clear();
    }

    //Checks if given info is a user
    public String login(String username, String password){
        User loginUser = getUser(username);
        if (loginUser == null) {
            return "Wrong Username";
        }
        else if(loginUser.getPassword().equals(password)) {
            currentUser = loginUser;
            // go to away from login interface
            for(String s : currentUser.myList.keySet()) {
                currentUser.myList.replace(s,findSpecificMedia(s));
                //int i = 0; i < currentUser.myList.keySet().size(); i++
            }
            return "Logged in";
        }
        else {
            return "Wrong Password";
        }
    }

    //clears currentUser so it ready for the next user
    void logout(){
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public User getUser(String username) {
        for (User user: users) {
            if (user.getName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    //clears shownContent, then add currentsUsers myList to shown content
    public void showMyList(){
        shownContent.clear();
        shownContent.addAll(currentUser.myList.values());
        showingMyList = true;
    }

    public void openFile() {
        try {
            newUserFile =  Model.class.getResourceAsStream("/NewUserData.txt");
            userFormat = new Formatter(String.valueOf(newUserFile));
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }
    }

    public void addUser(String username, String password) {
        if(users.contains(getUser(username))) {
            System.out.println("User already exists");
        }
        else {
            openFile();
            userFormat.format("%s%s%s%s",username,";",password,";");
            userFormat.format(System.lineSeparator());
            userFormat.close();
        }
    }

    public void updateUsers(){
        try {
            if(currentUser != null ){updateUserMylist();}
            newUserFile =  Model.class.getResourceAsStream("/NewUserData.txt");
            userFile =  Model.class.getResourceAsStream("/Users.txt");
            BufferedReader file = new BufferedReader(new FileReader(String.valueOf(userFile)));
            BufferedReader tempFile = new BufferedReader(new FileReader(String.valueOf(newUserFile)));
            StringBuffer inputBuffer = new StringBuffer();
            String line;
            String tempLine;
            while ((line = file.readLine()) != null) {
                if(currentUser != null ) {
                    String[] userData = line.split(";");
                    if(userData[0].equals(currentUser.getName())) {
                        line = tempFile.readLine();
                    }
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            if(currentUser == null ) {
                while ((tempLine = tempFile.readLine()) != null) {
                    inputBuffer.append(tempLine);
                    inputBuffer.append('\n');
                }
            }
            FileOutputStream fileOut = new FileOutputStream(String.valueOf(userFile));
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
            loadUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUserMylist() {
        openFile();
        userFormat.format("%s",currentUser.getName()+";");
        userFormat.format("%s",currentUser.getPassword()+";");
        for(Medie media: currentUser.myList.values())
        {
            userFormat.format("%s",media.getTitle()+";");
        }
        userFormat.close();
    }

    public Medie findSpecificMedia(String name) {
        for(Medie media : totalContent){
            if(media.getTitle().equals(name)){
                return media;
            }
        }
        return null;
    }
}