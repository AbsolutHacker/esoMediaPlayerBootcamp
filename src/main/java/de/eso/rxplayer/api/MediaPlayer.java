package de.eso.rxplayer.api;

import de.eso.rxplayer.Audio;


public interface MediaPlayer {
  void play();
  void pause();
  void stop();
  void next();
  void previous();
  void selectSource(Audio.Connection source);
  // {USB,CD}Player playing
  void selectTrack(int track);

  long getPlaytimeOffset();
  int getCurrentTrack();
}

