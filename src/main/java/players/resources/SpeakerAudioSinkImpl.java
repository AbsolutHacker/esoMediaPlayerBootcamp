package players.resources;

import de.eso.rxplayer.Audio;

/**
 * Singleton class representing the normal speakers of the MediaPlayer
 */
final class SpeakerAudioSinkImpl implements AudioSink {
    private static SpeakerAudioSinkImpl speakers;

    private Audio.AudioState audioState;
    private Audio.Connection connection;

    private SpeakerAudioSinkImpl() {
        this.connection = null;
        audioState = Audio.AudioState.STOPPED;
    }

    /**
     * Used to access the singleton
     * @return the singleton SpeakerAudioSinkImpl
     */
    static SpeakerAudioSinkImpl getInstance() {
        if (speakers == null) {
            speakers = new SpeakerAudioSinkImpl();
        }
        return speakers;
    }

    @Override
    public Audio.Connection getConnection() {
        return connection;
    }

    @Override
    public void setConnection(Audio.Connection connection) {
        this.connection = connection;
    }

    @Override
    public Audio.AudioState getState() {
        return audioState;
    }

    @Override
    public void setState(Audio.AudioState audioState) {
        this.audioState = audioState;
    }
}
