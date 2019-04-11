package seedu.address.testutil.epiggy;

import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.logic.commands.epiggy.AddExpenseCommand;
import seedu.address.logic.commands.epiggy.EditExpenseCommand.EditExpenseDescriptor;
import seedu.address.model.epiggy.Expense;
import seedu.address.model.tag.Tag;

/**
 * A utility class for Expense.
 */
public class ExpenseUtil {

    /**
     * Returns an add command string for adding the {@code expense}.
     */
    public static String getAddExpenseCommand(Expense expense) {
        return AddExpenseCommand.COMMAND_WORD + " " + getExpenseDetails(expense);
    }

    public static String getAddExpenseCommandAlias(Expense expense) {
        return AddExpenseCommand.COMMAND_ALIAS + " " + getExpenseDetails(expense);
    }

    /**
     * Returns the part of command string for the given {@code expense}'s details.
     */
    public static String getExpenseDetails(Expense expense) {
        String details = "";
        details += descExpenseName(expense) + " ";
        details += descExpenseCost(expense) + " ";
        details += descExpenseDate(expense) + " ";
        details += descExpenseTags(expense);
        return details;
    }

    /**
     * Return the command's name parameter for expense.
     */
    public static String descExpenseName(Expense expense) {
        return PREFIX_NAME + expense.getItem().getName().name;
    }

    /**
     * Return the command's cost parameter for expense.
     */
    public static String descExpenseCost(Expense expense) {
        return String.format("%s%.2f", PREFIX_COST, expense.getItem().getCost().getAmount());
    }


    /**
     * Return the command's date parameter for expense.
     */
    public static String descExpenseDate(Expense expense) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return PREFIX_DATE + formatter.format(expense.getDate());
    }

    /**
     * Return the command's date parameter for expense.
     */
    public static String descExpenseTags(Expense expense) {
        return expense.getItem().getTags().stream()
                .map(tag -> PREFIX_TAG + tag.tagName)
                .collect(Collectors.joining(" "));
    }

    /**
     * Returns the part of command string for the given {@code EditExpenseDescriptor}'s details.
     */
    public static String getEditExpenseDescriptorDetails(EditExpenseDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name).append(" "));
        descriptor.getCost().ifPresent(cost -> sb.append(PREFIX_COST).append(cost.toString()).append(" "));
        descriptor.getDate().ifPresent(date -> sb.append(PREFIX_DATE).append(date.toString()).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
