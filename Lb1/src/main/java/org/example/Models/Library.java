package org.example.Models;

import java.time.LocalDate;
import java.util.*;

public class Library {
    private String name = "Бібліотека";
    private List<Publication> publications = new ArrayList<>();
    private Random random = new Random();

    public void initTestData() {
        Publisher pub1 = new Publisher("А-БА-БА-ГА-ЛА-МА-ГА", "Україна", 1992);
        Publisher pub2 = new Publisher("Видавництво Старого Лева", "Україна", 2001);

        Author author1 = new Author("Тарас Шевченко", "Україна", 47, 10);
        Author author2 = new Author("Сергій Жадан", "Україна", 49, 15);

        Book book1 = new Book("Кобзар", LocalDate.of(1840, 4, 18), pub1, author1, 288, "Поезія");
        Book book2 = new Book("Інтернат", LocalDate.of(2017, 8, 20), pub2, author2, 336, "Роман");

        Map<String, Author> articles = new HashMap<>();
        articles.put("Хроніка подій", author2);
        Newspaper newspaper = new Newspaper("Літературна газета", LocalDate.of(2024, 2, 1), pub1, 42, articles);

        Almanac almanac = new Almanac("Сучасна поезія", LocalDate.of(2023, 10, 10), pub2, List.of(book1, book2));

        addPublication(book1);
        addPublication(book2);
        addPublication(newspaper);
        addPublication(almanac);
    }

    public boolean addPublication(Publication publication) {
        if (publication == null) {
            return false;
        }
        return publications.add(publication);
    }

    public Publication addRandomPublication() {
        int type = random.nextInt(3);
        Publication publication = switch (type) {
            case 0 -> new Book("Випадкова книга", LocalDate.now(), new Publisher(), new Author(), 100, "Фантастика");
            case 1 -> new Newspaper("Випадкова газета", LocalDate.now(), new Publisher(), 1, new HashMap<>());
            default -> new Almanac("Випадковий альманах", LocalDate.now(), new Publisher(), new ArrayList<>());
        };
        addPublication(publication);
        return publication;
    }

    public boolean removeByIndex(int index) {
        if (index >= 0 && index < publications.size()) {
            publications.remove(index);
            return true;
        }
        return false;
    }
    public List<Publication> getAll() {
        return publications;
    }
    public List<Publication> findByTitle(String title) {
        if (title == null || title.isBlank()) return List.of();
        String search = title.trim().toLowerCase();
        return publications.stream()
                .filter(p -> p.getTitle() != null && p.getTitle().toLowerCase().contains(search))
                .toList();
    }

    public List<Publication> findByYear(int year) {
        return publications.stream()
                .filter(p -> p.getDate() != null && p.getDate().getYear() == year)
                .toList();
    }

    public List<Publication> findByPublisher(String publisherName) {
        if (publisherName == null || publisherName.isBlank()) return List.of();
        String search = publisherName.trim().toLowerCase();
        return publications.stream()
                .filter(p -> p.getPublisher() != null
                        && p.getPublisher().getName() != null
                        && p.getPublisher().getName().toLowerCase().contains(search))
                .toList();
    }

    public List<Publication> findByAuthor(String authorName) {
        return publications.stream()
                .filter(p -> p.hasAuthor(authorName))
                .toList();
    }
}
