package seedu.address.logic.parser.epiggy;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.*;

import java.util.Date;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.epiggy.AddExpenseCommand;
import seedu.address.logic.parser.*;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.epiggy.Expense;
import seedu.address.model.epiggy.item.Item;
import seedu.address.model.epiggy.item.Name;
import seedu.address.model.epiggy.item.Price;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddExpenseCommand object
 */
public class AddExpenseCommandParser implements Parser<AddExpenseCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddExpenseCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddExpenseCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_COST, PREFIX_DATE, PREFIX_TAG);
        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_COST, PREFIX_DATE, PREFIX_TAG)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddExpenseCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseItemName(argMultimap.getValue(PREFIX_NAME).get());
        Price cost = ParserUtil.parseCost(argMultimap.getValue(PREFIX_COST).get());
        Date date = new Date(); //TODO
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Item item = new Item(name, cost, tagList);
        Expense expense = new Expense(item, date);

        return new AddExpenseCommand(expense);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
