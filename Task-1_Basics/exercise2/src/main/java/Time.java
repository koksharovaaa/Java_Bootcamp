import java.util.InputMismatchException;
import java.util.Scanner;

public class Time {
  static final int SS = 0;
  static final int MM = 1;
  static final int HH = 2;

  public static void main(String[] args) {
    try (Scanner s = new Scanner(System.in)) {
      int time = getInt(s);
      output(calc(time, HH), calc(time, MM), calc(time, SS));
    }
  }

  private static int getInt(Scanner s) {
    int seconds = -1;
    while (true) {
      try {
        seconds = s.nextInt();
        break;
      } catch (InputMismatchException e) {
        System.out.println("Could not parse a number. Please, try again");
        s.next();
      }
    }
    return seconds;
  }

  private static int calc(int seconds, int option) {
    int time = -1;
    if (seconds >= 0) {
      switch (option) {
        case SS -> time = seconds % 60;
        case MM -> time = (seconds % 3600) / 60;
        case HH -> time = seconds / 3600;
      }
    }
    return time;
  }

  private static void output(int hours, int mins, int seconds) {
    if (hours == -1 || mins == -1 || seconds == -1) {
      System.out.println("Incorrect time");
    } else {
      System.out.printf("%02d:%02d:%02d\n", hours, mins, seconds);
    }
  }
}
