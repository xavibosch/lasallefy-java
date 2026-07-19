package dao;

import model.Playlist;
import java.util.List;

public interface PlaylistsDAO {
    List<Playlist> getAll();

    void save(List<Playlist> playlists);
}
