package de.eso.rxplayer.ui;

public enum Language {
  English,
  German;

  @SuppressWarnings("SameReturnValue")
  public static Language getDefaultLanguage() {
    return English;
  }
}
