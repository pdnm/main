package seedu.address.model.epiggy;

import java.util.Date;

import seedu.address.model.epiggy.item.Price;

/**
 * Represents a Budget in the expense book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Budget {
    private final Price amount;
    private final Date startDate;
    private final int period;

    public Budget(Price amount, Date startDate, int period) {
        this.amount = amount;
        this.startDate = startDate;
        this.period = period;
    }
}
