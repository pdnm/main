package seedu.address.testutil.epiggy;

import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_2018;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_2019;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FOOD;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIENDS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.EPiggy;
import seedu.address.model.epiggy.Budget;
import seedu.address.model.epiggy.Expense;

/**
 * A utility class containing a list of {@code budget}, {@code expense},
 * {@code allowance} objects to be used in tests.
 */
public class TypicalReports {

    // valid test budgets
    // Date is in the form mm/dd/yyyy
    public static final Budget VALID_BUDGET_ONE = new BudgetBuilder().withAmount("100")
            .withPeriod("7").withDate("02/04/2019").build();
    public static final Budget VALID_BUDGET_TWO = new BudgetBuilder().withAmount("300")
            .withPeriod("18").withDate("02/11/2019").build();
    public static final Budget VALID_BUDGET_THREE = new BudgetBuilder().withAmount("500")
            .withDate("03/01/2019").withPeriod("30").build();

    // test expenses
    public static final Expense DUMPLING_SOUP = new ExpensesBuilder().withName("Dumpling Soup")
            .withCost("5.00")
            .withDate(VALID_DATE_2019)
            .withTags(VALID_TAG_FOOD, "lunch").build();
    public static final Expense STATIONARY = new ExpensesBuilder().withName("Stationary")
            .withCost("3.00")
            .withDate("07/04/2019")
            .withTags("school", "misc").build();
    public static final Expense MOVIE_AVENGERS = new ExpensesBuilder().withName("Avengers : Endgame movie")
            .withDate("26/04/2019")
            .withCost("8.50")
            .withTags("movie", "entertainment", VALID_TAG_FRIENDS).build();
    public static final Expense KARAOKE = new ExpensesBuilder().withName("Karaoke: KTV")
            .withCost("26.90")
            .withDate(VALID_DATE_2018)
            .withTags(VALID_TAG_FRIENDS).build();
    public static final Expense CLOTHES = new ExpensesBuilder().withName("Clothes shopping")
            .withCost("50.80")
            .withTags("shopping").build();
    public static final Expense KFC = new ExpensesBuilder().withName("KFC")
            .withDate("26/04/2019")
            .withCost("13.95")
            .withTags(VALID_TAG_FOOD, "dinner").build();
    public static final String KEYWORD_MATCHING_STATIONARY = "Stationary";
    public static final String KEYWORD_MATCHING_DINNER = "dinner";
    public static final String KEYWORD_MATCHING_DATE = "26/04/2019";
    public static final String KEYWORD_MATCHING_START_DATE = "25/04/2019";
    public static final String KEYWORD_MATCHING_END_DATE = "27/04/2019";
    private TypicalReports() {
    } // prevents instantiation
    /**
     * Returns an {@code EPiggy} with all the typical expenses, allowance and budget.
     * TODO: allowance are not add to storage.
     */
    public static EPiggy getTypicalEPiggy() {
        // todo: change the method here
        EPiggy ePiggy = new EPiggy();

        //add budgets into ePiggy
        int i = 0;
        for (Budget budget : getTypicalBudgets()) {
            ePiggy.addBudget(i, budget);
            i++;
        }

        //add expenses into ePiggy
        for (Expense expense : getTypicalExpenses()) {
            ePiggy.addExpense(expense);
        }
        return ePiggy;
    }

    /**
     * Returns an {@code EPiggy} with empty data.
     */
    public static EPiggy getTypicalEPiggyWithEmptyData() {
        return new EPiggy();
    }

    public static List<Budget> getTypicalBudgets() {
        return new ArrayList<>(Arrays.asList(VALID_BUDGET_ONE, VALID_BUDGET_TWO,
                VALID_BUDGET_THREE));
    }

    public static List<Expense> getTypicalExpenses() {
        return new ArrayList<>(Arrays.asList(DUMPLING_SOUP, STATIONARY, MOVIE_AVENGERS, KARAOKE, CLOTHES, KFC));
    }
}

