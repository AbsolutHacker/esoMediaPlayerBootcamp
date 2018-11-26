package players.resources;

import de.eso.rxplayer.Audio;

public interface AudioSink {
    default boolean isAvailable() {
        switch (getState()) {
            case STOPPED:
                return true;
            case STARTING:
                return false;
            case STARTED:
                return false;
            case STOPPING:
                return false;
            default:
                throw new IllegalStateException("The speaker state is in an unknown state");
        }
    }
    default void free() {
        setState(Audio.AudioState.STOPPED);
    }
    Audio.Connection getConnection();
    void setConnection(Audio.Connection connection);
    Audio.AudioState getState();
    void setState(Audio.AudioState audioState);
}
