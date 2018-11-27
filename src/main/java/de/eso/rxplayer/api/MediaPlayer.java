package de.eso.rxplayer.api;

import de.eso.rxplayer.Audio;


public interface MediaPlayer {
  void play();
  void pause();
  void stop();
  void next();
  void previous();
  void selectSource(Audio.Connection source);
  // selects an item (e.g. a given track, file, or station)
  // depending on the active source
  void selectItem(int index);

  long getPlaytimeOffset();
  int getCurrentTrack();
}
