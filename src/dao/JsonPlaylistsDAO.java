package dao;

import model.Playlist;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonPlaylistsDAO implements PlaylistsDAO {
    private final String filename;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonPlaylistsDAO(String filename) {
        this.filename = filename;
    }

    @Override
    public List<Playlist> getAll() {
        try {
            JsonReader reader = new JsonReader(new FileReader(filename));
            Playlist[] playlists = gson.fromJson(reader, Playlist[].class);
            reader.close();
            if (playlists == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(Arrays.asList(playlists));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void save(List<Playlist> playlists) {
        try {
            FileWriter fw = new FileWriter(filename);
            fw.write(gson.toJson(playlists));
            fw.close();
        } catch (Exception e) {
            throw new DataAccessException("Error guardando playlists: " + e.getMessage(), e);
        }
    }
}
