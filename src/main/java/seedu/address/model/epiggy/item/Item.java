package seedu.address.model.epiggy.item;

import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents an Item in the expense book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Item {
    private final Name name;
    private final Price price;
    private final Set<Tag> tags;

    public Item(Name name, Price price, Set<Tag> tags) {
        this.name = name;
        this.price = price;
        this.tags = tags;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Set<Tag> getTags() {
        return tags;
    }
}
