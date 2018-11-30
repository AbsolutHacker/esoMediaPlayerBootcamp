package de.eso.rxplayer.ui;

import de.eso.rxplayer.vertx.client.Launcher;

import java.awt.event.ActionListener;


class BackButton extends PlayerButton {

    @Override
    void changeLanguage(Language language) {
        //This button does not change with different languages
    }

    @Override
    public String getText() {
        return "<<";
    }

    @Override
    public ActionListener[] getActionListeners() {
        ActionListener[] actionListeners = new ActionListener[1];
        actionListeners[0] = e -> {
            System.out.println("BackButton pressed");
            Launcher.getClient().subscribe(client -> {
//                Observable<ApiResponse> apiResponse$ = client.newRequest("/play/back");
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
    }

    @Override
    public String getText() {
        return  buttonText;
    }

    @Override
    public ActionListener[] getActionListeners() {
        ActionListener[] actionListeners = new ActionListener[1];
        actionListeners[0] = e -> {
            System.out.println("StopButton pressed");
            Launcher.getClient().subscribe(client -> {
//                Observable<ApiResponse> apiResponse$ = client.newRequest("/play/stop");
            });
        };
        return actionListeners;
    }
}


class PlayPauseButton extends PlayerButton {

    private boolean isPlayMode = true; //if button press equals to play or pause
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
    }

    @Override
    public String getText() {
        return buttonText;
    }

    @Override
    public ActionListener[] getActionListeners() {
        ActionListener[] actionListeners = new ActionListener[1];
        actionListeners[0] = e -> {

            if (isPlayMode) {
                System.out.println("PlayButton pressed");
                isPlayMode = false;
                buttonText = "PLAY";
                Launcher.getClient().subscribe(client -> {
//                    Observable<ApiResponse> apiResponse$ = client.newRequest("/play/play");
                });

            } else {
                System.out.println("PauseButton pressed");
                isPlayMode = true;
                buttonText = "PAUSE";
                Launcher.getClient().subscribe(client -> {
//                    Observable<ApiResponse> apiResponse$ = client.newRequest("/play/pause");
                });
            }
        };
        return actionListeners;
    }
}


class ForwardButton extends PlayerButton {

    @Override
    void changeLanguage(Language language) {
        //this button does not change with different languages
    }

    @Override
    public String getText() {
        return ">>";
    }

    @Override
    public ActionListener[] getActionListeners() {
        ActionListener[] actionListeners = new ActionListener[1];
        actionListeners[0] = e -> {
            System.out.println("ForwardButton pressed");
            Launcher.getClient().subscribe(client -> {
//                Observable<ApiResponse> apiResponse$ = client.newRequest("/play/forward");
            });
        };
        return actionListeners;
    }
}
