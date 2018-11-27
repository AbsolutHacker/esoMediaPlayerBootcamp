package de.eso.rxplayer.api;

import de.eso.rxplayer.Audio;
import de.eso.rxplayer.Track;


/**
 * Provides functionality of a cross source media player
 */
public interface MediaPlayer {
  /**
   * play a track from the beginning
   */
  void play();

  /**
   * pause a track
   */
  void pause();

  /**
   * pause the track and jump back to the starting point
   */
  void stop();

  /**
   * go to the next track and pause at the beginning
   */
  void next();

  /**
   * go to the previous track and pause at the beginning
   */
  void previous();

  /**
   * switch between available sources
   *
   * @param source audio source
   */
  void selectSource(Audio.Connection source);

  /**
   * selects an item (e.g. a given track, file, or station)
   *
   * @param itemId id of station or track that should be selected
   */
  void selectItem(int itemId);

  /**
   * get the current position within a track
   * within the station context this time is 0
   *
   * @return current offset
   */
  long getPlaytimeOffset();

  /**
   * get the current choosen or played track
   *
   * @return
   */
  Track getCurrentTrack();
}
