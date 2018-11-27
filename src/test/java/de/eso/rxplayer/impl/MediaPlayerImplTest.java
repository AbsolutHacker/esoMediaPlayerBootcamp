package de.eso.rxplayer.impl;

import de.eso.rxplayer.Audio;
import de.eso.rxplayer.api.MediaPlayer;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class MediaPlayerImplTest {

    @BeforeEach
    void setUp() {
        Field instance = null;
        try {
            // ensure that ever test get a new singleton object
            instance = MediaPlayerImpl.class.getDeclaredField("mediaPlayer");
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getInstance() {
        Assert.assertSame(MediaPlayerImpl.getInstance(), MediaPlayerImpl.getInstance());
    }

    @Test
    void play() {
        Field playerField = null;
        try {
            playerField = MediaPlayerImpl.class.getDeclaredField("player");
            MediaPlayerImpl mp = MediaPlayerImpl.getInstance();
            mp.play();
            Assert.assertNull(playerField);
            mp.selectSource(Audio.Connection.USB);
            mp.play();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }



    }

    @Test
    void pause() {
    }

    @Test
    void stop() {
    }

    @Test
    void next() {
    }

    @Test
    void previous() {
    }

    @Test
    void selectSource() {
    }

    @Test
    void selectItem() {
    }

    @Test
    void getPlaytimeOffset() {
    }

    @Test
    void getCurrentTrack() {
    }
}