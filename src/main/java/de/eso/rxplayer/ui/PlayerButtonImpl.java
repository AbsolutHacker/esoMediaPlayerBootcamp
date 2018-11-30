package de.eso.rxplayer.ui;

import de.eso.rxplayer.Album;
import de.eso.rxplayer.Track;
import de.eso.rxplayer.api.ApiResponse;
import de.eso.rxplayer.vertx.client.EntertainmentControlClient;
import de.eso.rxplayer.vertx.client.Launcher;
import io.reactivex.Observable;

import java.awt.event.ActionListener;
import java.util.Random;

class DisplayAlbumsButton extends PlayerButton {

  private String buttonText;

  @SuppressWarnings("SpellCheckingInspection")
  @Override
  void changeLanguage(Language language) {
    switch (language) {
      case English:
        buttonText = "All Albums";
        break;
      case German:
        buttonText = "Alle Alben";
        break;
      default:
        buttonText = "All Albums";
        break;
    }
    setText(buttonText);
  }

  @Override
  public String getText() {
    return buttonText;
  }

  @Override
  public ActionListener[] getActionListeners() {
    ActionListener[] actionListeners = new ActionListener[1];
    actionListeners[0] =
        e -> {
          System.out.println("DisplayAlbumsButton pressed");

          EntertainmentControlClient client = Launcher.getClient().blockingGet();
          Observable<ApiResponse> response$ = client.newRequest("/browse/albums", Album.class);
          response$.subscribe(response -> {

            for (Object result : response.getBody()) {
              Album album = null;
              try {
                album = (Album) result;
              } catch (ClassCastException classCastEx) {
                System.out.println("[WARNING] ClassCastException when getting album");
              }

              if (album != null) {
                System.out.println("[INFO] Got the album \"" + album.getName() + "\"");
                MediaPlayerDisplay.getMediaPlayerDisplay().setTrackAlbum(album.getName());
              }

            }
          });

        };
    return actionListeners;
  }
}

/**
 * Currently mostly the same code as the AllAlbums Button.
 * This might however change in the future, in regard to the UI.
 */
class SearchAlbumButton extends PlayerButton {

  private String buttonText;

  @SuppressWarnings("SpellCheckingInspection")
  @Override
  void changeLanguage(Language language) {
    switch (language) {
      case English:
        buttonText = "Search Album";
        break;
      case German:
        buttonText = "Album suchen";
        break;
      default:
        buttonText = "Search Album";
        break;
    }
    setText(buttonText);
  }

  @Override
  public String getText() {
    return buttonText;
  }

  @Override
  public ActionListener[] getActionListeners() {
    ActionListener[] actionListeners = new ActionListener[1];
    actionListeners[0] =
        e -> {
          System.out.println("SearchAlbumButton pressed");
          String searchedAlbum = "A Night at the Opera"; //Bohemian Rhapsody

          EntertainmentControlClient client = Launcher.getClient().blockingGet();
          Observable<ApiResponse> response$ = client.newRequest("/browse/albums/" + searchedAlbum, Album.class);
          response$.subscribe(response -> {

            for (Object result : response.getBody()) {
              Album album = null;
              try {
                album = (Album) result;
              } catch (ClassCastException classCastEx) {
                System.out.println("[WARNING] ClassCastException when getting album");
              }

              if (album != null) {
                System.out.println("[INFO] Got the album \"" + album.getName() + "\"");
                MediaPlayerDisplay.getMediaPlayerDisplay().setTrackAlbum(album.getName());
              }

            }
          });
        };
    return actionListeners;
  }
}

class SearchTrackButton extends PlayerButton {

  private String buttonText;

  @SuppressWarnings("SpellCheckingInspection")
  @Override
  void changeLanguage(Language language) {
    switch (language) {
      case English:
        buttonText = "Search Track";
        break;
      case German:
        buttonText = "Titel suchen";
        break;
      default:
        buttonText = "Search Track";
        break;
    }
    setText(buttonText);
  }

  @Override
  public String getText() {
    return buttonText;
  }

  @Override
  public ActionListener[] getActionListeners() {
    ActionListener[] actionListeners = new ActionListener[1];
    actionListeners[0] =
        e -> {
          System.out.println("SearchTrackButton pressed");
          String searchedTrack = "Bohemian Rhapsody";

          EntertainmentControlClient client = Launcher.getClient().blockingGet();
          Observable<ApiResponse> response$ = client.newRequest("/browse/tracks/" + searchedTrack, Track.class);
          response$.subscribe(response -> {

            for (Object result : response.getBody()) {
              Track track = null;
              try {
                track = (Track) result;
              } catch (ClassCastException classCastEx) {
                System.out.println("[WARNING] ClassCastException when searching track");
              }

              if (track != null) {
                System.out.println("[INFO] Got the track \"" + track.getTitle() + "\"");
                MediaPlayerDisplay.getMediaPlayerDisplay().setTrackTitle(track.getTitle());
              }

            }
          });
        };
    return actionListeners;
  }
}

class AlbumTracksButton extends PlayerButton {

  private String buttonText;

  @SuppressWarnings("SpellCheckingInspection")
  @Override
  void changeLanguage(Language language) {
    switch (language) {
      case English:
        buttonText = "Album Tracks";
        break;
      case German:
        buttonText = "Album Titel";
        break;
      default:
        buttonText = "Album Tracks";
        break;
    }
    setText(buttonText);
  }

  @Override
  public String getText() {
    return buttonText;
  }

  @Override
  public ActionListener[] getActionListeners() {
    ActionListener[] actionListeners = new ActionListener[1];
    actionListeners[0] =
        e -> {
          System.out.println("AlbumTracksButton pressed");
          EntertainmentControlClient client = Launcher.getClient().blockingGet();
          String album = "A Night at the Opera"; //Bohemian Rhapsody
          System.out.println("Started searching for the tracks of the album \"" + album + "\"");

          client.newRequest("/browse/albumTracks/" + album, Track.class).subscribe(apiResponse -> {
            for (Object response : apiResponse.getBody()) {
              Track track = null;
              try {
                track = (Track)response;
              } catch (ClassCastException classCastEx) {
                System.out.println("[WARNING] Failed to cast the current Track");
              }
              if (track != null) {
                System.out.println("[INFO] Got the track \"" + track.getTitle() + "\"");
                MediaPlayerDisplay.getMediaPlayerDisplay().setTrackTitle(track.getTitle());
              }
            }
          });
        };
    return actionListeners;
  }
}

class BackButton extends PlayerButton {

  @Override
  void changeLanguage(Language language) {
    // This button does not change with different languages
  }

  @Override
  public String getText() {
    return "<<";
  }

  @Override
  public ActionListener[] getActionListeners() {
    ActionListener[] actionListeners = new ActionListener[1];
    actionListeners[0] =
        e -> {
          System.out.println("BackButton pressed");
          EntertainmentControlClient client = Launcher.getClient().blockingGet();
          client.newRequest("/play/back", Void.class).subscribe();
        };
    return actionListeners;
  }
}

class StopButton extends PlayerButton {

  private String buttonText;

  @Override
  void changeLanguage(Language language) {
    switch (language) {
      case English:
        buttonText = "STOP";
        break;
      case German:
        buttonText = "STOP";
        break;
      default:
        buttonText = "STOP";
        break;
    }
    setText(buttonText);
  }

  @Override
  public String getText() {
    return buttonText;
  }

  @Override
  public ActionListener[] getActionListeners() {
    ActionListener[] actionListeners = new ActionListener[1];
    actionListeners[0] =
        e -> {
          System.out.println("StopButton pressed");
          EntertainmentControlClient client = Launcher.getClient().blockingGet();
          client.newRequest("/play/stop", Void.class).subscribe();
        };
    return actionListeners;
  }
}

class PlayPauseButton extends PlayerButton {

  private boolean isPlayMode = true; // if button press equals to play or pause
  private String buttonText;
  private Language currentLanguage;

  @SuppressWarnings("SpellCheckingInspection")
  @Override
  void changeLanguage(Language language) {
    currentLanguage = language;
    switch (language) {
      case English:
        buttonText = isPlayMode ? "PLAY" : "PAUSE";
        break;
      case German:
        buttonText = isPlayMode ? "SPIELEN" : "PAUSE";
        break;
      default:
        buttonText = isPlayMode ? "PLAY" : "PAUSE";
    }
    setText(buttonText);
  }

  @Override
  public String getText() {
    return buttonText;
  }

  @Override
  public ActionListener[] getActionListeners() {
    ActionListener[] actionListeners = new ActionListener[1];
    actionListeners[0] =
        e -> {
          if (!isPlayMode) {
            System.out.println("PlayButton pressed");
            isPlayMode = true;
            changeLanguage(currentLanguage);
            EntertainmentControlClient client = Launcher.getClient().blockingGet();
            client.newRequest("/play/play", Void.class).subscribe();

          } else {
            System.out.println("PauseButton pressed");
            isPlayMode = false;
            changeLanguage(currentLanguage);
            EntertainmentControlClient client = Launcher.getClient().blockingGet();
            client.newRequest("/play/pause", Void.class).subscribe();
          }
        };
    return actionListeners;
  }
}

class ForwardButton extends PlayerButton {

  @Override
  void changeLanguage(Language language) {
    // this button does not change with different languages
  }

  @Override
  public String getText() {
    return ">>";
  }

  @Override
  public ActionListener[] getActionListeners() {
    ActionListener[] actionListeners = new ActionListener[1];
    actionListeners[0] =
        e -> {
          System.out.println("ForwardButton pressed");
          EntertainmentControlClient client = Launcher.getClient().blockingGet();
          client.newRequest("/play/forward", Void.class).subscribe();
        };
    return actionListeners;
  }
}

class SwitchLanguageButton extends PlayerButton {

  private String buttonText;

  @SuppressWarnings("SpellCheckingInspection")
  @Override
  void changeLanguage(Language language) {
    switch (language) {
      case English:
        buttonText = "Switch Language";
        break;
      case German:
        buttonText = "Sprache Ändern";
        break;
      default:
        buttonText = "Switch Language";
        break;
    }
    setText(buttonText);
  }

  @Override
  public String getText() {
    return buttonText;
  }

  @Override
  public ActionListener[] getActionListeners() {
    ActionListener[] actionListeners = new ActionListener[1];
    actionListeners[0] =
        e -> {
          System.out.println("SwitchLanguageButton pressed");
          Language language;
          if (getText().equals("Switch Language")) {
            language = Language.German;
          } else {
            language = Language.English;
          }
          MediaBrowserDisplay.getMediaBrowserDisplay().changeLanguage(language);
        };
    return actionListeners;
  }
}

class SwitchColorButton extends PlayerButton {

  private String buttonText;

  @SuppressWarnings("SpellCheckingInspection")
  @Override
  void changeLanguage(Language language) {
    switch (language) {
      case English:
        buttonText = "Change Theme";
        break;
      case German:
        buttonText = "Thema Ändern";
        break;
      default:
        buttonText = "Change Theme";
        break;
    }
    setText(buttonText);
  }

  @Override
  public String getText() {
    return buttonText;
  }

  @Override
  public ActionListener[] getActionListeners() {
    ActionListener[] actionListeners = new ActionListener[1];
    actionListeners[0] =
        e -> {
          System.out.println("SwitchColorThemeButton pressed");
          ColorTheme colorTheme;
          if (new Random().nextBoolean()) {
            colorTheme = ColorTheme.ESO;
          } else {
            colorTheme = ColorTheme.BLUE;
          }
          MediaBrowserDisplay.getMediaBrowserDisplay().changeColorTheme(colorTheme);
        };
    return actionListeners;
  }
}
