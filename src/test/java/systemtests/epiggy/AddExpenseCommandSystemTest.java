package systemtests.epiggy;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_DATE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_AMOUNT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.testutil.epiggy.ExpenseUtil.descExpenseCost;
import static seedu.address.testutil.epiggy.ExpenseUtil.descExpenseName;
import static seedu.address.testutil.epiggy.TypicalExpenses.KARAOKE;
import static seedu.address.testutil.epiggy.TypicalExpenses.KFC;
import static seedu.address.testutil.epiggy.TypicalExpenses.STATIONARY;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.epiggy.AddExpenseCommand;
import seedu.address.model.Model;
import seedu.address.model.epiggy.Expense;
import seedu.address.model.epiggy.item.Cost;
import seedu.address.model.epiggy.item.Name;
import seedu.address.testutil.epiggy.ExpenseUtil;

public class AddExpenseCommandSystemTest extends EPiggySystemTestWithEmptyData {
    private String messageHistory = "";

    @Test
    public void add() {
        Model model = getModel();

        // Adding an expense to non-empty epiggy, with leading spaces added
        Expense toAdd = KARAOKE;
        String command = ExpenseUtil.getAddExpenseCommand(toAdd);
        assertCommandSuccess(command, toAdd);

        // undo adding expense - expense removed
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        // redo adding expense
        command = RedoCommand.COMMAND_WORD;
        model.addExpense(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        // add to empty epiggy
        messageHistory = deleteAllExpenses() + messageHistory;
        assertCommandSuccess(STATIONARY);

        // invalid add operations

        // missing cost -> rejected
        command = AddExpenseCommand.COMMAND_WORD + " " + descExpenseName(KFC);
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddExpenseCommand.MESSAGE_USAGE));

        // invalid keyword
        command = "addexpense " + ExpenseUtil.getExpenseDetails(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        // invalid name
        command = AddExpenseCommand.COMMAND_WORD + INVALID_NAME_DESC + " " + descExpenseCost(toAdd);
        assertCommandFailure(command, Name.MESSAGE_CONSTRAINTS);

        // invalid cost
        command = AddExpenseCommand.COMMAND_WORD + " " + descExpenseName(toAdd) + INVALID_AMOUNT_DESC;
        assertCommandFailure(command, Cost.MESSAGE_CONSTRAINTS);

        // invalid date
        command = AddExpenseCommand.COMMAND_WORD + " " + descExpenseName(toAdd) + " "
                + descExpenseCost(toAdd) + INVALID_DATE_DESC;
        assertCommandFailure(command, MESSAGE_INVALID_DATE);


    }

    /**
     * Executes the {@code AddBudgetCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddBudgetCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Storage} and {@code BudgetListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code EPiggySystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see EPiggySystemTestWithEmptyData#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Expense toAdd) {
        assertCommandSuccess(ExpenseUtil.getAddExpenseCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Budget)}. Executes {@code command}
     * instead.
     *
     * @see AddExpenseCommandSystemTest#assertCommandSuccess(Expense)
     */
    private void assertCommandSuccess(String command, Expense toAdd) {
        Model expectedModel = getModel();
        expectedModel.addExpense(toAdd);
        String expectedMessage = String.format(AddExpenseCommand.MESSAGE_SUCCESS, toAdd);
        // assertEquals(messageHistory, getResultDisplay().getText());
        assertCommandSuccess(command, expectedModel, expectedMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Budget)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Storage} and {@code BudgetListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     *
     * @see AddExpenseCommandSystemTest#assertCommandSuccess(String, Expense)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        messageHistory = "========================\n" + "ePiggy: " + expectedResultMessage + "\n\n" + "You: " + command
                + "\n" + messageHistory;
        assertApplicationDisplaysExpected("", messageHistory, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Storage} and {@code BudgetListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code EPiggySystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see EPiggySystemTestWithEmptyData#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        messageHistory = "========================\n" + "ePiggy: " + expectedResultMessage + "\n\n" + "You: " + command
                + "\n" + messageHistory;
        assertApplicationDisplaysExpected(command, messageHistory, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
