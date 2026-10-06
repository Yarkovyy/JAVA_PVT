package org.example;

import org.example.Models.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PublicationEditor {
    private Scanner scanner = new Scanner(System.in);
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public void edit(Publication publication) {
        if (publication == null) {
            System.out.println("Об'єкт для редагування не знайдено.");
            return;
        }

        System.out.println("Редагування видання: " + publication.getTitle());

        // Спільні поля
        editCommonFields(publication);

        if (publication instanceof Book) {
            Book book = (Book) publication;
            editBookDetails(book);
            return;
        }
        if (publication instanceof Newspaper) {
            Newspaper newspaper = (Newspaper) publication;
            editNewspaperDetails(newspaper);
            return;
        }
            Almanac almanac = (Almanac) publication;
            editAlmanacDetails(almanac);


        System.out.println("Зміни збережено.");
    }

    private void editCommonFields(Publication pub) {
        pub.setTitle(readString("Назва видання", pub.getTitle()));
        pub.setDate(readDate("Дата виходу (дд.мм.рррр)", pub.getDate()));
        editPublisher(pub.getPublisher());
    }

    private void editPublisher(Publisher publisher) {
        System.out.println("Дані про видавництво:");
        publisher.setName(readString("Назва видавництва", publisher.getName()));
        publisher.setCountry(readString("Країна видавництва", publisher.getCountry()));
        publisher.setYear(readInt("Рік заснування видавництва", publisher.getYear()));
    }

    private void editAuthor(Author author) {
        System.out.println("Дані про автора:");
        author.setName(readString("Ім'я автора", author.getName()));
        author.setCountry(readString("Країна автора", author.getCountry()));
        author.setAge(readInt("Вік автора", author.getAge()));
        author.setCountPublications(readInt("Кількість публікацій автора", author.getCountPublications()));
    }

    // Редагування книги
    private void editBookDetails(Book book) {
        System.out.println("Дані книги:");
        editAuthor(book.getAuthor());
        book.setPageCount(readInt("Кількість сторінок", book.getPageCount()));
        book.setGenre(readString("Жанр", book.getGenre()));
    }

    // Редагування газети
    private void editNewspaperDetails(Newspaper newspaper) {
        System.out.println("Дані газети:");
        newspaper.setIssueNumber(readInt("Номер випуску", newspaper.getIssueNumber()));

        boolean exit = false;
        while (!exit) {
            System.out.println("\nКерування статтями газети:");
            System.out.println("1. Переглянути статті");
            System.out.println("2. Додати статтю");
            System.out.println("3. Видалити статтю");
            System.out.println("0. Завершити редагування статей");
            System.out.print("Виберіть дію: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": {
                    printNewspaperArticles(newspaper);
                    break;
                }
                case "2": {
                    addArticleToNewspaper(newspaper);
                    break;
                }
                case "3": {
                    removeArticleFromNewspaper(newspaper);
                    break;
                }
                case "0": {
                    exit = true;
                    break;
                }
                default: {
                    System.out.println("Невірний пункт.");
                    break;
                }
            }
        }
    }

    private void printNewspaperArticles(Newspaper newspaper) {
        Map<String, Author> articles = newspaper.getArticles();
        if (articles.isEmpty()) {
            System.out.println("Статей немає.");
            return;
        }
        System.out.println("Список статей:");
        articles.forEach((title, author) -> {
            System.out.println("Стаття: " + title + " | Автор: " + author.getName());
        });
    }

    private void addArticleToNewspaper(Newspaper newspaper) {
        System.out.print("Введіть назву статті: ");
        String articleTitle = scanner.nextLine().trim();
        if (articleTitle.isEmpty()) {
            System.out.println("Назва статті не може бути порожньою.");
            return;
        }

        Author author = new Author();
        editAuthor(author);

        newspaper.getArticles().put(articleTitle, author);
        System.out.println("Статтю додано.");
    }

    private void removeArticleFromNewspaper(Newspaper newspaper) {
        System.out.print("Введіть назву статті для видалення: ");
        String articleTitle = scanner.nextLine().trim();
        if (newspaper.getArticles().remove(articleTitle) != null) {
            System.out.println("Статтю видалено.");
        } else {
            System.out.println("Статтю не знайдено.");
        }
    }

    // Редагування альманаху
    private void editAlmanacDetails(Almanac almanac) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nКерування творами в альманасі:");
            System.out.println("1. Переглянути твори");
            System.out.println("2. Редагувати твір");
            System.out.println("3. Додати новий твір");
            System.out.println("4. Видалити твір");
            System.out.println("0. Завершити редагування творів");
            System.out.print("Виберіть дію: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": {
                    printAlmanacBooks(almanac);
                    break;
                }
                case "2": {
                    editBookInAlmanac(almanac);
                    break;
                }
                case "3": {
                    addBookToAlmanac(almanac);
                    break;
                }
                case "4": {
                    removeBookFromAlmanac(almanac);
                    break;
                }
                case "0": {
                    exit = true;
                    break;
                }
                default: {
                    System.out.println("Невірний пункт.");
                    break;
                }
            }
        }
    }

    private void printAlmanacBooks(Almanac almanac) {
        List<Book> books = almanac.getBooks();
        if (books.isEmpty()) {
            System.out.println("Альманах порожній.");
            return;
        }
        for (int i = 0; i < books.size(); i++) {
            Book b = books.get(i);
            System.out.println("[" + i + "] " + b.getTitle() + " | Автор: " + b.getAuthor().getName());
        }
    }

    private void editBookInAlmanac(Almanac almanac) {
        List<Book> books = almanac.getBooks();
        if (books.isEmpty()) {
            System.out.println("В альманасі немає творів.");
            return;
        }
        printAlmanacBooks(almanac);
        int index = readInt("Введіть номер твору для редагування", -1);
        if (index >= 0 && index < books.size()) {
            Book book = books.get(index);

            edit(book);
            System.out.println("Твір оновлено.");
        } else {
            System.out.println("Невірний номер.");
        }
    }

    private void addBookToAlmanac(Almanac almanac) {
        System.out.println("Додавання твору в альманах:");
        Book newBook = new Book();
        newBook.setTitle(readString("Назва твору", "Новий твір"));
        newBook.setDate(almanac.getDate());
        newBook.setPublisher(almanac.getPublisher());
        editBookDetails(newBook);

        almanac.getBooks().add(newBook);
        System.out.println("Твір додано.");
    }

    private void removeBookFromAlmanac(Almanac almanac) {
        List<Book> books = almanac.getBooks();
        if (books.isEmpty()) {
            System.out.println("В альманасі немає творів.");
            return;
        }
        printAlmanacBooks(almanac);
        int index = readInt("Введіть номер твору для видалення", -1);
        if (index >= 0 && index < books.size()) {
            books.remove(index);
            System.out.println("Твір видалено.");
        } else {
            System.out.println("Невірний номер.");
        }
    }

    // Зчитування з консолі
    private String readString(String message, String currentValue) {
        System.out.print(message + " [" + currentValue + "]: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return currentValue;
        }
        return input;
    }

    private int readInt(String message, int currentValue) {
        while (true) {
            System.out.print(message + " [" + currentValue + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return currentValue;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Помилка: введіть ціле число.");
            }
        }
    }

    private LocalDate readDate(String message, LocalDate currentValue) {
        while (true) {
            String currentStr = currentValue != null ? currentValue.format(dateFormatter) : "";
            System.out.print(message + " [" + currentStr + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return currentValue;
            }
            try {
                return LocalDate.parse(input, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Помилка: введіть дату у форматі дд.мм.рррр");
            }
        }
    }

}
