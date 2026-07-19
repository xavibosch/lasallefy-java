package dao;

import model.Song;
import java.util.List;

public interface SongsDAO {
    List<Song> getAll();

    void save(List<Song> songs);
}
