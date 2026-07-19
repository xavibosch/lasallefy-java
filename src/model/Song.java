package model;

import java.util.ArrayList;
import java.util.List;

public class Song {
    private String id;
    private String title;
    private String artist;
    private int durationSeconds;
    private String mood;
    private String style;
    private boolean playable;
    private List<Sound> sounds;

    public Song(String id, String title, String artist, int durationSeconds,
                String mood, String style, boolean playable, List<Sound> sounds) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.durationSeconds = durationSeconds;
        this.mood = mood;
        this.style = style;
        this.playable = playable;
        this.sounds = sounds == null ? new ArrayList<>() : new ArrayList<>(sounds);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public String getMood() {
        return mood;
    }

    public String getStyle() {
        return style;
    }

    public List<Sound> getSounds() {
        return new ArrayList<>(sounds);
    }

    public boolean isPlayable() {
        return playable && sounds != null && !sounds.isEmpty();
    }

    public String getPlayableLabel() {
        return isPlayable() ? "PLAYABLE" : "NOT PLAYABLE";
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %ds | %s | %s | [%s]",
                id, title, artist, durationSeconds, mood, style, getPlayableLabel());
    }
}
