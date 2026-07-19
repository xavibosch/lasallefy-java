package dao;

import model.Song;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonSongsDAO implements SongsDAO {
    private final String filename;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonSongsDAO(String filename) {
        this.filename = filename;
    }

    @Override
    public List<Song> getAll() {
        try {
            JsonReader reader = new JsonReader(new FileReader(filename));
            Song[] songs = gson.fromJson(reader, Song[].class);
            reader.close();
            if (songs == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(Arrays.asList(songs));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void save(List<Song> songs) {
        try {
            FileWriter fw = new FileWriter(filename);
            fw.write(gson.toJson(songs));
            fw.close();
        } catch (Exception e) {
            throw new DataAccessException("Error guardando canciones: " + e.getMessage(), e);
        }
    }
}
