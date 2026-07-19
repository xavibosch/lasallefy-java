package manager;

import model.Song;
import model.Playlist;
import model.Sound;
import synth.SoundSynth;
import synth.SoundSynthSine;
import synth.SoundSynthSquare;
import synth.SoundSynthTriangle;
import synth.SoundSynthSawtooth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaybackManager {
    private final LibraryManager libraryManager;
    private final Map<String, SoundSynth> synths;

    private Thread playbackThread;
    private volatile boolean paused;
    private volatile boolean stopped;
    private volatile Song currentSong;

    public PlaybackManager(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
        this.synths = new HashMap<>();
        synths.put("SINE", new SoundSynthSine());
        synths.put("SQUARE", new SoundSynthSquare());
        synths.put("TRIANGLE", new SoundSynthTriangle());
        synths.put("SAWTOOTH", new SoundSynthSawtooth());
    }

    public void playSongAsync(Song song) {
        if (song == null) {
            throw new IllegalArgumentException("La canción no puede ser nula.");
        }
        if (!song.isPlayable()) {
            throw new IllegalStateException("La canción '" + song.getTitle() + "' no es reproducible.");
        }
        if (isPlaying()) {
            throw new IllegalStateException("Ya hay una canción en reproducción.");
        }

        paused = false;
        stopped = false;
        currentSong = song;

        playbackThread = new Thread(() -> {
            reproducir(song);
        });
        playbackThread.start();
    }

    public void playSongsAsync(List<Song> songs) {
        if (songs == null || songs.isEmpty()) {
            throw new IllegalArgumentException("No hay canciones para reproducir.");
        }
        if (isPlaying()) {
            throw new IllegalStateException("Ya hay una reproducción en curso.");
        }

        paused = false;
        stopped = false;

        playbackThread = new Thread(() -> {
            for (Song song : songs) {
                if (stopped) {
                    return;
                }
                currentSong = song;
                reproducir(song);
            }
            currentSong = null;
        });
        playbackThread.start();
    }

    // Reproduce todos los sonidos de una canción, respetando la pausa y el stop
    private void reproducir(Song song) {
        for (Sound sound : song.getSounds()) {
            if (stopped) {
                return;
            }
            while (paused && !stopped) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    return;
                }
            }
            if (stopped) {
                return;
            }
            SoundSynth synth = synths.get(sound.getTimbre());
            if (synth == null) {
                continue;
            }
            try {
                synth.makeSound(sound.getFrequency(), sound.getDurationMs());
            } catch (Exception e) {
                // si falla un sonido concreto, seguimos con el siguiente
            }
        }
    }

    public void pauseSong() {
        paused = true;
    }

    public void resumeSong() {
        paused = false;
    }

    // Detiene la reproducción y espera a que el hilo termine
    public void stopPlayback() {
        stopped = true;
        paused = false;
        if (playbackThread != null) {
            try {
                playbackThread.join();
            } catch (InterruptedException e) {
                // la espera se interrumpió; la reproducción se detiene igualmente
            }
        }
        currentSong = null;
    }

    public boolean isPlaying() {
        return playbackThread != null && playbackThread.isAlive();
    }

    public boolean isPaused() {
        return paused;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public List<Song> getPlayableSongsForPlaylist(String playlistId) {
        Playlist playlist = libraryManager.findPlaylistById(playlistId);
        if (playlist == null) {
            throw new IllegalArgumentException("No existe la playlist " + playlistId + ".");
        }
        List<Song> result = new ArrayList<>();
        for (String songId : playlist.getSongIds()) {
            Song song = libraryManager.findSongById(songId);
            if (song != null && song.isPlayable()) {
                result.add(song);
            }
        }
        return result;
    }
}
