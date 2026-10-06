package org.example.Models;

import lombok.Data;

@Data
public class Flight {
    private static int lastId = 0;
    private int id;
    private String destination = "unknown";
    private int count;
    private String type = "unknown";
    private int experience;
    private boolean inProcess = false;
    private boolean isCompleted = false;
    private Driver driver;
    private Car car;
    private int routeLength;
    private int weight;

    public Flight() {
        id = lastId++;
    }

    public Flight( String destination, int count, String type, int experience, boolean inProcess, Driver driver, Car car, int routeLength, int weight) {
        id = lastId++;
        this.destination = destination;
        this.count = count;
        this.type = type;
        this.experience = experience;
        this.inProcess = inProcess;
        this.driver = driver;
        this.car = car;
        this.routeLength = routeLength;
        this.weight = weight;
    }
}