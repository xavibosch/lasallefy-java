package manager;

import model.Song;
import model.Playlist;
import dao.SongsDAO;
import dao.PlaylistsDAO;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LibraryManager {
    private final SongsDAO songsDAO;
    private final PlaylistsDAO playlistsDAO;
    private List<Song> songs;
    private List<Playlist> playlists;

    public LibraryManager(SongsDAO songsDAO, PlaylistsDAO playlistsDAO) {
        this.songsDAO = songsDAO;
        this.playlistsDAO = playlistsDAO;
        this.songs = new ArrayList<>();
        this.playlists = new ArrayList<>();
    }

    public void loadData() {
        songs = new ArrayList<>(songsDAO.getAll());
        playlists = new ArrayList<>(playlistsDAO.getAll());
    }

    public List<Song> listSongs() {
        return new ArrayList<>(songs);
    }

    public List<Song> listPlayableSongs() {
        List<Song> result = new ArrayList<>();
        for (Song s : songs) {
            if (s.isPlayable()) {
                result.add(s);
            }
        }
        return result;
    }

    public List<Playlist> listPlaylists() {
        return new ArrayList<>(playlists);
    }

    public void addSong(Song song) {
        songs.add(song);
        saveSongs();
    }

    public boolean removeSong(String songId) {
        Song toRemove = findSongById(songId);
        if (toRemove == null) {
            return false;
        }
        if (isSongUsedInAnyPlaylist(toRemove.getId())) {
            return false;
        }
        songs.remove(toRemove);
        saveSongs();
        return true;
    }

    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
        savePlaylists();
    }

    public boolean removePlaylist(String playlistId) {
        Playlist toRemove = findPlaylistById(playlistId);
        if (toRemove != null) {
            playlists.remove(toRemove);
            savePlaylists();
            return true;
        }
        return false;
    }

    public boolean addSongToPlaylist(String playlistId, String songId) {
        Song song = findSongById(songId);
        Playlist playlist = findPlaylistById(playlistId);
        if (song == null || playlist == null) {
            return false;
        }
        // Guardamos el id real de la canción (s001), no lo que escribió el usuario (S001)
        playlist.addSongId(song.getId());
        savePlaylists();
        return true;
    }

    public boolean removeSongFromPlaylist(String playlistId, String songId) {
        Playlist playlist = findPlaylistById(playlistId);
        if (playlist == null) {
            return false;
        }
        boolean removed = playlist.removeSongId(songId);
        if (removed) {
            savePlaylists();
        }
        return removed;
    }

    public Song findSongById(String id) {
        for (Song s : songs) {
            if (s.getId().equalsIgnoreCase(id)) {
                return s;
            }
        }
        return null;
    }

    public Playlist findPlaylistById(String id) {
        for (Playlist p : playlists) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    public Map<String, Song> getSongMap() {
        Map<String, Song> map = new LinkedHashMap<>();
        for (Song s : songs) {
            map.put(s.getId(), s);
        }
        return map;
    }

    public String nextSongId() {
        int max = 0;
        for (Song s : songs) {
            int num = Integer.parseInt(s.getId().split("s")[1]);
            if (num > max) {
                max = num;
            }
        }
        return "s" + pad(max + 1);
    }

    public String nextPlaylistId() {
        int max = 0;
        for (Playlist p : playlists) {
            int num = Integer.parseInt(p.getId().split("p")[1]);
            if (num > max) {
                max = num;
            }
        }
        return "p" + pad(max + 1);
    }

    // Devuelve el número con tres cifras: 1 -> "001", 25 -> "025", 120 -> "120"
    private String pad(int number) {
        if (number < 10) {
            return "00" + number;
        }
        if (number < 100) {
            return "0" + number;
        }
        return "" + number;
    }

    public void saveSongs() {
        songsDAO.save(songs);
    }

    public void savePlaylists() {
        playlistsDAO.save(playlists);
    }

    private boolean isSongUsedInAnyPlaylist(String songId) {
        for (Playlist p : playlists) {
            if (p.getSongIds().contains(songId)) {
                return true;
            }
        }
        return false;
    }
}
