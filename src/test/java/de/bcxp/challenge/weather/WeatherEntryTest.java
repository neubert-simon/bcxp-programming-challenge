package de.bcxp.challenge.weather;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WeatherEntryTest {

    @Test
    void testValidWeatherEntryCreation() {
        WeatherEntry entry = new WeatherEntry("Monday", 30.5, 15.2);
        assertEquals("Monday", entry.getDay());
        assertEquals(30.5, entry.getMaxTemp());
        assertEquals(15.2, entry.getMinTemp());

        entry = new WeatherEntry("9", 42.4, -12.9);
        assertEquals("9", entry.getDay());
        assertEquals(42.4, entry.getMaxTemp());
        assertEquals(-12.9, entry.getMinTemp());
    }

    @Test
    void testExceptionWhenMaxTempLessThanMinTemp() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new WeatherEntry("Tuesday", 10.0, 20.0));
        assertEquals("Maximum temperature can't be less than minimum temperature.", exception.getMessage());
    }

    @Test
    void testGetBestMatchScore() {
        final WeatherEntry entry = new WeatherEntry("Thursday", 40.0, 25.5);
        assertEquals(40.0 - 25.5, entry.getBestMatchScore());
    }

    @Test
    void testGetBestMatchScoreExtremeValues() {
        WeatherEntry entry = new WeatherEntry("Thursday", Double.MAX_VALUE, 0);
        assertEquals(Double.MAX_VALUE, entry.getBestMatchScore());

        entry = new WeatherEntry("Thursday", Double.MAX_VALUE, -Double.MAX_VALUE);
        assertThrows(ArithmeticException.class, entry::getBestMatchScore);
    }

}