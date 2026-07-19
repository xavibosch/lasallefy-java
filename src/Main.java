import controller.Controller;
import dao.JsonPlaylistsDAO;
import dao.JsonSongsDAO;
import dao.PlaylistsDAO;
import dao.SongsDAO;
import manager.AlbumGenerator;
import manager.LibraryManager;
import manager.PlaybackManager;
import view.ConsoleView;
import view.View;

public class Main {
    public static void main(String[] args) {
        SongsDAO songsDAO = new JsonSongsDAO("songs.json");
        PlaylistsDAO playlistsDAO = new JsonPlaylistsDAO("playlists.json");

        LibraryManager libraryManager = new LibraryManager(songsDAO, playlistsDAO);
        libraryManager.loadData();

        View view = new ConsoleView();
        PlaybackManager playbackManager = new PlaybackManager(libraryManager);
        AlbumGenerator albumGenerator = new AlbumGenerator(libraryManager);
        Controller controller = new Controller(view, libraryManager, playbackManager, albumGenerator);
        controller.run();
    }
}
