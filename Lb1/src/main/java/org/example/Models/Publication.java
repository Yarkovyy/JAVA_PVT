package org.example.Models;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class Publication {
    private String title = "Без назви";
    private LocalDate date = LocalDate.now();
    private Publisher publisher = new Publisher();

    public abstract boolean hasAuthor(String authorName);


}
