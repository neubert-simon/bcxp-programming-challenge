package model.csv;

import model.DocumentEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a weather data entry for a specific day, containing the maximum and minimum temperatures.
 * <p>
 * This class extends {@link DocumentEntry} and provides additional temperature-specific information
 * such as the maximum temperature, minimum temperature, and temperature spread.
 * </p>
 *
 * @see DocumentEntry
 */
public class WeatherEntry extends DocumentEntry {
    private static final Logger logger = LogManager.getLogger(WeatherEntry.class);

    private final double maxTemp;
    private final double minTemp;

    /**
     * Constructs a {@link WeatherEntry} object for a specific day with the given maximum and minimum temperatures.
     *
     * @param day      the name or identifier of the day
     * @param maxTemp  the maximum temperature for the day
     * @param minTemp  the minimum temperature for the day
     *
     * @throws IllegalArgumentException if {@param maxTemp} is less than {@param minTemp}
     */
    public WeatherEntry(final String day, final double maxTemp, final double minTemp) {
        super(day);
        if (maxTemp < minTemp) {
            logger.warn("MaxTemp {} lower than MinTemp {} for day {}", maxTemp, minTemp, day);
            throw new IllegalArgumentException("Maximum temperature can't be less than minimum temperature.");
        }
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        logger.trace("Created new country entry with {}, {}, {}", day, maxTemp, minTemp);
    }

    public String getDay() {
        return this.getId();
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    /**
     * Retrieves the numeric difference between this day's maximum and minimum temperature.
     * @return Temperature spread in double format.
     */
    public double getTemperatureSpread() {
        return maxTemp - minTemp;
    }
    
    //region java.lang.Object Overrides
    @Override
    public String toString() {
        return "WeatherEntry: " +
                "maxTemp: " + maxTemp +
                ", minTemp: " + minTemp;
    }
    //endregion
}