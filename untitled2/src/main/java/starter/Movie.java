package starter;

import javafx.scene.image.Image;

import java.io.File;

public class Movie extends Medie {
    protected File movieFileLocation;

    public Movie(String title, String rating, String releaseYear, String categoriesInput, Image pictureFileLocation){
        super(title, rating, releaseYear, categoriesInput, pictureFileLocation);
    }
}