package seedu.address.ui;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.epiggy.Expense;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class ExpenseCard extends UiPart<Region> {

    private static final Logger logger = LogsCenter.getLogger(ExpenseCard.class);

    private static final String FXML = "ExpenseListCard.fxml";
    private static final String[] TAG_COLOR_STYLES =
        { "turquoise", "orange", "yellow", "green", "black", "blue", "beige", "pink", "white", "grey" };

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Expense expense;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label name;
    @FXML
    private Label cost;
    @FXML
    private Label date;
    @FXML
    private FlowPane tags;

    public ExpenseCard(Expense expense, int displayedIndex) {
        super(FXML);

        this.expense = expense;
        id.setText(displayedIndex + ". ");
        name.setText(expense.getItem().getName().name);
        cost.setText(String.format("Cost: %d $", expense.getItem().getPrice().getAmount()));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
        date.setText(formatter.format(expense.getDate()));
        initialiseTags(expense);
    }

    /**
     * Returns the color style for {@code tagName}'s label.
     */
    private String getTagColorStyleFor(String tagName) {
        // generate a random color from the hash code of the tag so the color remain consistent
        // between different runs of the program while still making it random enough between tags.
        return TAG_COLOR_STYLES[Math.abs(tagName.hashCode()) % TAG_COLOR_STYLES.length];
    }

    /**
     * Creates the tag labels for {@code person}.
     */
    private void initialiseTags(Expense expense) {
        expense.getItem().getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColorStyleFor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ExpenseCard)) {
            return false;
        }

        // state check
        ExpenseCard card = (ExpenseCard) other;
        return id.getText().equals(card.id.getText())
                && expense.equals(card.expense);
    }
}
