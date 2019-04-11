package seedu.address.testutil.epiggy;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.epiggy.Expense;
import seedu.address.model.epiggy.item.Cost;
import seedu.address.model.epiggy.item.Item;
import seedu.address.model.epiggy.item.Name;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Expense objects.
 */
public class ExpensesBuilder {

    public static final String DEFAULT_NAME = "Chicken Rice";
    public static final String DEFAULT_COST = "5.0";
    public static final String DEFAULT_DATE = "03/01/2019";
    //public static final Set<Tag> DEFAULT_TAGS = new HashSet<>(Arrays.asList("Food", "Lunch"));

    private Name name;
    private Cost cost;
    private Date date;
    private Set<Tag> tags;

    public ExpensesBuilder() {
        name = new Name(DEFAULT_NAME);
        cost = new Cost(DEFAULT_COST);
        date = new Date(DEFAULT_DATE);
        tags = new HashSet<>();
    }

    /**
     * Initializes the ExpensesBuilder with the data of {@code expenseToCopy}.
     */
    public ExpensesBuilder(Expense expenseToCopy) {
        name = expenseToCopy.getItem().getName();
        cost = expenseToCopy.getItem().getCost();
        date = expenseToCopy.getDate();
        tags = new HashSet<>(expenseToCopy.getItem().getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Expense} that we are building.
     */
    public ExpensesBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Expense} that we are building.
     */
    public ExpensesBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code cost} of the {@code Expense} that we are building.
     */
    public ExpensesBuilder withCost(String cost) {
        this.cost = new Cost(cost);
        return this;
    }

    /**
     * Sets the {@code date} of the {@code Expense} that we are building.
     */
    public ExpensesBuilder withDate(String date) {
        if (date.equals("")) {
            this.date = new Date();
        }
        try {
            this.date = ParserUtil.parseDate(date);
        } catch (ParseException e) {
            this.date = null;
        }
        return this;
    }

    /**
     * Builds an {@code Expense} object with name, date, cost and tags
     * @return Expense object
     */
    public Expense build() {
        Item item = new Item(name, cost, tags);
        return new Expense(item, date);
    }

}
