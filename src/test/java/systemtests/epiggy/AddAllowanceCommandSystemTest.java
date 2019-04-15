package systemtests.epiggy;

import static seedu.address.commons.core.Messages.MESSAGE_CANNOT_CREATE_ALLOWANCE_TAG;
import static seedu.address.commons.core.Messages.MESSAGE_CANNOT_CREATE_EXPENSE_TAG;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_DATE;
import static seedu.address.logic.commands.CommandTestUtil.AMOUNT_DESC_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.AMOUNT_DESC_SECONDEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_AMOUNT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_SECONDEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_ALLOWANCE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_EXPENSE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FIRSTEXTRA;
import static seedu.address.testutil.epiggy.TypicalAllowances.FIRST_EXTRA;
import static seedu.address.testutil.epiggy.TypicalAllowances.SECOND_EXTRA;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.epiggy.AddAllowanceCommand;
import seedu.address.model.Model;
import seedu.address.model.epiggy.Allowance;
import seedu.address.model.epiggy.item.Cost;
import seedu.address.model.epiggy.item.Name;
import seedu.address.testutil.epiggy.AllowanceUtil;

//@@author kev-inc

public class AddAllowanceCommandSystemTest extends EPiggySystemTestWithEmptyData {

    private String messageHistory = "";

    @Test
    public void add() {
        Model model = getModel();

        /* Performing an add allowance to the expense list */

        // Case: Adding an allowance to epiggy, with leading spaces added
        Allowance toAdd = FIRST_EXTRA;
        String command = "  " + AddAllowanceCommand.COMMAND_WORD + " " + NAME_DESC_FIRSTEXTRA + " "
                + AMOUNT_DESC_FIRSTEXTRA + " " + DATE_DESC_FIRSTEXTRA + " " + TAG_DESC_FIRSTEXTRA;
        assertCommandSuccess(command, toAdd);

        // Case: Undo adding allowance - allowance removed
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        // Case: Redo adding allowance
        command = RedoCommand.COMMAND_WORD;
        model.addAllowance(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        // Case: Add to empty epiggy
        messageHistory = deleteAllExpenses() + messageHistory;
        assertCommandSuccess(SECOND_EXTRA);

        /* Performing invalid add allowance commands */

        // Case: Missing name -> Rejected
        command = AddAllowanceCommand.COMMAND_WORD + AMOUNT_DESC_SECONDEXTRA;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAllowanceCommand.MESSAGE_USAGE));

        // Case: Missing cost -> Rejected
        command = AddAllowanceCommand.COMMAND_WORD + NAME_DESC_SECONDEXTRA;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAllowanceCommand.MESSAGE_USAGE));

        // Case: Invalid keyword -> Rejected
        command = "addallowance " + AllowanceUtil.getAllowanceDetails(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        // Case: Invalid name -> Rejected
        command = AddAllowanceCommand.COMMAND_WORD + INVALID_NAME_DESC + AMOUNT_DESC_SECONDEXTRA;
        assertCommandFailure(command, Name.MESSAGE_CONSTRAINTS);

        // Case: Invalid cost -> Rejected
        command = AddAllowanceCommand.COMMAND_WORD + NAME_DESC_SECONDEXTRA + INVALID_AMOUNT_DESC;
        assertCommandFailure(command, Cost.MESSAGE_CONSTRAINTS);

        // Case: Invalid date -> Rejected
        command = AddAllowanceCommand.COMMAND_WORD + NAME_DESC_SECONDEXTRA
                + AMOUNT_DESC_SECONDEXTRA + INVALID_DATE_DESC;
        assertCommandFailure(command, MESSAGE_INVALID_DATE);

        // Case: Tag contains reserved word "allowance" -> Rejected
        command = AddAllowanceCommand.COMMAND_WORD + NAME_DESC_SECONDEXTRA
                + AMOUNT_DESC_SECONDEXTRA + TAG_DESC_ALLOWANCE;
        assertCommandFailure(command, MESSAGE_CANNOT_CREATE_ALLOWANCE_TAG);

        // Case: Tag contains reserved word "expense" -> Rejected
        command = AddAllowanceCommand.COMMAND_WORD + NAME_DESC_SECONDEXTRA
                + AMOUNT_DESC_SECONDEXTRA + TAG_DESC_EXPENSE;
        assertCommandFailure(command, MESSAGE_CANNOT_CREATE_EXPENSE_TAG);


    }

    /**
     * Executes the {@code AddAllowanceCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddAllowanceCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Storage} and {@code ExpenseListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code EPiggySystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see EPiggySystemTestWithEmptyData#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Allowance toAdd) {
        assertCommandSuccess(AllowanceUtil.getAddAllowanceCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Allowance)}. Executes {@code command}
     * instead.
     *
     * @see AddAllowanceCommandSystemTest#assertCommandSuccess(Allowance)
     */
    private void assertCommandSuccess(String command, Allowance toAdd) {
        Model expectedModel = getModel();
        expectedModel.addAllowance(toAdd);
        String expectedMessage = String.format(AddAllowanceCommand.MESSAGE_SUCCESS, toAdd);
        assertCommandSuccess(command, expectedModel, expectedMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Allowance)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Storage} and {@code ExpenseListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     *
     * @see AddAllowanceCommandSystemTest#assertCommandSuccess(String, Allowance)
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
     * 4. {@code Storage} and {@code ExpenseListPanel} remain unchanged.<br>
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
