package org.example;

import org.example.Models.*;

public class Main {
    static void main() {
        Dispatcher dispatcher = new Dispatcher();

        dispatcher.addCar(Car.builder().modelName("Volvo").weight(5000).isFree(true).difficultyDriving(3).repairCost(700).build());
        dispatcher.addCar(Car.builder().modelName("Газель").weight(2000).isFree(true).difficultyDriving(1).repairCost(300).build());

        dispatcher.addDriver(Driver.builder().name("Олег").experience(5).isFree(true).difficultyDriving(4).balance(1500).build());
        dispatcher.addDriver(Driver.builder().name("Іван").experience(2).isFree(true).difficultyDriving(2).balance(800).build());

        System.out.println("\nСтворення та реєстрація рейсів");
        Flight flight1 = new Flight("Київ", 15, "Товари", 3, false, null, null, 500, 4500);
        Flight flight2 = new Flight("Львів", 5, "Продукти", 1, false, null, null, 300, 1500);

        dispatcher.addFlight(flight1);
        dispatcher.addFlight(flight2);

        System.out.println("\nСтарт рейсів");
        dispatcher.startFlights();

        System.out.println("\nПоломка та ремонт");
        dispatcher.setBreakdown(flight1.getId());

        // Завершення з поломкою
        dispatcher.finishFlight(flight1.getId());

        // Ремонт автомобіля
        dispatcher.repairBreakdown(flight1.getId());

        System.out.println("\nЗавершення рейсів");
        dispatcher.finishFlight(flight1.getId());
        dispatcher.finishFlight(flight2.getId());

        System.out.println("\nСтатистика автобази");
        System.out.println(dispatcher.toString());
    }
}
