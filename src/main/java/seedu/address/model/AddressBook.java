package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.util.InvalidationListenerManager;
import seedu.address.model.epiggy.Allowance;
import seedu.address.model.epiggy.Budget;
import seedu.address.model.epiggy.Expense;
import seedu.address.model.epiggy.ExpenseList;
import seedu.address.model.epiggy.Goal;
import seedu.address.model.epiggy.Savings;
import seedu.address.model.epiggy.UniqueBudgetList;
import seedu.address.model.epiggy.item.Item;
import seedu.address.model.epiggy.item.Period;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final ExpenseList expenses;
    private final ObservableList<Item> items;
    private SimpleObjectProperty<Goal> goal;
    private SimpleObjectProperty<Savings> savings;
    private final UniquePersonList persons;
    private final UniqueBudgetList budgetList;
    private final InvalidationListenerManager invalidationListenerManager = new InvalidationListenerManager();

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        expenses = new ExpenseList();
        items = FXCollections.observableArrayList();
        budgetList = new UniqueBudgetList();
        persons = new UniquePersonList();
        goal = new SimpleObjectProperty<>();
        savings = new SimpleObjectProperty<>(new Savings());

    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
        indicateModified();
    }

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void addBudgetList(List<Budget> budgets) {
        this.budgetList.addBudgetList(budgets);
        indicateModified();
    }

    /**
     * Replaces the contents of the expense list with {@code expenses}.
     * {@code expenses} can contain duplicate expenses.
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses.setExpenses(expenses);
        indicateModified();
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setPersons(newData.getPersonList());
        addBudgetList(newData.getBudgetList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
        indicateModified();
    }

    /**
     * Adds an expense to the expense book.
     */
    public void addExpense(Expense expense) {
        expenses.add(expense);

        Savings s = savings.get();
        s.deductSavings(expense.getItem().getCost().getAmount());
        this.savings.setValue(new Savings(s));

        if (budgetList.getBudgetListSize() > 0) {
            int indexOfBudgetToEdit = budgetList.getBudgetIndexBasedOnDate(expense.getDate());
            if (indexOfBudgetToEdit >= 0) {
                Budget budgetToEdit = budgetList.getBudgetAtIndex(indexOfBudgetToEdit);
                Budget editedBudget = updateBudget(budgetToEdit);
                budgetList.replaceAtIndex(indexOfBudgetToEdit, editedBudget);
            }
        }
        indicateModified();
    }

    /**
     * Adds an allowance to the expense book.
     * @param allowance to be added.
     */
    public void addAllowance(Allowance allowance) {
        expenses.add(allowance);
        Savings s = savings.get();
        s.addSavings(allowance.getItem().getCost().getAmount());
        this.savings.setValue(new Savings(s));
        indicateModified();
    }

    public SimpleObjectProperty<Savings> getSavings() {
        return savings;
    }

    /**
     * Adds a budget to the budgetList.
     * Called by the Command addBudget only.
     * @param budget to be added into budgetList.
     */
    public void addBudget(int index, Budget budget) {
        budget = updateBudget(budget);

        budgetList.addAtIndex(index, budget);
        indicateModified();
    }

    /**
     * Deletes the budget at the specific index.
     * @param index of the to be deleted budget.
     */
    public void deleteBudgetAtIndex(int index) {
        budgetList.remove(index);
        indicateModified();
    }

    /**
     * Deletes the expense {@code toDelete}.
     * @param toDelete the expense to be deleted.
     */
    public void deleteExpense(Expense toDelete) {
        expenses.remove(toDelete);
        indicateModified();
    }

    /**
     * Updates the remaining amount and days of the budget.
     * Allowances in the Expense list does not affect the budget.
     * @param budget to be updated.
     * @return updated budget.
     */
    private Budget updateBudget(Budget budget) {
        budget.setRemainingDays(calculateRemainingDays(budget));

        budget.resetRemainingAmount();
        SortedList<Expense> sortedExpensesByDate = sortExpensesByDate();
        ListIterator<Expense> iterator = sortedExpensesByDate.listIterator();
        while (iterator.hasNext()) {
            Expense expense = iterator.next();
            if (!expense.getDate().before(budget.getStartDate())) {
                if (budget.getEndDate().after(expense.getDate())) {
                    if (!(expense instanceof Allowance)) {
                        budget.deductRemainingAmount(expense.getItem().getCost());
                    }
                } else {
                    return budget;
                }
            }
        }
        return budget;
    }

    /**
     * Calculates the remaining days for the budget based on the current date.
     * @param budget to calculate the remaining days for.
     * @return remaining days.
     */
    private Period calculateRemainingDays(Budget budget) {
        Date todaysDate = new Date();
        if (todaysDate.after(budget.getEndDate())) {
            return new Period(0);
        }
        long diffInMillies = budget.getEndDate().getTime() - todaysDate.getTime();
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return new Period((int) Math.ceil(diff));
    }

    /**
     * Checks if there are any overlapping budgets.
     */
    public boolean budgetsOverlap(Date startDate, Date endDate, Budget earlierBudget) {
        if (!startDate.after(earlierBudget.getStartDate()) && endDate.after(earlierBudget.getStartDate())) {
            return true;
        }
        if (!startDate.before(earlierBudget.getStartDate()) && !endDate.after(earlierBudget.getEndDate())) {
            return true;
        }
        if (startDate.before(earlierBudget.getEndDate()) && !endDate.before(earlierBudget.getEndDate())) {
            return true;
        }
        return false;
    }

    /**
     * Sorts Expenses according to Date. Earlier Dates will have lower indexes.
     * @return SortedList of Expenses
     */
    public SortedList<Expense> sortExpensesByDate() {
        return expenses.sortByDate();
    }

    /**
     * Sorts Expenses according to Date. Earlier Dates will have lower indexes.
     * @return SortedList of Expenses
     */
    public SortedList<Expense> sortExpensesByName() {
        return expenses.sortByName();
    }

    /**
     * Sorts Expenses according to Date. Earlier Dates will have lower indexes.
     * @return SortedList of Expenses
     */
    public SortedList<Expense> sortExpensesByAmount() {
        return expenses.sortByAmount();
    }


    /**
     * Replaces the given expense {@code target} in the list with {@code editedExpense}.
     * {@code target} must exist in the expense tracker.
     * The expense identity of {@code editedExpense}
     * must not be the same as another existing expense in the expense tracker.
     */
    public void setExpense(Expense target, Expense editedExpense) {
        requireNonNull(editedExpense);
        expenses.setExpense(target, editedExpense);
        indicateModified();
    }

    /**
     * Replaces the current/previous budget in the list with {@code editedBudget}.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setCurrentBudget(Budget editedBudget) {
        requireNonNull(editedBudget);
        int indexOfCurrentBudget = budgetList.getCurrentBudgetIndex();
        budgetList.replaceAtIndex(indexOfCurrentBudget, updateBudget(editedBudget));
        indicateModified();
    }

    /**
     * Gets the current budget's index.
     * @return -1 if there is no current budget.
     */
    public int getCurrentBudgetIndex() {
        return this.budgetList.getCurrentBudgetIndex();
    };

    /**
     * Sets the saving goal for ePiggy.
     */
    public void setGoal(Goal goal) {
        this.goal.setValue(goal);
        indicateModified();
    }

    /**
     * Get the saving goal for ePiggy.
     */
    public SimpleObjectProperty<Goal> getGoal() {
        return goal;
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
        indicateModified();
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListenerManager.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListenerManager.removeListener(listener);
    }

    /**
     * Notifies listeners that the address book has been modified.
     */
    protected void indicateModified() {
        invalidationListenerManager.callListeners(this);
    }

    //// util methods

    @Override
    public String toString() {
        return budgetList.asUnmodifiableObservableList() + " budgets";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Expense> getExpenseList() {
        return expenses.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Item> getItemList() {
        return FXCollections.unmodifiableObservableList(items);
    }

    /**
     * Gets the current budget list for ePiggy.
     */
    @Override
    public ObservableList<Budget> getBudgetList() {
        return budgetList.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && persons.equals(((AddressBook) other).persons));
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
