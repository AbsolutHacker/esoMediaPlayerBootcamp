package players.resources;

import de.eso.rxplayer.Audio;

/**
 * Interface for a generic Audio-Output
 */
public interface AudioSink {
    /**
     * comfort default functionality to see if the player currently is available (not playing anything)
     * @return true if AudioState.Stopped, false otherwise
     */
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

    /**
     * sets the AudioState to Stopped
     */
    default void free() {
        setState(Audio.AudioState.STOPPED);
    }


    Audio.Connection getConnection();

    void setConnection(Audio.Connection connection);

    Audio.AudioState getState();

    void setState(Audio.AudioState audioState);
}
