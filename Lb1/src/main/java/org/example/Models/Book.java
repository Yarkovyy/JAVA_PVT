package org.example.Models;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Book extends Publication {
    private Author author = new Author();
    private int pageCount = 0;
    private String genre = "Не вказано";

    public Book(String title, LocalDate date, Publisher publisher, Author author, int pageCount, String genre) {
        super(title, date, publisher);
        this.author = author;
        this.pageCount = pageCount;
        this.genre = genre;
    }


    @Override
    public boolean hasAuthor(String authorName) {
        if (authorName == null || authorName.isBlank() || author == null) {
            return false;
        }

        return author.getName().toLowerCase().contains(authorName.toLowerCase());
    }
}
