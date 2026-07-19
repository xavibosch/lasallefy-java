package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Playlist {
    private String id;
    private String name;
    private String description;
    private String mood;
    private List<String> songIds;
    private boolean random;

    public Playlist(String id, String name, String description, String mood,
                    List<String> songIds, boolean random) {
        this.id = id;
        this.name = name;
        this.description = description == null ? "" : description;
        this.mood = mood;
        this.songIds = songIds == null ? new ArrayList<>() : new ArrayList<>(songIds);
        this.random = random;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMood() {
        return mood;
    }

    public List<String> getSongIds() {
        return new ArrayList<>(songIds);
    }

    public boolean isRandom() {
        return random;
    }

    public void addSongId(String songId) {
        if (!songIds.contains(songId)) {
            songIds.add(songId);
        }
    }

    public boolean removeSongId(String songId) {
        for (int i = 0; i < songIds.size(); i++) {
            if (songIds.get(i).equalsIgnoreCase(songId)) {
                songIds.remove(i);
                return true;
            }
        }
        return false;
    }

    public int getSongCount() {
        return songIds.size();
    }

    public int getTotalDurationSeconds(Map<String, Song> songMap) {
        int total = 0;
        for (String songId : songIds) {
            Song song = songMap.get(songId);
            if (song != null) {
                total += song.getDurationSeconds();
            }
        }
        return total;
    }

    public int getPlayableSongCount(Map<String, Song> songMap) {
        int total = 0;
        for (String songId : songIds) {
            Song song = songMap.get(songId);
            if (song != null && song.isPlayable()) {
                total++;
            }
        }
        return total;
    }

    public int getNonPlayableSongCount(Map<String, Song> songMap) {
        int total = 0;
        for (String songId : songIds) {
            Song song = songMap.get(songId);
            if (song == null || !song.isPlayable()) {
                total++;
            }
        }
        return total;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | canciones=%d%s",
                id, name, songIds.size(), random ? " | RANDOM" : "");
    }
}
