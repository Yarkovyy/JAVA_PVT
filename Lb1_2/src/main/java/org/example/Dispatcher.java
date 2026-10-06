package org.example;

import lombok.NonNull;
import org.example.Models.*;

import java.util.*;

public class Dispatcher {
    private Map<String, Integer> destinationPlaces = new HashMap<String, Integer>();

    private List<Flight> flights = new ArrayList<>();
    private List<Driver> drivers = new ArrayList<>();
    private List<Car> cars = new ArrayList<>();
    private List<Flight> completedFlights = new ArrayList<>();

    public Dispatcher() {
    }

    public Dispatcher(List<Flight> flights, List<Driver> drivers, List<Car> cars) {
        this.drivers = drivers;
        this.cars = cars;
        this.flights = flights;
    }

    public void addDriver(@NonNull Driver driver) {
        drivers.add(driver);
        System.out.println("До системи додано водія: " + driver.getName() + " (Досвід: " + driver.getExperience() + " років)");
    }

    public void addCar(@NonNull Car car) {
        cars.add(car);
        System.out.println("До системи додано автомобіль: " + car.getModelName() + " (Вага: " + car.getWeight() + ")");
    }

    public void addFlight(@NonNull Flight flight) {
        flights.add(flight);
        System.out.println("До системи додано новий рейс: " + "(Номер: " + flight.getId() + ")");
//        destinationPlaces.merge(flight.getDestination(), 1, Integer::sum);
    }

    public void setDriverAndCarToFlights() {
        for (Flight flight : flights) {

            if(flight.isCompleted() || flight.isInProcess() || (flight.getCar() != null && flight.getDriver() != null))
                continue;

            Car suitableCar = flight.getCar();
            if(suitableCar == null) {
                suitableCar = cars.stream()
                        .filter(Car::isFree)
                        .filter(car -> !car.isBroken())
                        .filter(c -> c.getWeight() >= flight.getWeight())
                        .min(Comparator.comparingInt(Car::getWeight)).orElse(null);
            }

                if (suitableCar != null) {
                    Car newCar = suitableCar;

                    Driver suitableDriver = flight.getDriver();
                    if (suitableDriver == null) {
                        suitableDriver = drivers.stream()
                                .filter(Driver::isFree)
                                .filter(driver -> driver.getExperience() >= flight.getExperience())
                                .filter(driver -> driver.getRouteLength() >= flight.getRouteLength())
                                .filter(driver -> driver.getDifficultyDriving() >= newCar.getDifficultyDriving())
                                .findAny().orElse(null);
                    }

                    if (suitableDriver != null) {
                        flight.setDriver(suitableDriver);
                        flight.setCar(suitableCar);
                        suitableDriver.setFree(false);
                        suitableCar.setFree(false);
                        System.out.println(String.format("Рейсу №%d призначено авто [%s] та водія [%s]",
                                flight.getId(), suitableCar.getModelName(), suitableDriver.getName()));
                    }
                }


            }
        }


    public void startFlights() {
        setDriverAndCarToFlights();
        flights.stream()
                .filter(flight -> !flight.isInProcess())
                .filter(flight -> flight.getDriver() != null && flight.getCar() != null)
                .forEach(flight -> {
                    flight.setInProcess(true);
                    System.out.println(String.format("Рейс №%d успішно розпочато. Водій [%s] на авто [%s] вирушив у дорогу.",
                            flight.getId(), flight.getDriver().getName(), flight.getCar().getModelName()));
                });
    }

    public void finishFlight(int id) {
        Flight f = getFlightById(id);
        if (f == null || !f.isInProcess()) {
            System.out.println("Спроба завершити рейс №" + id + " провальна: рейс не знайдено або він не активний.");
            return;
        }

        if(f.getCar().isBroken())
        {
            System.out.println("Спроба завершити рейс №" + id + " провальна: автомобіль зламаний.");
            return;
        }

        Driver driver = f.getDriver();
        driver.incrementFlights();
        driver.increaseBalance();
        driver.setFree(true);

        f.getCar().setFree(true);

        System.out.println(String.format("Рейс №%d успішно завершено водієм [%s].", f.getId(), driver.getName()));
        f.setInProcess(false);
        f.setCompleted(true);
        destinationPlaces.merge(f.getDestination(), 1, Integer::sum);
        completedFlights.add(f);
        flights.remove(f);
    }

    public Flight getFlightById(int id) {
        return flights.stream().filter(flight -> flight.getId() == id).findAny().orElse(null);
    }

    public void setBreakdown(int id) {
        Flight f = getFlightById(id);
        if (f == null || !f.isInProcess() || f.getCar() == null) {
            System.out.println("Неможливо зафіксувати поломку: рейс відсутній або не виконується.");
            return;
        }
        System.out.println(String.format("На рейсі №%d зламалося авто [%s]!", f.getId(), f.getCar().getModelName()));
        f.getCar().setBroken(true);
    }

    public void repairBreakdown(int id) {
        Flight f = getFlightById(id);
        if (f == null || !f.isInProcess()) {
            System.out.println("Неможливо виконати ремонт: рейс відсутній або не виконується.");
            return;
        }
        Car car = f.getCar();
        Driver driver = f.getDriver();
        if (car == null || !car.isBroken()) {
            System.out.println("Неможливо виконати ремонт: автомобіль відсутній або не зламаний.");
            return;
        }
        car.setBroken(false);
        driver.setBalance(driver.getBalance() - car.getRepairCost());
        System.out.println(String.format("Авто [%s] на рейсі №%d успішно відремонтовано. З балансу водія [%s] списано кошти.",
                car.getModelName(), f.getId(), driver.getName()));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Відомості про автобазу:\n");
        sb.append("Активні рейси:\n");
        if (flights.isEmpty())
            sb.append(" Відсутні\n");
        else {
            flights.forEach(flight -> sb.append(flight).append("\n"));
        }

        sb.append("Завершені рейси:\n");
        if (completedFlights.isEmpty()){
            sb.append(" Відсутні\n");
        }
        else{
            completedFlights.forEach(flight -> sb.append(flight).append("\n"));
        }

        sb.append("Водії:\n");
        if (drivers.isEmpty())
            sb.append(" Відсутні\n");
        else {
            drivers.forEach(driver -> sb.append(driver).append("\n"));
        }

        sb.append("Автомобілі:\n");
        if (cars.isEmpty())
            sb.append(" Відсутні\n");
        else {
            cars.forEach(car -> sb.append(car).append("\n"));
        }

        sb.append(destinationPlacesToString());
        sb.append(driverStatistics());
        sb.append(highestEarningDriverInfo());

        return sb.toString();
    }


    public String destinationPlacesToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nСтатистика поїздок за напрямками:\n");
        if (destinationPlaces.isEmpty()) {
            sb.append(" Дані відсутні\n");
        } else {
            destinationPlaces.forEach((key, value) -> sb.append(key).append(": ").append(value).append("\n"));
        }
        return sb.toString();
    }

    public String driverStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("Статистика водіїв:\n");
        if (drivers.isEmpty())
            sb.append("Водії відсутні\n");
        else {
            drivers.forEach(driver -> sb.append(driver.getName()).append(" : ").append(driver.getCount()).append("\n"));
        }
        return sb.toString();
    }

    public String highestEarningDriverInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Водій з найбільшим заробітком:\n");

        Driver driver = drivers.stream().max(Comparator.comparing(Driver::getBalance)).orElse(null);
        if (driver != null)
            sb.append(driver.getName()).append(" із заробітком ").append(driver.getBalance()).append("\n");
        else
            sb.append("Водії відсутні");
        return sb.toString();
    }
}
