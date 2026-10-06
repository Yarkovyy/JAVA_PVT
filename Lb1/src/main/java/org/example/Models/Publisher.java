package org.example.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publisher {
    private String name = "Невідоме видавництво";
    private String country = "Невідома країна";
    private int year = 1900;
}
