package org.example.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Newspaper extends Publication {
    private int issueNumber = 1;
    // Стаття, Автор
    private Map<String, Author> articles = new HashMap<>();

    public Newspaper(String title, LocalDate date, Publisher publisher, int issueNumber, Map<String, Author> articles) {
        super(title, date, publisher);
        this.issueNumber = issueNumber;
        this.articles = articles != null ? articles : new HashMap<>();
    }

    @Override
    public boolean hasAuthor(String authorName) {
        if (authorName == null || authorName.isBlank() || articles == null) {
            return false;
        }

        String search = authorName.trim().toLowerCase();

        return articles.values().stream()
                .anyMatch(author -> author != null
                        && author.getName().toLowerCase().contains(search));
    }
}
