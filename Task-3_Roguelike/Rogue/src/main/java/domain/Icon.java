package domain;

import com.googlecode.lanterna.TextColor;

public class Icon {
  public final char glyph;
  public final TextColor colour;

  public Icon(Icon icon) {
    this.glyph = icon.glyph;
    this.colour = icon.colour;
  }

  public Icon(char glyph, TextColor colour) {
    this.glyph = glyph;
    this.colour = colour;
  }
}
