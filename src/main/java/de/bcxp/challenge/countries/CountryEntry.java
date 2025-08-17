package de.bcxp.challenge.countries;

import de.bcxp.challenge.common.model.DocumentEntry;
import de.bcxp.challenge.common.model.csv.IEntryWithComparableNumericTuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a data entry for a specific country.
 * <p>
 * This class extends {@link DocumentEntry} and adds population and area-specific information.
 * </p>
 *
 * @see DocumentEntry
 */
public class CountryEntry extends DocumentEntry implements IEntryWithComparableNumericTuple {
    private static final Logger logger = LogManager.getLogger(CountryEntry.class);

    private final long population;
    private final double area;

    /**
     * Constructs a {@link CountryEntry} object.
     *
     * @param country    the name or identifier of the country
     * @param population the population count of the country (must be zero or positive)
     * @param area       the total area of the country in square units (must be greater than zero)
     *
     * @throws IllegalArgumentException if {@code population} is negative, or if {@code area} is less than or equal to zero
     */
    public CountryEntry(final String country, final long population, final double area) {
        super(country);
        if (population < 0) {
            logger.warn("Population parameter ({}) less than 0 for {}", population, country);
            throw new IllegalArgumentException("Population must be greater or equal to 0.");
        }
        if (area <= 0) {
            logger.warn("Area parameter ({}) equal to or less than 0 for {}", area, country);
            throw new IllegalArgumentException("Area must be greater than 0.");
        }
        this.population = population;
        this.area = area;
        logger.trace("Created new country entry with {}, {}, {}", country, population, area);
    }

    /**
     * Calculates and returns the population density for this country instance.
     *
     * @return the population density as a {@code double}
     *
     * @throws IllegalStateException if the area is zero, indicating an invalid object state
     */

    @Override
    public double getBestMatchScore() {
        if (area <= 0) {
            logger.error("Invalid object created:\nFields: Country: {}, Population: {}, Area: {}.\nArea is less than or equal to 0.", this.getCountry(), this.population, this.area);
            throw new IllegalStateException("This object is invalid. Area can't be zero.");
        }

        final double result = population / area;
        if(!Double.isFinite(result)) {
            logger.warn("Score calculation for country: {} failed with population: {} and area: {}", getCountry(), population, area);
            throw new ArithmeticException("Calculating score failed.");
        }
        return result;
    }

    //region Getter
    public String getCountry() {
        return this.getId();
    }

    public double getArea() {
        return area;
    }

    public long getPopulation() {
        return population;
    }
    //endregion

    //region java.lang.Object Overrides
    @Override
    public String toString() {
        return "CountryEntry: " +
                "population: " + population +
                ", area:" + area;
    }
    //endregion
}
