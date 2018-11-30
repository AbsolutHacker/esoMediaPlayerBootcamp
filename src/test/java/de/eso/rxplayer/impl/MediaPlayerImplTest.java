package de.eso.rxplayer.impl;

import static org.junit.jupiter.api.Assertions.*;

import de.eso.rxplayer.Audio;
import de.eso.rxplayer.EntertainmentService;
import de.eso.rxplayer.Player;
import java.lang.reflect.Field;

import de.eso.rxplayer.myEntertainmentService;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MediaPlayerImplTest {

  /** reset Singleton to initial state */
  @BeforeEach
  void setUp() throws InterruptedException {
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

  /**
   * test if the the singleton pattern works as expected
   */
  @Test
  void getInstance() {
    Assert.assertSame(MediaPlayerImpl.getInstance(), MediaPlayerImpl.getInstance());
  }

  /**
   * checks if the initial call of the play button loads a default source
   * and play the first track
   */
  @Test
  void playInitialCall() throws InterruptedException {
    Field playerField = null;
    Field playerConnection = null;
    try {
      MediaPlayerImpl mp = MediaPlayerImpl.getInstance();

      playerField = MediaPlayerImpl.class.getDeclaredField("player");
      playerField.setAccessible(true);
      Player p = (Player) playerField.get(MediaPlayerImpl.getInstance());
      Assert.assertNull(p);

      mp.play();
      Thread.sleep(1000);

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

  /**
   * Check if play function play the after a source would selected and loaded
   *
   * @throws InterruptedException from thread sleep
   */
  @Test
  void playAfterSelectSource() throws InterruptedException {
    Field playerField = null;
    try {
      MediaPlayerImpl mp = MediaPlayerImpl.getInstance();
      mp.selectSource(Audio.Connection.CD);

      playerField = MediaPlayerImpl.class.getDeclaredField("player");
      playerField.setAccessible(true);
      Player p = (Player) playerField.get(MediaPlayerImpl.getInstance());


      mp.play();

      p.isPlaying().test().assertValue(Boolean.FALSE);

      Thread.sleep(1500);
      mp.play();
      Thread.sleep(1500);

      p.isPlaying().test().assertValue(Boolean.TRUE);

    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  /**
   * test the pause function after a source would selected and a track starts to play
   *
   * @throws InterruptedException from Thread sleep
   */
  @Test
  void pause() throws InterruptedException {
    Field playerField = null;
    Field playerConnection = null;
    try {
      MediaPlayerImpl mp = MediaPlayerImpl.getInstance();

      playerField = MediaPlayerImpl.class.getDeclaredField("player");
      playerField.setAccessible(true);

      mp.selectSource(Audio.Connection.USB);
      mp.play();
      Thread.sleep(1000);
      mp.pause();

      Player p = (Player) playerField.get(MediaPlayerImpl.getInstance());
      p.isPlaying().test()
              .assertValue(Boolean.FALSE);

      Thread.sleep(1000);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

  }

  @Test
  void stop() throws InterruptedException {
    Field playerField = null;
    Field stateField = null;

    try {
      MediaPlayerImpl mp = MediaPlayerImpl.getInstance();
      mp.selectSource(Audio.Connection.USB);

      playerField = MediaPlayerImpl.class.getDeclaredField("player");
      playerField.setAccessible(true);
      stateField = MediaPlayerImpl.class.getDeclaredField("currentState");
      stateField.setAccessible(true);

      mp.play();
      Thread.sleep(1000);
      mp.pause();
      Thread.sleep(1000);
      mp.stop();

      Player p = (Player) playerField.get(MediaPlayerImpl.getInstance());

      p.isPlaying().test()
              .assertValue(Boolean.FALSE);

      Thread.sleep(1500);

      Assert.assertEquals(Audio.AudioState.STOPPED, stateField.get(MediaPlayerImpl.getInstance()));

    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Test
  void next() throws InterruptedException {
    Field playerField = null;
    Field stateField = null;
    Field trackField = null;

    try {
      MediaPlayerImpl mp = MediaPlayerImpl.getInstance();
      mp.selectSource(Audio.Connection.USB);

      playerField = MediaPlayerImpl.class.getDeclaredField("player");
      playerField.setAccessible(true);
      stateField = MediaPlayerImpl.class.getDeclaredField("currentState");
      stateField.setAccessible(true);
      trackField = MediaPlayerImpl.class.getDeclaredField("currentTrackId");
      trackField.setAccessible(true);

      mp.play();
      mp.pause();
      mp.next();

      Player p = (Player) playerField.get(MediaPlayerImpl.getInstance());

      p.isPlaying().test()
              .assertValue(Boolean.FALSE);
      Thread.sleep(1500);

      Assert.assertEquals(Audio.AudioState.STARTED, stateField.get(MediaPlayerImpl.getInstance()));
      Thread.sleep(1500);
      Assert.assertEquals(new Integer(1), trackField.get(MediaPlayerImpl.getInstance()));

    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }
//
//  @Test
//  void previous() {}
//
//  @Test
//  void selectSource() {}
//
//  @Test
//  void selectItem() {}
//
//  @Test
//  void getPlaytimeOffset() {}
//
//  @Test
//  void getCurrentTrack() {}

  @AfterEach
  void resetPlayer() throws InterruptedException, NoSuchFieldException, IllegalAccessException {

    MediaPlayerImpl mp = MediaPlayerImpl.getInstance();

    mp.stop();

    Thread.sleep(2000);
  }
}
