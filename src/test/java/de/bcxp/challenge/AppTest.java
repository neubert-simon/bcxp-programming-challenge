package de.bcxp.challenge;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Example JUnit 5 test case.
 */
class AppTest {

    private static PrintStream originalOut;

    @BeforeAll
    static void backupOriginalOutputStream() {
        originalOut = System.out;
    }

    @Test
    void testMainPrintsExpectedOutput() {

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        assertDoesNotThrow(() -> App.main());
        String output = outContent.toString();

        // CSV files were copied and altered to test for different results:
        assertTrue(output.contains("25"));
        assertTrue(output.contains("Austria"));

    }

    @AfterAll
    static void revertOutputStream() {
        System.setOut(originalOut);
    }

}