package de.eso.rxplayer.impl;

import de.eso.rxplayer.Audio;
import de.eso.rxplayer.Player;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class MediaPlayerImplTest {

    /**
     * reset Singleton to initial state
     */
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
    void playInitialCall() {
        Field playerField = null;
        Field playerConnection = null;
        try {
            MediaPlayerImpl mp = MediaPlayerImpl.getInstance();

            playerField = MediaPlayerImpl.class.getDeclaredField("player");
            playerField.setAccessible(true);
            Player p = (Player) playerField.get(MediaPlayerImpl.getInstance());
            Assert.assertNull(p);

            mp.play();

            p = (Player) playerField.get(MediaPlayerImpl.getInstance());
            playerConnection = p.getClass().getDeclaredField("connection");
            playerConnection.setAccessible(true);

            Audio.Connection c = (Audio.Connection) playerConnection.get(p);
            Assert.assertEquals("CD", c.name());

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void playUSB() {
        Field playerField = null;
        Field playerConnection = null;
        try {
            MediaPlayerImpl mp = MediaPlayerImpl.getInstance();

            playerField = MediaPlayerImpl.class.getDeclaredField("player");
            playerField.setAccessible(true);

            mp.selectSource(Audio.Connection.USB);
            mp.play();

            Player p = (Player) playerField.get(MediaPlayerImpl.getInstance());
            playerConnection = p.getClass().getDeclaredField("connection");
            playerConnection.setAccessible(true);

            Audio.Connection c = (Audio.Connection) playerConnection.get(p);
            Assert.assertEquals("USB", c.name());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
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