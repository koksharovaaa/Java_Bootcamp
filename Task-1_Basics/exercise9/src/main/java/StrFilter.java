import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class StrFilter {
  public static void main(String args[]) {
    ArrayList<String> list;
    try (Scanner s = new Scanner(System.in)) {
      int n = s.nextInt();
      s.nextLine();
      list = new ArrayList<>();
      for (int i = 0; i < n; i++) {
        String str = s.nextLine();
        list.add(str);
      }
      String substr = s.nextLine();
      ArrayList<String> filtered = filterList(list, substr);
      System.out.println(filtered);
    } catch (InputMismatchException e) {
      System.out.println("Input error");
    } catch (NoSuchElementException e) {
      System.out.println("Empty input");
    }
  }

  public static ArrayList<String> filterList(ArrayList<String> list, String substr) {
    ArrayList<String> filtered = new ArrayList<>();
    if (!substr.equals("")) {
      for (String str : list) {
        if (str.contains(substr)) {
          filtered.add(str);
        }
      }
    }
    return filtered;
  }
}
