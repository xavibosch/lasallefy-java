package controller;

import model.Song;
import model.Playlist;
import model.Sound;
import manager.LibraryManager;
import manager.PlaybackManager;
import manager.AlbumGenerator;
import view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Controller {
    private final View view;
    private final LibraryManager libraryManager;
    private final PlaybackManager playbackManager;
    private final AlbumGenerator albumGenerator;

    private static final String[] MOODS = {"HAPPY", "SAD", "RELAX", "ENERGETIC"};
    private static final String[] STYLES = {"CHIPTUNE", "CLASSICAL", "GAME", "AMBIENT", "LOFI", "ELECTRONIC"};
    private static final String[] TIMBRES = {"SINE", "SQUARE", "TRIANGLE", "SAWTOOTH"};

    public Controller(View view, LibraryManager libraryManager,
                      PlaybackManager playbackManager, AlbumGenerator albumGenerator) {
        this.view = view;
        this.libraryManager = libraryManager;
        this.playbackManager = playbackManager;
        this.albumGenerator = albumGenerator;
    }

    public void run() {
        boolean running = true;
        while (running) {
            view.show("\n■■■ laSallefy ■■■");
            view.show("1. Gestionar canciones");
            view.show("2. Gestionar playlists");
            view.show("3. Reproducir");
            view.show("4. Generar álbum random por mood");
            view.show("Q. Salir");

            String option = view.getString("Elige una opción:").toUpperCase();
            try {
                switch (option) {
                    case "1": manageSongs(); break;
                    case "2": managePlaylists(); break;
                    case "3": playMenu(); break;
                    case "4": generateRandomAlbum(); break;
                    case "Q": running = false; break;
                    default: view.show("Opción no válida.");
                }
            } catch (RuntimeException e) {
                view.show("Error: " + e.getMessage());
            }
        }
        view.show("Hasta pronto.");
    }

    private void manageSongs() {
        boolean back = false;
        while (!back) {
            view.show("\n--- Gestión de canciones ---");
            view.show("1. Listar canciones");
            view.show("2. Añadir canción");
            view.show("3. Eliminar canción");
            view.show("B. Volver");
            String option = view.getString("Elige una opción:").toUpperCase();
            switch (option) {
                case "1": listSongs(); break;
                case "2": addSong(); break;
                case "3": removeSong(); break;
                case "B": back = true; break;
                default: view.show("Opción no válida.");
            }
        }
    }

    private void managePlaylists() {
        boolean back = false;
        while (!back) {
            view.show("\n--- Gestión de playlists ---");
            view.show("1. Listar playlists");
            view.show("2. Crear playlist");
            view.show("3. Añadir canción a playlist");
            view.show("4. Eliminar canción de playlist");
            view.show("5. Eliminar playlist");
            view.show("B. Volver");
            String option = view.getString("Elige una opción:").toUpperCase();
            switch (option) {
                case "1": listPlaylists(); break;
                case "2": createPlaylist(); break;
                case "3": addSongToPlaylist(); break;
                case "4": removeSongFromPlaylist(); break;
                case "5": removePlaylist(); break;
                case "B": back = true; break;
                default: view.show("Opción no válida.");
            }
        }
    }

    private void playMenu() {
        boolean back = false;
        while (!back) {
            view.show("\n--- Reproducción ---");
            view.show("1. Reproducir canción");
            view.show("2. Reproducir playlist");
            view.show("B. Volver");
            String option = view.getString("Elige una opción:").toUpperCase();
            switch (option) {
                case "1": playSong(); break;
                case "2": playPlaylist(); break;
                case "B": back = true; break;
                default: view.show("Opción no válida.");
            }
        }
    }

    private void listSongs() {
        view.show("\n--- Biblioteca de canciones ---");
        for (Song song : libraryManager.listSongs()) {
            view.show(song.toString());
        }
    }

    private void addSong() {
        String title = view.getString("Título:");
        String artist = view.getString("Artista:");
        int durationSeconds = view.getInteger("Duración aproximada en segundos:");
        String mood = askFromList("Mood:", MOODS);
        if (mood == null) {
            return;
        }
        String style = askFromList("Style:", STYLES);
        if (style == null) {
            return;
        }

        boolean playable = false;
        List<Sound> sounds = new ArrayList<>();

        if (askYesNo("¿Será reproducible? (s/n):")) {
            String timbre = askFromList("Timbre:", TIMBRES);
            if (timbre == null) {
                return;
            }
            int template = askTemplateOption();
            if (template == 0) {
                return;
            }
            sounds = buildTemplate(template, timbre);
            playable = true;
        }

        Song song = new Song(libraryManager.nextSongId(), title, artist,
                durationSeconds, mood, style, playable, sounds);
        libraryManager.addSong(song);
        view.show("Canción añadida con id " + song.getId() + ".");
    }

    // Devuelve 1-4 según la plantilla, o 0 si el usuario escribe B para volver atrás.
    private int askTemplateOption() {
        while (true) {
            view.show("Plantillas disponibles:");
            view.show("1. Happy Birthday");
            view.show("2. Escala ascendente (Do-Re-Mi-Fa-Sol)");
            view.show("3. Arpegio (Do-Mi-Sol)");
            view.show("4. Tono de aviso (3 pitidos)");
            view.show("B. Volver atrás");
            String input = view.getString("Elige plantilla:").toUpperCase();
            if (input.equals("B")) {
                return 0;
            }
            try {
                int selected = Integer.parseInt(input);
                if (selected >= 1 && selected <= 4) {
                    return selected;
                }
            } catch (NumberFormatException e) {
                // entrada no numérica
            }
            view.show("Opción no válida.");
        }
    }

    private List<Sound> buildTemplate(int template, String timbre) {
        List<Sound> sounds = new ArrayList<>();
        switch (template) {
            case 1: // Happy Birthday (inicio simplificado)
                sounds.add(new Sound(264, 300, timbre)); // Do
                sounds.add(new Sound(264, 150, timbre)); // Do
                sounds.add(new Sound(297, 450, timbre)); // Re
                sounds.add(new Sound(264, 450, timbre)); // Do
                sounds.add(new Sound(352, 450, timbre)); // Fa
                sounds.add(new Sound(330, 600, timbre)); // Mi
                break;
            case 2: // Escala ascendente
                sounds.add(new Sound(264, 300, timbre)); // Do
                sounds.add(new Sound(297, 300, timbre)); // Re
                sounds.add(new Sound(330, 300, timbre)); // Mi
                sounds.add(new Sound(352, 300, timbre)); // Fa
                sounds.add(new Sound(396, 300, timbre)); // Sol
                break;
            case 3: // Arpegio
                sounds.add(new Sound(264, 250, timbre)); // Do
                sounds.add(new Sound(330, 250, timbre)); // Mi
                sounds.add(new Sound(396, 250, timbre)); // Sol
                sounds.add(new Sound(528, 500, timbre)); // Do agudo
                break;
            case 4: // Tono de aviso
                sounds.add(new Sound(440, 200, timbre));
                sounds.add(new Sound(440, 200, timbre));
                sounds.add(new Sound(440, 400, timbre));
                break;
            default:
                break;
        }
        return sounds;
    }

    private void removeSong() {
        listSongs();
        String songId = view.getString("Id de la canción a eliminar:");
        boolean removed = libraryManager.removeSong(songId);
        if (removed) {
            view.show("Canción eliminada.");
        } else {
            view.show("No se puede eliminar: no existe o está usada en una playlist.");
        }
    }

    private void listPlaylists() {
        view.show("\n--- Playlists ---");
        Map<String, Song> songMap = libraryManager.getSongMap();
        for (Playlist p : libraryManager.listPlaylists()) {
            int dur = p.getTotalDurationSeconds(songMap);
            int play = p.getPlayableSongCount(songMap);
            int noplay = p.getNonPlayableSongCount(songMap);
            view.show(String.format("%s | %s | canciones=%d | duración=%s | reproducibles=%d | no reproducibles=%d%s",
                    p.getId(), p.getName(), p.getSongCount(),
                    formatDuration(dur), play, noplay,
                    p.isRandom() ? " | RANDOM" : ""));
        }
    }

    private void createPlaylist() {
        String name = view.getString("Nombre de la playlist:");
        String description = view.getString("Descripción (puede quedar vacía):");
        String mood = null;
        if (askYesNo("¿Quieres asignar mood principal? (s/n):")) {
            mood = askFromList("Mood:", MOODS);
        }

        Playlist playlist = new Playlist(libraryManager.nextPlaylistId(),
                name, description, mood, new ArrayList<>(), false);
        libraryManager.addPlaylist(playlist);
        view.show("Playlist creada con id " + playlist.getId() + ".");
    }

    private void addSongToPlaylist() {
        listPlaylists();
        String playlistId = view.getString("Id de la playlist:");
        listSongs();
        String songId = view.getString("Id de la canción a añadir:");
        if (libraryManager.addSongToPlaylist(playlistId, songId)) {
            view.show("Canción añadida a la playlist.");
        } else {
            view.show("No se ha podido añadir. Revisa los ids.");
        }
    }

    private void removeSongFromPlaylist() {
        listPlaylists();
        String playlistId = view.getString("Id de la playlist:");
        Playlist p = libraryManager.findPlaylistById(playlistId);
        if (p == null) {
            view.show("La playlist no existe.");
            return;
        }
        view.show("Canciones dentro de la playlist: " + p.getSongIds());
        String songId = view.getString("Id de la canción a quitar:");
        if (libraryManager.removeSongFromPlaylist(playlistId, songId)) {
            view.show("Canción eliminada de la playlist.");
        } else {
            view.show("No se ha encontrado esa canción en la playlist.");
        }
    }

    private void removePlaylist() {
        listPlaylists();
        String playlistId = view.getString("Id de la playlist a eliminar:");
        if (libraryManager.removePlaylist(playlistId)) {
            view.show("Playlist eliminada.");
        } else {
            view.show("No existe una playlist con ese id.");
        }
    }

    private void playSong() {
        view.show("\n--- Canciones reproducibles ---");
        for (Song s : libraryManager.listPlayableSongs()) {
            view.show(s.toString());
        }
        String songId = view.getString("Id de la canción a reproducir:");
        Song song = libraryManager.findSongById(songId);
        if (song == null) {
            view.show("La canción no existe.");
            return;
        }
        view.show("Reproduciendo: " + song.getTitle() + " - " + song.getArtist());
        playbackManager.playSongAsync(song);
        interactiveControlMenu();
        view.show("Reproducción finalizada.");
    }

    private void playPlaylist() {
        listPlaylists();
        String playlistId = view.getString("Id de la playlist a reproducir:");
        Playlist playlist = libraryManager.findPlaylistById(playlistId);
        if (playlist == null) {
            view.show("La playlist no existe.");
            return;
        }
        List<Song> songsToPlay = playbackManager.getPlayableSongsForPlaylist(playlistId);
        if (songsToPlay.isEmpty()) {
            view.show("Esta playlist no tiene canciones reproducibles.");
            return;
        }
        view.show("Reproduciendo playlist (" + songsToPlay.size() + " canciones):");
        for (Song s : songsToPlay) {
            view.show("  - " + s.getTitle() + " - " + s.getArtist());
        }
        playbackManager.playSongsAsync(songsToPlay);
        interactiveControlMenu();
        view.show("Playlist reproducida.");
    }

    private void interactiveControlMenu() {
        while (playbackManager.isPlaying()) {
            String opt = view.getString("P = pausar / reanudar  |  ENTER = volver al menú:").toUpperCase();
            if (!playbackManager.isPlaying()) {
                break;
            }
            if (opt.equals("P")) {
                if (playbackManager.isPaused()) {
                    playbackManager.resumeSong();
                    view.show("Reanudado.");
                } else {
                    playbackManager.pauseSong();
                    view.show("Pausado.");
                }
            } else if (opt.equals("")) {
                // ENTER: paramos y volvemos al menú
                playbackManager.stopPlayback();
                return;
            } else {
                view.show("Opción no válida. Usa P o ENTER.");
            }
        }
    }

    private void generateRandomAlbum() {
        String mood = askFromList("Mood:", MOODS);
        if (mood == null) {
            return;
        }
        int targetMinutes = view.getInteger("Duración objetivo en minutos:");
        Playlist playlist = albumGenerator.generateRandomAlbum(mood, targetMinutes);
        view.show("Álbum generado: " + playlist.getName() + " con " + playlist.getSongCount() + " canción(es).");
    }

    // Pregunta sí/no. Solo acepta s o n; si se escribe otra cosa, repite.
    private boolean askYesNo(String prompt) {
        while (true) {
            String r = view.getString(prompt).trim().toLowerCase();
            if (r.equals("s")) {
                return true;
            }
            if (r.equals("n")) {
                return false;
            }
            view.show("Opción no válida. Escribe s o n.");
        }
    }

    // Muestra una lista numerada. Devuelve la opción elegida, o null si el usuario
    // escribe B para volver atrás. Repite si la opción no es válida.
    private String askFromList(String prompt, String[] options) {
        while (true) {
            view.show(prompt);
            for (int i = 0; i < options.length; i++) {
                view.show((i + 1) + ". " + options[i]);
            }
            view.show("B. Volver atrás");
            String input = view.getString("Elige número:").toUpperCase();
            if (input.equals("B")) {
                return null;
            }
            try {
                int selected = Integer.parseInt(input);
                if (selected >= 1 && selected <= options.length) {
                    return options[selected - 1];
                }
            } catch (NumberFormatException e) {
                // entrada no numérica: cae al mensaje de abajo
            }
            view.show("Opción no válida.");
        }
    }

    private String formatDuration(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        String secondsText = "" + seconds;
        if (seconds < 10) {
            secondsText = "0" + seconds;
        }
        return minutes + ":" + secondsText;
    }
}
