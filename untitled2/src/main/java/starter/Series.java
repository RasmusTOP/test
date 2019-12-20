package starter;

import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;

public class Series extends Medie {
    protected File[][] seasons;

    public Series(String title, String rating, String releaseYear, String categoriesInput, Image pictureFileLocation, String seasonDataString){
        super(title, rating, releaseYear, categoriesInput, pictureFileLocation);

        String[] seasonData = seasonDataString.split(",");
        this.seasons = new File[seasonData.length][];

        for(int i = 0; i < seasonData.length; i++) {
            String[] thisSeason = seasonData[i].split("-");
            int amountEpisodes = Integer.parseInt(thisSeason[1]);
            this.seasons[i] = new File[amountEpisodes];
        }
    }

    @Override
    public ArrayList<String> display(){
        ArrayList<String> content = super.display();
        content.add("Seasons: " + seasons.length);
        return content;
    }
}