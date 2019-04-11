package seedu.address.logic.commands.epiggy;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.epiggy.TypicalExpenses.BOWLING;
import static seedu.address.testutil.epiggy.TypicalExpenses.CLOTHES;
import static seedu.address.testutil.epiggy.TypicalExpenses.DUMPLING_SOUP;
import static seedu.address.testutil.epiggy.TypicalExpenses.IPHONE;
import static seedu.address.testutil.epiggy.TypicalExpenses.KARAOKE;
import static seedu.address.testutil.epiggy.TypicalExpenses.KFC;
import static seedu.address.testutil.epiggy.TypicalExpenses.MOVIE_AVENGERS;
import static seedu.address.testutil.epiggy.TypicalExpenses.STATIONARY;
import static seedu.address.testutil.epiggy.TypicalExpenses.getTypicalEPiggy;

import java.util.Arrays;

import org.junit.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.epiggy.comparators.CompareExpenseByCost;
import seedu.address.model.epiggy.comparators.CompareExpenseByDate;
import seedu.address.model.epiggy.comparators.CompareExpenseByName;

/**
 * Contains integration tests (interaction with the Model) for {@code SortExpenseCommand}.
 */
public class SortExpenseCommandTest {
    private Model model = new ModelManager(getTypicalEPiggy(), new UserPrefs());
    private Model expectedModel;

    @Test
    public void execute_sortByName_success() {
        expectedModel = new ModelManager(getTypicalEPiggy(), new UserPrefs());
        SortExpenseCommand command = new SortExpenseCommand(new CompareExpenseByName());
        command.execute(model, null);
        expectedModel.sortExpenses(new CompareExpenseByName());
        assertEquals(Arrays.asList(MOVIE_AVENGERS, BOWLING, CLOTHES, DUMPLING_SOUP, IPHONE, KARAOKE, KFC, STATIONARY),
                model.getFilteredExpenseList());
    }

    @Test
    public void execute_sortByCost_success() {
        expectedModel = new ModelManager(getTypicalEPiggy(), new UserPrefs());
        SortExpenseCommand command = new SortExpenseCommand(new CompareExpenseByCost());
        command.execute(model, null);
        expectedModel.sortExpenses(new CompareExpenseByCost());
        assertEquals(Arrays.asList(IPHONE, CLOTHES, KFC, KARAOKE, MOVIE_AVENGERS, DUMPLING_SOUP, BOWLING, STATIONARY),
                model.getFilteredExpenseList());
    }

    @Test
    public void execute_sortByDate_success() {
        expectedModel = new ModelManager(getTypicalEPiggy(), new UserPrefs());
        SortExpenseCommand command = new SortExpenseCommand(new CompareExpenseByDate());
        command.execute(model, null);
        expectedModel.sortExpenses(new CompareExpenseByDate());
        assertEquals(Arrays.asList(MOVIE_AVENGERS, KFC, STATIONARY, DUMPLING_SOUP, IPHONE, CLOTHES, KARAOKE, BOWLING),
                model.getFilteredExpenseList());
    }
}
