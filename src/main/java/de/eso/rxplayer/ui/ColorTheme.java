package de.eso.rxplayer.ui;

/**
 * Customizable color theme of the UI.
 * Can be changed by the user
 */
public enum ColorTheme {
    ESO(0xFF6501),
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF),
    LIGHT(0xE0E0E0),
    DARK(0x404040);

    private int hexCode;
    ColorTheme(int hexCode) {
        this.hexCode = hexCode;
    }

    public static ColorTheme getDefaultColorTheme() {
        return ESO;
    }

    public int getHexCode() {
        return hexCode;
    }
}
