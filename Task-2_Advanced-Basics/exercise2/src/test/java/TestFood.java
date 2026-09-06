import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TestFood {
    
    @Test
    void testCase1() {
        String str = "3\ndog\nSnowball\n12\n5.0\ndog\nSnowball2\n10\n10.0\ndog\nSnowball3\n9\n9.0";
        String check = "Dog name = Snowball, age = 12, mass = 5.00, feed = 1.50\nDog name = Snowball2, age = 10, mass = 10.00, feed = 3.00\nDog name = Snowball3, age = 9, mass = 9.00, feed = 2.70";
        assertEquals(check, program(str));
    }

// unfinished
    @Test
    void testCase2() {
        String str = "3\ndog\nSnowball\n12\n5.0\ncat\nKitty\n10\n10.0\ndog\nBalloon\n9\n9.0";
        String check = "Dog name = Snowball, age = 12, mass = 5.00, feed = 1.50\nCat name = Kitty, age = 10, mass = 10.00, feed = 1.00\nDog name = Balloon, age = 9, mass = 9.00, feed = 2.70";
        assertEquals(check, program(str));
    }

    @Test
    void testCase3() {
        String str = "4\nhamster\ncat\nKitty\n-10\ndog\nBalloon\n9\n-9\ncat\nFura\n9\n12.5";
        String check = "Incorrect input. Unsupported pet type\nIncorrect input. Age <= 0\nIncorrect input. Mass <= 0\nCat name = Fura, age = 9, mass = 12.50, feed = 1.25";
        assertEquals(check, program(str));
    }

    @Test
    void NotEnoughPets() {
        String str = "3\ndog\nSnowball\n12\n5.0\ncat\nKitty\n10\n10.0";
        String check = "Dog name = Snowball, age = 12, mass = 5.00, feed = 1.50\nCat name = Kitty, age = 10, mass = 10.00, feed = 1.00";
        assertEquals(check, program(str));
    }

    @Test
    void testCaseZeroSize() {
        String str = "0\ndog\nSnowball\n12\n5.0";
        String check = "Input error. Size <= 0";
        assertEquals(check, program(str));
    }

    @Test
    void testCaseNegSize() {
        String str = "-1\ndog\nSnowball\n12\n5.0";
        String check = "Input error. Size <= 0";
        assertEquals(check, program(str));
    }

    @Test
    void testCaseEmptyStr() {
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
            PetFood.main(new String[]{});
        } finally {
            System.setIn(stdin);
            System.setOut(stdout);
        }
        return out.toString().trim();
    }
}