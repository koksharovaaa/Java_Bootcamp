import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TestList {
    
    @Test
    void testCase1() {
        String str = "3\ndog\nSnowball\n12\ndog\nSnowball2\n10\ndog\nSnowball3\n9";
        String check = "Dog name = Snowball, age = 12\nDog name = Snowball2, age = 10\nDog name = Snowball3, age = 9";
        assertEquals(check, program(str));
    }

    @Test
    void testCase2() {
        String str = "3\ndog\nSnowball\n12\ncat\nKitty\n10\ndog\nBalloon\n9";
        String check = "Dog name = Snowball, age = 12\nCat name = Kitty, age = 10\nDog name = Balloon, age = 9";
        assertEquals(check, program(str));
    }

    @Test
    void testCase3() {
        String str = "3\nhamster\ncat\nKitty\n-10\ncat\nFura\n9";
        String check = "Incorrect input. Unsupported pet type\nIncorrect input. Age <= 0\nCat name = Fura, age = 9";
        assertEquals(check, program(str));
    }

    @Test
    void NotEnoughPets() {
        String str = "3\ndog\nSnowball\n12\ncat\nKitty\n10";
        String check = "Dog name = Snowball, age = 12\nCat name = Kitty, age = 10";
        assertEquals(check, program(str));
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
            PetList.main(new String[]{});
        } finally {
            System.setIn(stdin);
            System.setOut(stdout);
        }
        return out.toString().trim();
    }
}