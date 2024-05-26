package org.semtech.program.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Department {
    private String name;
    private int totalPopulation;
    private Map<String, Integer> cityPopulationMap;
}
