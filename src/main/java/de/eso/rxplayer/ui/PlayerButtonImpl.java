package de.eso.rxplayer.ui;

import java.awt.event.ActionListener;

class BackButton extends PlayerButton {

    @Override
    public String getText() {
        return "<<";
    }

    @Override
    public ActionListener[] getActionListeners() {
        ActionListener[] actionListeners = new ActionListener[1];
        actionListeners[0] = e -> {
            System.out.println("BackButton pressed");
//            ApiRequest apiRequest = new ApiRequest();
//            getClient.sendapirequest
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
        actionListeners[0] = e -> System.out.println("StopButton pressed");
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
        actionListeners[0] = e -> System.out.println("PlayButton pressed");
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
        actionListeners[0] = e -> System.out.println("PauseButton pressed");
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
        actionListeners[0] = e -> System.out.println("ForwardButton pressed");
        return actionListeners;
    }
}
