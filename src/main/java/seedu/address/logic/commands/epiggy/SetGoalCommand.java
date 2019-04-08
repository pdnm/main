package seedu.address.logic.commands.epiggy;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.expense.Goal;

/**
 * Set goal amount and name.
 */
public class SetGoalCommand extends Command {
    public static final String COMMAND_WORD = "setGoal";
    public static final String COMMAND_ALIAS = "sg";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets a goal that you would like to reach. "
            + "Parameters: "
            + CliSyntax.PREFIX_NAME + "GOAL NAME "
            + CliSyntax.PREFIX_COST + "GOAL AMOUNT\n"
            + "Example: " + COMMAND_WORD + " "
            + CliSyntax.PREFIX_NAME + "Nike shoe "
            + CliSyntax.PREFIX_COST + "79";
    public static final String MESSAGE_SUCCESS = "Goal set: %1$s";

    private final Goal toSet;

    public SetGoalCommand(Goal toSet) {
        requireNonNull(toSet);
        this.toSet = toSet;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        model.setGoal(toSet);
        model.commitEPiggy();
        return new CommandResult(String.format(MESSAGE_SUCCESS, toSet));
    }
}
