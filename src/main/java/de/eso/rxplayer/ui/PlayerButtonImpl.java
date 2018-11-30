package de.eso.rxplayer.ui;

import de.eso.rxplayer.vertx.client.Launcher;
import java.awt.event.ActionListener;
import java.util.Random;

class DisplayAlbumsButton extends PlayerButton {

  private String buttonText;

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
          Launcher.getClient()
              .subscribe(
                  client -> {
                    //                Observable<ApiResponse> apiResponse$ =
                    // client.newRequest("/browse/albums", Album.class);
                    //                apiResponse$.subscribe(apiResponse -> {
                    //
                    //                });
                  });
        };
    return actionListeners;
  }
}

class SearchAlbumButton extends PlayerButton {

  private String buttonText;

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
          Launcher.getClient()
              .subscribe(
                  client -> {
                    //                Observable<ApiResponse> apiResponse$ =
                    // client.newRequest("/browse/album", albumName);
                    //                apiResponse$.subscribe(apiResponse -> {
                    //
                    //                });
                  });
        };
    return actionListeners;
  }
}

class SearchTrackButton extends PlayerButton {

  private String buttonText;

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
          Launcher.getClient()
              .subscribe(
                  client -> {
                    //                Observable<ApiResponse> apiResponse$ =
                    // client.newRequest("/browse/track", trackName);
                    //                apiResponse$.subscribe(apiResponse -> {
                    //
                    //                });
                  });
        };
    return actionListeners;
  }
}

class AlbumTracksButton extends PlayerButton {

  private String buttonText;

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
          Launcher.getClient()
              .subscribe(
                  client -> {
                    //                Observable<ApiResponse> apiResponse$ =
                    // client.newRequest("/browse/albumTracks", albumName);
                    //                apiResponse$.subscribe(apiResponse -> {
                    //
                    //                });
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
          Launcher.getClient()
              .subscribe(
                  client -> {
                    //                Observable<ApiResponse> apiResponse$ =
                    // client.newRequest("/play/back");
                    //                apiResponse$.subscribe(apiResponse -> {
                    //
                    //                });
                  });
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
          Launcher.getClient()
              .subscribe(
                  client -> {
                    //                Observable<ApiResponse> apiResponse$ =
                    // client.newRequest("/play/stop");
                  });
        };
    return actionListeners;
  }
}

class PlayPauseButton extends PlayerButton {

  private boolean isPlayMode = true; // if button press equals to play or pause
  private String buttonText;

  @Override
  void changeLanguage(Language language) {
    switch (language) {
      case English:
        buttonText = "PLAY";
        break;
      case German:
        buttonText = "SPIELEN";
        break;
      default:
        buttonText = "PLAY";
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
          if (isPlayMode) {
            System.out.println("PlayButton pressed");
            isPlayMode = false;
            buttonText = "PLAY";
            Launcher.getClient()
                .subscribe(
                    client -> {
                      //                    Observable<ApiResponse> apiResponse$ =
                      // client.newRequest("/play/play");
                    });

          } else {
            System.out.println("PauseButton pressed");
            isPlayMode = true;
            buttonText = "PAUSE";
            Launcher.getClient()
                .subscribe(
                    client -> {
                      //                    Observable<ApiResponse> apiResponse$ =
                      // client.newRequest("/play/pause");
                    });
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
          Launcher.getClient()
              .subscribe(
                  client -> {
                    //                Observable<ApiResponse> apiResponse$ =
                    // client.newRequest("/play/forward");
                  });
        };
    return actionListeners;
  }
}

class SwitchLanguageButton extends PlayerButton {

  private String buttonText;

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
