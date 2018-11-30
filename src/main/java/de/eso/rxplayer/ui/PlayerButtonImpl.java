package de.eso.rxplayer.ui;

import de.eso.rxplayer.api.ApiResponse;
import de.eso.rxplayer.vertx.client.Launcher;
import io.reactivex.Observable;
import java.awt.event.ActionListener;

class BackButton extends PlayerButton {

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
                    Observable<ApiResponse> apiResponse$ = client.newRequest("/play/back",
                        Void.class);
                  });
        };
    return actionListeners;
  }
}

class StopButton extends PlayerButton {

  @Override
  public String getText() {
    return "STOP";
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
                    Observable<ApiResponse> apiResponse$ = client.newRequest("/play/stop", Void.class);
                  });
        };
    return actionListeners;
  }
}

class PlayButton extends PlayerButton {

  @Override
  public String getText() {
    return "PLAY";
  }

  @Override
  public ActionListener[] getActionListeners() {
    ActionListener[] actionListeners = new ActionListener[1];
    actionListeners[0] =
        e -> {
          System.out.println("PlayButton pressed");
          Launcher.getClient()
              .subscribe(
                  client -> {
                    Observable<ApiResponse> apiResponse$ = client.newRequest("/play/play", Void.class);
                  });
        };
    return actionListeners;
  }
}

class PauseButton extends PlayerButton {

  @Override
  public String getText() {
    return "PAUSE";
  }

  @Override
  public ActionListener[] getActionListeners() {
    ActionListener[] actionListeners = new ActionListener[1];
    actionListeners[0] =
        e -> {
          System.out.println("PauseButton pressed");
          Launcher.getClient()
              .subscribe(
                  client -> {
                    Observable<ApiResponse> apiResponse$ = client.newRequest("/play/pause", Void.class);
                  });
        };
    return actionListeners;
  }
}

class ForwardButton extends PlayerButton {

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
                    Observable<ApiResponse> apiResponse$ = client.newRequest("/play/forward", Void.class);
                  });
        };
    return actionListeners;
  }
}
