package starter;

import javafx.scene.control.Button;

public class EpisodeButton extends Button {

    private int season;
    private int episode;

    public EpisodeButton(String text, int season, int episode){
        super(text);
        this.season = season;
        this.episode = episode;
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
        return episode;
    }
}
