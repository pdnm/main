package seedu.address.testutil.epiggy;

import static seedu.address.logic.commands.CommandTestUtil.VALID_COST_BOWLING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COST_IPHONE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_2018;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_2019;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOWLING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_IPHONE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_EXPENSE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FOOD;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIENDS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.EPiggy;
import seedu.address.model.epiggy.Expense;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalExpenses {

    public static final Expense DUMPLING_SOUP = new ExpensesBuilder().withName("Dumpling Soup")
            .withCost("5.00")
            .withDate(VALID_DATE_2019)
            .withTags(VALID_TAG_FOOD, "lunch", VALID_TAG_EXPENSE).build();
    public static final Expense STATIONARY = new ExpensesBuilder().withName("Stationary")
            .withCost("3.00")
            .withDate("06/03/2019")
            .withTags("school", "misc", VALID_TAG_EXPENSE).build();
    public static final Expense MOVIE_AVENGERS = new ExpensesBuilder().withName("Avengers Endgame movie")
            .withDate("26/04/2019")
            .withCost("8.50")
            .withTags("movie", "entertainment", VALID_TAG_FRIENDS, VALID_TAG_EXPENSE).build();
    public static final Expense KARAOKE = new ExpensesBuilder().withName("Karaoke KTV")
            .withCost("10.00")
            .withDate(VALID_DATE_2018)
            .withTags(VALID_TAG_FRIENDS, VALID_TAG_EXPENSE).build();
    public static final Expense CLOTHES = new ExpensesBuilder().withName("Clothes shopping")
            .withCost("21.80")
            .withTags("shopping", VALID_TAG_EXPENSE).build();
    public static final Expense KFC = new ExpensesBuilder().withName("KFC")
            .withDate("26/04/2019")
            .withCost("13.00")
            .withTags(VALID_TAG_FOOD, "dinner", VALID_TAG_EXPENSE).build();

    // Manually added
    public static final Expense BOWLING =
            new ExpensesBuilder().withName(VALID_NAME_BOWLING)
            .withCost(VALID_COST_BOWLING)
            .withDate(VALID_DATE_2018)
            .withTags(VALID_TAG_FRIENDS).build();
    public static final Expense IPHONE =
            new ExpensesBuilder().withName(VALID_NAME_IPHONE)
                    .withCost(VALID_COST_IPHONE)
                    .withDate(VALID_DATE_2019).build();

    public static final String KEYWORD_MATCHING_CLOTHES = "n/Clothes"; // A keyword that matches Clothes
    public static final String KEYWORD_MATCHING_SOUP = "n/soup"; //A keyword that matches soup
    public static final String KEYWORD_MATCHING_FOOD = "t/Food"; //A keyword that matches Food tag

    private TypicalExpenses() {} // prevents instantiation

    /**
     * Returns an {@code EPiggy} with all the typical expenses.
     */
    public static EPiggy getTypicalEPiggy() {
        EPiggy ab = new EPiggy();
        for (Expense expense : getTypicalExpenses()) {
            ab.addExpense(expense);
        }
        return ab;
    }

    public static List<Expense> getTypicalExpenses() {
        return new ArrayList<>(Arrays.asList(DUMPLING_SOUP, STATIONARY, MOVIE_AVENGERS, KARAOKE, CLOTHES, KFC,
                BOWLING, IPHONE));
    }
}
