package org.example;
import org.example.Models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MenuLibrary {
    private Library library = new Library();
    private PublicationEditor editor = new PublicationEditor();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": {
                    library.initTestData();
                    System.out.println("Тестові дані успішно завантажено.");
                    break;
                }
                case "2": {
                    showAllPublications();
                    break;
                }
                case "3": {
                    showGroupedPublications();
                    break;
                }
                case "4": {
                    addSpecificPublication();
                    break;
                }
                case "5": {
                    Publication randomPub = library.addRandomPublication();
                    System.out.println("Випадковий об'єкт створено і додано: " + randomPub.getTitle());
                    break;
                }
                case "6": {
                    editPublication();
                    break;
                }
                case "7": {
                    deletePublication();
                    break;
                }
                case "8": {
                    searchMenu();
                    break;
                }
                case "0": {
                    running = false;
                    System.out.println("Роботу програми завершено.");
                    break;
                }
                default: {
                    System.out.println("Невірний пункт меню. Спробуйте ще раз.");
                    break;
                }
            }
            System.out.println();
        }
    }

    private void printMainMenu() {
        System.out.println("Головне меню:");
        System.out.println("1. Завантажити тестові дані");
        System.out.println("2. Вивести весь каталог");
        System.out.println("3. Вивести каталог (групування за типом)");
        System.out.println("4. Додати видання конкретного типу");
        System.out.println("5. Додати видання випадкового типу");
        System.out.println("6. Редагувати видання");
        System.out.println("7. Видалити видання");
        System.out.println("8. Пошук у каталозі");
        System.out.println("0. Вихід");
        System.out.print("Оберіть дію: ");
    }

    private void showAllPublications() {
        List<Publication> list = library.getAll();
        if (list.isEmpty()) {
            System.out.println("Каталог порожній.");
            return;
        }

        System.out.println("Список усіх видань:");
        for (int i = 0; i < list.size(); i++) {
            System.out.println("[" + i + "] " + list.get(i));
        }
    }

    private void showGroupedPublications() {
        List<Publication> all = library.getAll();
        if (all.isEmpty()) {
            System.out.println("Каталог порожній.");
            return;
        }

        all.stream()
                .collect(Collectors.groupingBy(Publication::getClass))
                .forEach((type, items) -> {
                    System.out.println(type.getSimpleName() + " (" + items.size() + "):");
                    for (Publication item : items) {
                        System.out.println("  " + item);
                    }
                });
    }

    private void addSpecificPublication() {
        System.out.println("Оберіть тип для створення:");
        System.out.println("1. Книга");
        System.out.println("2. Газета");
        System.out.println("3. Альманах");
        System.out.print("Ваш вибір: ");

        String typeChoice = scanner.nextLine().trim();
        Publication newPub = null;

        switch (typeChoice) {
            case "1": {
                newPub = new Book();
                break;
            }
            case "2": {
                newPub = new Newspaper();
                break;
            }
            case "3": {
                newPub = new Almanac();
                break;
            }
            default: {
                System.out.println("Невідомий тип.");
                return;
            }
        }

        System.out.println("Заповніть інформацію для нового видання:");
        editor.edit(newPub);

        library.addPublication(newPub);
        System.out.println("Видання успішно додано до каталогу.");
    }

    // Редагування
    private void editPublication() {
        List<Publication> list = library.getAll();
        if (list.isEmpty()) {
            System.out.println("Каталог порожній.");
            return;
        }

        showAllPublications();
        System.out.print("Введіть номер видання для редагування: ");
        int index = readInt();

        if (index >= 0 && index < list.size()) {
            editor.edit(list.get(index));
        } else {
            System.out.println("Невірний номер видання.");
        }
    }

    private void deletePublication() {
        List<Publication> list = library.getAll();
        if (list.isEmpty()) {
            System.out.println("Каталог порожній.");
            return;
        }

        showAllPublications();
        System.out.print("Введіть номер видання для видалення: ");
        int index = readInt();

        if (library.removeByIndex(index)) {
            System.out.println("Видання успішно видалено.");
        } else {
            System.out.println("Помилка: невірний номер видання.");
        }
    }

    private void searchMenu() {
        System.out.println("Пошук видань:");
        System.out.println("1. За назвою");
        System.out.println("2. За роком випуску");
        System.out.println("3. За видавництвом");
        System.out.println("4. За автором");
        System.out.print("Оберіть критерій: ");

        String searchChoice = scanner.nextLine().trim();
        List<Publication> results = new ArrayList<>();

        switch (searchChoice) {
            case "1": {
                System.out.print("Введіть назву (або частину назви): ");
                String title = scanner.nextLine().trim();
                results = library.findByTitle(title);
                break;
            }
            case "2": {
                System.out.print("Введіть рік випуску (наприклад, 2024): ");
                int year = readInt();
                results = library.findByYear(year);
                break;
            }
            case "3": {
                System.out.print("Введіть назву видавництва: ");
                String publisher = scanner.nextLine().trim();
                results = library.findByPublisher(publisher);
                break;
            }
            case "4": {
                System.out.print("Введіть ім'я автора: ");
                String author = scanner.nextLine().trim();
                results = library.findByAuthor(author);
                break;
            }
            default: {
                System.out.println("Невірний критерій пошуку.");
                return;
            }
        }

        printSearchResults(results);
    }

    private void printSearchResults(List<Publication> results) {
        if (results.isEmpty()) {
            System.out.println("Нічого не знайдено.");
            return;
        }

        System.out.println("Результати пошуку (знайдено: " + results.size() + "):");
        for (int i = 0; i < results.size(); i++) {
            System.out.println("[" + i + "] " + results.get(i));
        }
    }

    private int readInt() {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Помилка. Введіть ціле число: ");
            }
        }
    }
}
