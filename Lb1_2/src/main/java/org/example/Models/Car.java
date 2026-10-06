package org.example.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
    @Builder.Default
    private int weight = 500;
    @Builder.Default
    private boolean isBroken = false;
    @Builder.Default
    private String modelName = "unknown";
    private boolean isFree;
    private int difficultyDriving;
    @Builder.Default
    private int repairCost = 500;
}
