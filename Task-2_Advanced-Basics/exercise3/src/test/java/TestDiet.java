import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TestDiet {
    
    @Test
    void testCase1() {
        String str = "4\ndog\nSnowball\n12\nguinea\nPiggy\n5\ncat\nSnowball\n9\nhamster\nWave\n2";
        String check = "GuineaPig name = Piggy, age = 5. I can chill for 12 hours\nHamster name = Wave, age = 2. I can chill for 8 hours\nDog name = Snowball, age = 12. I can hunt for robbers\nCat name = Snowball, age = 9. I can hunt for mice";
        assertEquals(check, program(str));
    }

    @Test
    void testCase2() {
        String str = "2\ndog\nSnowball\n12\ncat\nKitty\n10";
        String check = "Dog name = Snowball, age = 12. I can hunt for robbers\nCat name = Kitty, age = 10. I can hunt for mice";
        assertEquals(check, program(str));
    }

    @Test
    void testCase3() {
        String str = "3\nturtle\ncat\nKitty\n-10\nguinea\nPiggy\n3";
        String check = "Incorrect input. Unsupported pet type\nIncorrect input. Age <= 0\nGuineaPig name = Piggy, age = 3. I can chill for 12 hours";
        assertEquals(check, program(str));
    }

    @Test
    void NotEnoughPets() {
        String str = "3\ndog\nSnowball\n12\ncat\nKitty\n10";
        String check = "Dog name = Snowball, age = 12. I can hunt for robbers\nCat name = Kitty, age = 10. I can hunt for mice";
        assertEquals(check, program(str));
    }

    @Test
    void ZeroSize() {
        String str = "0\ndog\nSnowball\n12\ncat\nKitty\n10";
        String check = "Input error. Size <= 0";
        assertEquals(check, program(str));
    }

    @Test
    void NegSize() {
        String str = "-1\ndog\nSnowball\n12\ncat\nKitty\n10";
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
            PetDiet.main(new String[]{});
        } finally {
            System.setIn(stdin);
            System.setOut(stdout);
        }
        return out.toString().trim();
    }
}