package de.bcxp.challenge.common.utility;

import org.junit.jupiter.api.Test;
import static de.bcxp.challenge.common.utility.StringParsingUtility.*;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

class StringParsingUtilityTest {

    //region getLongFromString Tests

    //region Positive tests
    @Test
    void testGetLongFromStringValidIntegerUSLocale() throws Exception {
        long result = getLongFromString("12345", Locale.US);
        assertEquals(12345L, result);
    }

    @Test
    void testGetLongFromStringValidIntegerWithGroupingUSLocale() throws Exception {
        long result = getLongFromString("1,234", Locale.US);
        assertEquals(1234L, result);
    }

    @Test
    void testGetLongFromStringValidIntegerGermanLocale() throws Exception {
        long result = getLongFromString("1.234", Locale.GERMANY);
        assertEquals(1234L, result);
    }

    @Test
    void testGetLongFromStringNegativeNumber() throws Exception {
        long result = getLongFromString("-987", Locale.US);
        assertEquals(-987L, result);

        result = getLongFromString("-98732421", Locale.US);
        assertEquals(-98732421L, result);
    }

    @Test
    void testGetLongFromStringLargeNumber() throws Exception {
        long result = getLongFromString(String.valueOf(Long.MAX_VALUE), Locale.US);
        assertEquals(Long.MAX_VALUE, result);

        result = getLongFromString(String.valueOf(Long.MAX_VALUE), Locale.GERMANY);
        assertEquals(Long.MAX_VALUE, result);
    }
    //endregion

    //region Negative tests
    @Test
    void testGetLongFromStringNumberLargerThanLongMax() {
        assertThrows(NumberFormatException.class, () ->
                getLongFromString(Long.MAX_VALUE + "123456789", Locale.US));
    }

    @Test
    void testGetLongFromStringInvalidNumber() {
        assertThrows(NumberFormatException.class, () ->
                getLongFromString("12abc", Locale.US));
    }

    @Test
    void testGetLongFromStringNullString() {
        assertThrows(IllegalArgumentException.class, () ->
                getLongFromString(null, Locale.US));
    }

    @Test
    void testGetLongFromStringEmptyString() {
        assertThrows(IllegalArgumentException.class, () ->
                getLongFromString("", Locale.US));
    }

    @Test
    void testGetLongFromStringBlankString() {
        assertThrows(IllegalArgumentException.class, () ->
                getLongFromString("   ", Locale.US));
    }
    //endregion

    //endregion

    //region getDoubleFromString Tests

    //region Positive tests
    @Test
    void testGetDoubleFromStringValidDoubleUSLocale() throws Exception {
        double result = getDoubleFromString("123.45", Locale.US);
        assertEquals(123.45, result, 0.000001);
    }

    @Test
    void testGetDoubleFromStringValidDoubleWithGroupingUSLocale() throws Exception {
        double result = getDoubleFromString("1,234.56", Locale.US);
        assertEquals(1234.56, result, 0.000001);
    }

    @Test
    void testGetDoubleFromStringValidDoubleGermanLocale() throws Exception {
        double result = getDoubleFromString("1.234,56", Locale.GERMANY);
        assertEquals(1234.56, result, 0.000001);
    }

    @Test
    void testGetDoubleFromStringNegativeNumber() throws Exception {
        double result = getDoubleFromString("-987.65", Locale.US);
        assertEquals(-987.65, result, 0.000001);
    }
    //endregion

    //region Negative tests
    @Test
    void testGetDoubleFromStringNumberLargerThanDoubleMax() {
        assertThrows(NumberFormatException.class, () ->
                getDoubleFromString(Double.MAX_VALUE + "9", Locale.US));
    }

    @Test
    void testGetDoubleFromStringNullString() {
        assertThrows(IllegalArgumentException.class, () ->
                getDoubleFromString(null, Locale.US));
    }

    @Test
    void testGetDoubleFromStringEmptyString() {
        assertThrows(IllegalArgumentException.class, () ->
                getDoubleFromString("", Locale.US));
    }

    @Test
    void testGetDoubleFromStringBlankString() {
        assertThrows(IllegalArgumentException.class, () ->
                getDoubleFromString("   ", Locale.US));
    }
    //endregion

    //endregion
}