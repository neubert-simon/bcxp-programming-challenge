package de.bcxp.challenge.model.csv;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CountryEntryTest {
    
    @Test
    void testValidCountryEntryCreation() {
        CountryEntry entry = new CountryEntry("Monday", 30_000, 150);
        assertEquals("Monday", entry.getCountry());
        assertEquals(30_000, entry.getPopulation());
        assertEquals(150, entry.getArea());

        entry = new CountryEntry("Monday", 309_000_312, 1_421);
        assertEquals("Monday", entry.getCountry());
        assertEquals(309_000_312, entry.getPopulation());
        assertEquals(1_421, entry.getArea());
    }

    @Test
    void testValidCountryEntryCreationWithExtremeValues() {
        CountryEntry entry = new CountryEntry("Monday", Long.MAX_VALUE, Double.MAX_VALUE);
        assertEquals("Monday", entry.getCountry());
        assertEquals(Long.MAX_VALUE, entry.getPopulation());
        assertEquals(Double.MAX_VALUE, entry.getArea());

        entry = new CountryEntry("Monday", 0, Double.MIN_VALUE);
        assertEquals("Monday", entry.getCountry());
        assertEquals(0, entry.getPopulation());
        assertEquals(Double.MIN_VALUE, entry.getArea());
    }

    @Test
    void testExceptionWithInvalidParameters() {
        final String populationExceptionMsg = "Population must be greater or equal to 0.";
        final String areaExceptionMsg = "Area must be greater than 0.";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new CountryEntry("Tuesday", -10, 420));
        assertEquals(populationExceptionMsg, exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class,
                () -> new CountryEntry("Tuesday", 10, 0));
        assertEquals(areaExceptionMsg, exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class,
                () -> new CountryEntry("Tuesday", 10, -29103));
        assertEquals(areaExceptionMsg, exception.getMessage());
    }

    @Test
    void testGetBestMatchScore() {
        CountryEntry entry = new CountryEntry("Thursday", 40, 2);
        assertEquals(40.0 / 2.0, entry.getBestMatchScore());

        entry = new CountryEntry("Thursday", 1_300_1213, 142_421.);
        assertEquals(1_300_1213 / 142_421., entry.getBestMatchScore());
    }

}
