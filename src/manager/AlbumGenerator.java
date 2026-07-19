package manager;

import model.Song;
import model.Playlist;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlbumGenerator {
    private final LibraryManager libraryManager;
    private final Random random;

    public AlbumGenerator(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
        this.random = new Random();
    }

    public Playlist generateRandomAlbum(String mood, int targetMinutes) {
        int minutes = targetMinutes;
        if (minutes < 1) {
            minutes = 1;
        }
        int targetSeconds = minutes * 60;
        List<Song> candidates = new ArrayList<>();

        for (Song song : libraryManager.listSongs()) {
            if (song.isPlayable() && song.getMood().equalsIgnoreCase(mood)) {
                candidates.add(song);
            }
        }

        if (candidates.isEmpty()) {
            throw new IllegalStateException("No hay canciones reproducibles con mood " + mood + ".");
        }

        // Barajamos las canciones cambiando cada una por otra al azar
        for (int i = 0; i < candidates.size(); i++) {
            int j = random.nextInt(candidates.size());
            Song temp = candidates.get(i);
            candidates.set(i, candidates.get(j));
            candidates.set(j, temp);
        }

        int maxDuration = 0;
        for (Song s : candidates) {
            if (s.getDurationSeconds() > maxDuration) {
                maxDuration = s.getDurationSeconds();
            }
        }
        int limit = targetSeconds + maxDuration;

        List<String> selectedIds = new ArrayList<>();
        int accumulated = 0;

        for (Song s : candidates) {
            if (accumulated >= targetSeconds) break;
            if (accumulated + s.getDurationSeconds() <= limit) {
                selectedIds.add(s.getId());
                accumulated += s.getDurationSeconds();
            }
        }

        if (selectedIds.isEmpty()) {
            selectedIds.add(candidates.get(0).getId());
        }

        String id = libraryManager.nextPlaylistId();
        Playlist playlist = new Playlist(
                id,
                "Random " + mood + " – " + targetMinutes + "min",
                "Álbum generado automáticamente con canciones de mood " + mood,
                mood,
                selectedIds,
                true
        );
        libraryManager.addPlaylist(playlist);
        return playlist;
    }
}
