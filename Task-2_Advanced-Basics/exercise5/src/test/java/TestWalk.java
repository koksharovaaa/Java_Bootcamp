import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TestWalk {

    @Test
    void testCase1() {
        String str = "3\ndog\nSnowball\n12\ndog\nSnowball2\n8\ndog\nSnowball3\n10";
        String result = program(str);
        assertTrue(result.contains("Dog name = Snowball2, age = 8, start time = 0."));
        assertTrue(result.contains("Dog name = Snowball3, age = 10, start time = 0."));
        assertTrue(result.contains("Dog name = Snowball, age = 12, start time = 0."));
        assertTrue(result.contains(", end time ="));
    }

    @Test
    void testCase2() {
        String str = "3\ndog\nSnowball\n8\ncat\nKitty\n9\ndog\nBalloon\n9";
        String result = program(str);
        assertTrue(result.contains("Cat name = Kitty, age = 9, start time = 0."));
        assertTrue(result.contains("Dog name = Snowball, age = 8, start time = 0."));
        assertTrue(result.contains("Dog name = Balloon, age = 9, start time = 0."));
        assertTrue(result.contains(", end time ="));
    }

    @Test
    void testCase3() {
        String str = "4\nhamster\ncat\nKitty\n-10\ndog\nBalloon\n11\ncat\nFura\n9";
        String result = program(str);
        assertTrue(result.contains("Incorrect input. Unsupported pet type"));
        assertTrue(result.contains("Incorrect input. Age <= 0"));
        assertTrue(result.contains("Cat name = Fura, age = 9, start time = 0."));
        assertTrue(result.contains("Dog name = Balloon, age = 11, start time = 0."));
        assertTrue(result.contains(", end time ="));
    }

    @Test
    void NotEnoughPets() {
        String str = "3\ndog\nSnowball\n12\ncat\nKitty\n10";
        String result = program(str);
        assertTrue(result.contains("Dog name = Snowball, age = 12, start time = 0."));
        assertTrue(result.contains("Cat name = Kitty, age = 10, start time = 0."));
        assertTrue(result.contains(", end time ="));
    }

    @Test
    void ZeroSize() {
        String str = "0\ncat\nKitty\n10\ncat\nFura\n9";
        String check = "Input error. Size <= 0";
        assertEquals(check, program(str));
    }

    @Test
    void NegSize() {
        String str = "-1\ncat\nKitty\n10\ncat\nFura\n9";
        String check = "Input error. Size <= 0";
        assertEquals(check, program(str));
    }

    @Test
    void EmptyStr() {
        String str = "";
        String check = "Input error. Size <= 0";
        assertEquals(check, program(str));
    }

    public String program (String str) {
        java.io.InputStream stdin = System.in;
        PrintStream stdout = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(str.getBytes()));
        System.setOut(new PrintStream(out));
        try {
            PetWalk.main(new String[]{});
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
        } finally {
            System.setIn(stdin);
            System.setOut(stdout);
        }
        return out.toString().trim();
    }
}