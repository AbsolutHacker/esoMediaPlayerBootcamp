package audio;

import de.eso.rxplayer.Audio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SpeakerAudioSinkImplTest {


    @Test
    void connectionDefaultFails() {
        AudioSink speakers = SpeakerAudioSinkImpl.getInstance();
        assertEquals(null, speakers.getConnection());
    }


    @Test
    void setConnection() {
        AudioSink speakers = SpeakerAudioSinkImpl.getInstance();
        speakers.setConnection(Audio.Connection.CD);
        assertEquals(Audio.Connection.CD, speakers.getConnection());
    }

    @Test
    void setState() {
        AudioSink speakers = SpeakerAudioSinkImpl.getInstance();
        speakers.setState(Audio.AudioState.STOPPED);

        assertEquals(Audio.AudioState.STOPPED, speakers.getState());

        speakers.setState(Audio.AudioState.STARTING);
        assertEquals(Audio.AudioState.STARTING, speakers.getState());
    }

    @Test
    void setMoreState() {
        AudioSink speakers = mock(AudioSink.class);

        speakers.setState(Audio.AudioState.STARTING);
        speakers.setState(Audio.AudioState.STARTED);

        verify(speakers).setState(Audio.AudioState.STARTING);
        verify(speakers).setState(Audio.AudioState.STARTED);
    }
}