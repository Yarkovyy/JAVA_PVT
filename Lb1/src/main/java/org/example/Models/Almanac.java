package org.example.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Almanac extends Publication {
    private List<Book> books = new ArrayList<>();

    public Almanac(String title, LocalDate date, Publisher publisher, List<Book> books) {
        super(title, date, publisher);
        this.books = books != null ? new ArrayList<>(books) : new ArrayList<>();
    }

    @Override
    public boolean hasAuthor(String authorName) {
        if (authorName == null || authorName.isBlank() || books == null) {
            return false;
        }
        return books.stream().anyMatch(book -> book.hasAuthor(authorName));
    }
}
