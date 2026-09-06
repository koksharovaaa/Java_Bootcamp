package presentation;

public class Line {
  public static final String START = "NEW GAME 'S'";
  public static final String LOAD = "RESUME GAME 'C'";
  public static final String SCOREBOARD = "LEADERBOARD 'L'";
  public static final String SCORES = "LEADERBOARD";
  public static final String SCORESEMPTY = "No finished attempts!";
  public static final String SCORESBACK = "Press 'L' to return to menu";
  public static final String SCOREHEADER =
      "LEVEL  GOLD  RESULT  STEPS  TREASURES  FOOD  ELIXIRS  SCROLLS  HITS: LANDED  FAILED  MISSED";
  public static final String GAMEPAUSED = "THE GAME IS PAUSED";
  public static final String CTRL = "CONTROLS:";
  public static final String PAUSE = "Press 'P' to resume";
  public static final String RESTART = "Press 'R' to restart";
  public static final String QUIT = "Press 'Q' to quit";
  public static final String OVER = "Game over!";
  public static final String LOSS = "YOU LOST...";
  public static final String WIN = "YOU WON!";

  public static final String FOOD = "Backpack: FOOD";
  public static final String ELIXIR = "Backpack: ELIXIRS";
  public static final String SCROLL = "Backpack: SCROLLS";
  public static final String WEAPON = "Backpack: WEAPONS";
  public static final String EMPTY = "No items!";
  public static final String USEITEM = "Press 1-9 to use an item";
  public static final String REMOVE = "Press 0 to discard the current weapon";
  public static final String COMBAT = "< COMBAT LOG >";

  public static final String PLAYER = "PLAYER:";
  public static final String ENEMY = "ENEMY:";

  public static final String[] ROGUE = {
    "                                                         .----.",
    "                                                        /\\_/   \\",
    "                                                        |  |  /|",
    "  .----------------------------------------------------------' |",
    " /  .-.          _____   ____   _____ _    _ ______            |",
    "|  /   \\        |  __ \\ / __ \\ / ____| |  | |  ____|           |",
    "| |\\_.  |       | |__) | |  | | |  __| |  | | |__              |",
    "|\\|  | /|       |  _  /| |  | | | |_ | |  | |  __|             |",
    "| `---' |       | | \\ \\| |__| | |__| | |__| | |____            |",
    "|       |       |_|  \\_\\\\____/ \\_____|\\____/|______|           |",
    "|       |                                                      /",
    "|       |-----------------------------------------------------'",
    "\\       /",
    " `----'"
  };

  public static final String[] LOSSICON = {
    "                 /()",
    "                / /",
    "               / /",
    "  /============| |------------------------------------------,",
    "{=| / / / / / /|()}     }     }     }                        }>",
    "  \\============| |------------------------------------------'",
    "               \\ \\",
    "                \\ \\",
    "                 \\()"
  };

  public static final String WINICON[] = {
    "         ______________",
    "        /\\______;;_____\\",
    "       | /             /",
    "       `. ())oo()^.-O*'",
    "        |\\(%()*^^()O))o%\\",
    "       %| |-%-----------|",
    " (O   % \\ | %    ))     |  *q",
    " *()  %  \\|%____________|()) (O",
    "       %%%%           o()"
  };
}
