package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERIOD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.epiggy.EditBudgetCommand;
import seedu.address.logic.commands.epiggy.EditExpenseCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.model.EPiggy;
import seedu.address.model.Model;
import seedu.address.model.epiggy.Budget;
import seedu.address.model.epiggy.Expense;
import seedu.address.model.epiggy.ExpenseContainsKeywordsPredicate;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.epiggy.EditBudgetDetailsBuilder;
import seedu.address.testutil.epiggy.EditExpenseDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;

    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses

    public static final String VALID_TAG_FOOD = "food";
    public static final String VALID_TAG_FRIENDS = "friends";
    public static final String VALID_DATE_2018 = "10/07/2018";
    public static final String VALID_DATE_2019 = "02/03/2019";
    public static final String VALID_NAME_BOWLING = "bowling at West Bowl centre";
    public static final String VALID_COST_BOWLING = "4.80";
    public static final String VALID_NAME_IPHONE = "IPhone XR from challenger";
    public static final String VALID_COST_IPHONE = "1799.00";

    public static final String VALID_AMOUNT_FIRSTEXTRA = "500";
    public static final String VALID_AMOUNT_SECONDEXTRA = "1000";
    public static final String VALID_PERIOD_FIRSTEXTRA = "7";
    public static final String VALID_PERIOD_SECONDEXTRA = "28";
    public static final String VALID_DATE_FIRSTEXTRA = "03/03/2019";
    public static final String VALID_DATE_SECONDEXTRA = "01/02/2019";
    public static final String VALID_NAME_FIRSTEXTRA = "Apple watch";
    public static final String VALID_NAME_SECONDEXTRA = "Apple Pineapple";
    public static final String VALID_TAG_EXPENSE = "Expense";
    public static final String VALID_TAG_ALLOWANCE = "Allowance";
    public static final String VALID_TAG_FIRSTEXTRA = "Shopping";
    public static final String VALID_TAG_SECONDEXTRA = "Fruits";

    public static final String AMOUNT_DESC_FIRSTEXTRA = " " + PREFIX_COST + VALID_AMOUNT_FIRSTEXTRA;
    public static final String AMOUNT_DESC_SECONDEXTRA = " " + PREFIX_COST + VALID_AMOUNT_SECONDEXTRA;
    public static final String PERIOD_DESC_FIRSTEXTRA = " " + PREFIX_PERIOD + VALID_PERIOD_FIRSTEXTRA;
    public static final String PERIOD_DESC_SECONDEXTRA = " " + PREFIX_PERIOD + VALID_PERIOD_SECONDEXTRA;
    public static final String DATE_DESC_FIRSTEXTRA = " " + PREFIX_DATE + VALID_DATE_FIRSTEXTRA;
    public static final String DATE_DESC_SECONDEXTRA = " " + PREFIX_DATE + VALID_DATE_SECONDEXTRA;
    public static final String NAME_DESC_FIRSTEXTRA = " " + PREFIX_NAME + VALID_NAME_FIRSTEXTRA;
    public static final String NAME_DESC_SECONDEXTRA = " " + PREFIX_NAME + VALID_NAME_SECONDEXTRA;
    public static final String TAG_DESC_FIRSTEXTRA = " " + PREFIX_TAG + VALID_TAG_FIRSTEXTRA;
    public static final String TAG_DESC_SECONDEXTRA = " " + PREFIX_TAG + VALID_TAG_SECONDEXTRA;
    public static final String TAG_DESC_EXPENSE = " " + PREFIX_TAG + VALID_TAG_EXPENSE;
    public static final String TAG_DESC_ALLOWANCE = " " + PREFIX_TAG + VALID_TAG_ALLOWANCE;


    public static final String INVALID_AMOUNT_DESC = " " + PREFIX_COST + "-500.00"; // negative cost is not allowed
    public static final String INVALID_PERIOD_DESC = " " + PREFIX_PERIOD + "0"; // period of 0 is not allowed
    public static final String INVALID_DATE_DESC = " " + PREFIX_DATE + "12/04"; // year should be mentioned

    public static final String NAME_DESC_BOWLING = " " + PREFIX_NAME + VALID_NAME_BOWLING;
    public static final String NAME_DESC_IPHONE = " " + PREFIX_NAME + VALID_NAME_IPHONE;
    public static final String COST_DESC_BOWLING = " " + PREFIX_COST + VALID_COST_BOWLING;
    public static final String COST_DESC_IPHONE = " " + PREFIX_COST + VALID_COST_IPHONE;
    public static final String DATE_DESC_2018 = " " + PREFIX_DATE + VALID_DATE_2018;
    public static final String DATE_DESC_2019 = " " + PREFIX_DATE + VALID_DATE_2019;
    public static final String TAG_DESC_FRIENDS = " " + PREFIX_TAG + VALID_TAG_FRIENDS;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_COST_DESC = " " + PREFIX_COST + "0.00"; // cost of 0 is not allowed
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "food*"; // '*' not allowed in tags

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditExpenseCommand.EditExpenseDescriptor DESC_BOWLING;
    public static final EditExpenseCommand.EditExpenseDescriptor DESC_IPHONE;

    public static final EditBudgetCommand.EditBudgetDetails DESC_FIRSTEXTRA;
    public static final EditBudgetCommand.EditBudgetDetails DESC_SECONDEXTRA;

    public static final EditCommand.EditPersonDescriptor DESC_AMY;
    public static final EditCommand.EditPersonDescriptor DESC_BOB;

    static {
        DESC_BOWLING = new EditExpenseDescriptorBuilder().withName(VALID_NAME_BOWLING)
                .withCost(VALID_COST_BOWLING).withDate(VALID_DATE_2018)
                .withTags(VALID_TAG_FRIENDS).build();
        DESC_IPHONE = new EditExpenseDescriptorBuilder().withName(VALID_NAME_IPHONE)
                .withCost(VALID_COST_IPHONE).withDate(VALID_DATE_2019).build();
        DESC_FIRSTEXTRA = new EditBudgetDetailsBuilder().withAmount(VALID_AMOUNT_FIRSTEXTRA)
                .withPeriod(VALID_PERIOD_FIRSTEXTRA).withDate(VALID_DATE_FIRSTEXTRA).build();
        DESC_SECONDEXTRA = new EditBudgetDetailsBuilder().withAmount(VALID_AMOUNT_SECONDEXTRA)
                .withPeriod(VALID_PERIOD_SECONDEXTRA).withDate(VALID_DATE_SECONDEXTRA).build();
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();

    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel} <br>
     * - the {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandHistory actualCommandHistory,
            CommandResult expectedCommandResult, Model expectedModel) {
        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);
        try {
            CommandResult result = command.execute(actualModel, actualCommandHistory);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
            assertEquals(expectedCommandHistory, actualCommandHistory);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandHistory, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage, Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, actualCommandHistory, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in {@code actualModel} remain unchanged <br>
     * - {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandFailure(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        EPiggy expectedEPiggy = new EPiggy(actualModel.getEPiggy());
        List<Expense> expectedFilteredList = new ArrayList<>(actualModel.getFilteredExpenseList());
        Expense expectedSelectedExpense = actualModel.getSelectedExpense();

        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);

        try {
            command.execute(actualModel, actualCommandHistory);
            throw new AssertionError("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedEPiggy, actualModel.getEPiggy());
            assertEquals(expectedFilteredList, actualModel.getFilteredExpenseList());
            assertEquals(expectedSelectedExpense, actualModel.getSelectedExpense());
            assertEquals(expectedCommandHistory, actualCommandHistory);
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the expense at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredExpenseList().size());

        Expense expense = model.getFilteredExpenseList().get(targetIndex.getZeroBased());
        String[] splitName = expense.getItem().getName().name.split("\\s+");
        final ArgumentMultimap keywordsMap = ArgumentTokenizer.tokenize(" n/"
                + splitName[0], PREFIX_NAME);
        model.updateFilteredExpensesList(new ExpenseContainsKeywordsPredicate(keywordsMap));

        assertEquals(1, model.getFilteredExpenseList().size());
    }

    /**
     * Updates {@code model}'s filtered list to show only the budget at the given {@code targetIndex} in the
     * {@code model}'s epiggy.
     */
    public static void showBudgetAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredBudgetList().size());

        Budget budget = model.getFilteredBudgetList().get(targetIndex.getZeroBased());
        model.updateFilteredBudgetList(Predicate.isEqual(budget));

        assertEquals(1, model.getFilteredBudgetList().size());
    }
    /**
     * Updates {@code model}'s filtered list to show only the expense at the given {@code targetIndex} in the
     * {@code model}'s epiggy.
     */
    public static void showExpenseAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredExpenseList().size());;
        Expense expense = model.getExpenseList().get(targetIndex.getZeroBased());
        model.updateFilteredExpensesList(Predicate.isEqual(expense));
        assertEquals(1, model.getFilteredExpenseList().size());
    }

    /**
     * Deletes the first expense in {@code model}'s filtered list from {@code model}'s address book.
     */
    public static void deleteFirstPerson(Model model) {
        Expense firstExpense = model.getFilteredExpenseList().get(0);
        model.deleteExpense(firstExpense);
        model.commitEPiggy();
    }

}
