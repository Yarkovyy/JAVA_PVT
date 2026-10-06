package org.example.Models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {
    @Builder.Default
    private String name = "Driver";
    private int experience;
    private int balance;
    @Setter(AccessLevel.NONE)
    private int count;
    private boolean isFree;
    private int difficultyDriving;

    public void incrementFlights() {
        this.count++;
    }
    public void increaseBalance()
    {
        balance += experience * count * 100;
    }

    public int getRouteLength()
    {
        return experience * 150 + 200;
    }
}
