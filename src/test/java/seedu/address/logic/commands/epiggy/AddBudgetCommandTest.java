package seedu.address.logic.commands.epiggy;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.epiggy.Allowance;
import seedu.address.model.epiggy.Budget;
import seedu.address.model.epiggy.Expense;
import seedu.address.model.epiggy.Goal;
import seedu.address.model.epiggy.Savings;
import seedu.address.model.person.Person;
import seedu.address.testutil.epiggy.BudgetBuilder;

public class AddBudgetCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullBudget_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddBudgetCommand(null);
    }

    @Test
    public void execute_budgetAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingBudgetAdded modelStub = new ModelStubAcceptingBudgetAdded();
        Budget validBudget = new BudgetBuilder().build();

        CommandResult commandResult = new AddBudgetCommand(validBudget).execute(modelStub, commandHistory);

        assertEquals(String.format(AddBudgetCommand.MESSAGE_SUCCESS, validBudget), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validBudget), modelStub.budgetsAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicateBudget_throwsCommandException() throws Exception {
        Budget validBudget = new BudgetBuilder().build();
        // Person validPerson = new PersonBuilder().build();
        AddBudgetCommand addBudgetCommand = new AddBudgetCommand(validBudget);
        ModelStub modelStub = new ModelStubWithBudget(validBudget);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddBudgetCommand.MESSAGE_OVERLAPPING_BUDGET);
        addBudgetCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void execute_overlappingStartDate_throwsCommandException() throws Exception {
        Budget validBudget = new BudgetBuilder().build();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(validBudget.getEndDate());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String endDate = simpleDateFormat.format(calendar.getTime());
        Budget overlappingBudget = new BudgetBuilder().withDate(endDate).build();

        AddBudgetCommand addBudgetCommand = new AddBudgetCommand(validBudget);
        ModelStub modelStub = new ModelStubWithBudget(overlappingBudget);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddBudgetCommand.MESSAGE_OVERLAPPING_BUDGET);
        addBudgetCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void execute_overlappingEndDate_throwsCommandException() throws Exception {
        Budget validBudget = new BudgetBuilder().build();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(validBudget.getStartDate());
        calendar.add(Calendar.DAY_OF_MONTH, 1 - Integer.parseInt(BudgetBuilder.DEFAULT_PERIOD));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String startDate = simpleDateFormat.format(calendar.getTime());
        Budget overlappingBudget = new BudgetBuilder().withDate(startDate).build();

        AddBudgetCommand addBudgetCommand = new AddBudgetCommand(validBudget);
        ModelStub modelStub = new ModelStubWithBudget(overlappingBudget);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddBudgetCommand.MESSAGE_OVERLAPPING_BUDGET);
        addBudgetCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void equals() {
        Budget twenty = new BudgetBuilder().withAmount("20").build();
        Budget fifty = new BudgetBuilder().withAmount("50").build();
        // Person alice = new PersonBuilder().withName("Alice").build();
        // Person bob = new PersonBuilder().withName("Bob").build();
        AddBudgetCommand addTwentyCommand = new AddBudgetCommand(twenty);
        AddBudgetCommand addFiftyCommand = new AddBudgetCommand(fifty);

        // same object -> returns true
        assertTrue(addTwentyCommand.equals(addTwentyCommand));

        // same values -> returns true
        AddBudgetCommand addTwentyCommandCopy = new AddBudgetCommand(twenty);
        assertTrue(addTwentyCommand.equals(addTwentyCommandCopy));

        // different types -> returns false
        assertFalse(addTwentyCommand.equals(1));

        // null -> returns false
        assertFalse(addTwentyCommand.equals(null));

        // different budgets -> returns false
        assertFalse(addTwentyCommand.equals(addFiftyCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addExpense(Expense expense) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addAllowance(Allowance allowance) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProperty<Expense> selectedExpenseProperty() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addBudget(int index, Budget budget) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean budgetsOverlap(Date startDate, Date endDate, Budget earlierBudget) {
            throw new AssertionError("This method should not be called");
        }

        @Override
        public void deleteBudgetAtIndex(int index) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteExpense(Expense toDelete) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public int getCurrentBudgetIndex() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public SimpleObjectProperty<Savings> getSavings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public SimpleObjectProperty<Goal> getGoal() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGoal(Goal goal) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSelectedExpense(Expense expense) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setCurrentBudget(Budget editedBudget) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Budget> getFilteredBudgetList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Expense> getFilteredExpenseList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredBudgetList(Predicate<Budget> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredExpensesList(Predicate<Expense> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortExpenses(ArgumentMultimap keywords) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProperty<Person> selectedPersonProperty() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setExpense(Expense target, Expense editedExpense) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Person getSelectedPerson() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSelectedPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithBudget extends ModelStub {
        final ArrayList<Budget> budgets = new ArrayList<>();
        private AddressBook addressBook = new AddressBook();

        ModelStubWithBudget(Budget budget) {
            requireNonNull(budget);
            budgets.add(budget);
        }

        @Override
        public ObservableList<Budget> getFilteredBudgetList() {
            return FXCollections.observableArrayList(budgets);
        }

        @Override
        public boolean budgetsOverlap(Date startDate, Date endDate, Budget earlierBudget) {
            return addressBook.budgetsOverlap(startDate, endDate, earlierBudget);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingBudgetAdded extends ModelStub {
        final ArrayList<Budget> budgetsAdded = new ArrayList<>();

        @Override
        public void addBudget(int index, Budget toAdd) {
            budgetsAdded.add(index, toAdd);
        }

        @Override
        public ObservableList<Budget> getFilteredBudgetList() {
            return FXCollections.observableArrayList(budgetsAdded);
        }

        @Override
        public boolean budgetsOverlap(Date startDate, Date endDate, Budget earlierBudget) {
            requireAllNonNull(startDate, endDate, earlierBudget);
            return false;
        }

        @Override
        public void commitAddressBook() {
            // called by {@code AddCommand#execute()}
        }
    }
}
