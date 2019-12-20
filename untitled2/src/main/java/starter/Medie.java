package starter;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.*;

public abstract class Medie {
    protected String title;
    protected String releaseYear;
    protected String rating;
    protected ArrayList<String> category;
    protected Image pictureFile;

    public Medie(String title, String rating, String releaseYear, String categoriesInput, Image pictureFile) {
        this.title = title;
        this.rating = rating.substring(1);
        this.releaseYear = releaseYear.substring(1);

        category = new ArrayList<String>();
        String[] categories = categoriesInput.split(",");
        for(String i : categories){
            category.add(i.substring(1));
        }

        try {
            this.pictureFile = new Image(new FileInputStream(String.valueOf(pictureFile)));
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public String getTitle()
    {
        return title;
    }
    public ArrayList getCategories()
    {
        return category;
    }
    public String getRating()
    {
        return rating;
    }
    public Image getPictureFile()
    {
        return pictureFile;
    }

    public ArrayList<String> display(){
        ArrayList<String> content = new ArrayList<>();
        content.add(title);
        content.add(releaseYear);
        String genreString = "";
        for(String genre : category){
            genreString += genre+", ";
        }
        content.add(genreString.substring(0, genreString.length()-2));
        content.add(rating);

        return content;
    }
}