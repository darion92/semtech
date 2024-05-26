package org.semtech.program.processor;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.semtech.program.bean.Department;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service processing the population csv file and return the output result
 */
@Service
@Slf4j
public class PopulationProcessor {

    public static final int DEPARTMENT_NAME_INDEX = 3;
    public static final int CITY_INDEX = 1;
    public static final int POPULATION_TOTAL_INDEX = 2;
    public static final int DEPARTMENT_CODE_INDEX = 0;
    public static final String LINE_SEPARATOR = ";";

    public String processPopulation(MultipartFile multipartFile) throws IOException, CsvValidationException {
        log.info("start processing the population file");
        InputStream inputStreamReader = multipartFile.getInputStream();
        CSVReader csvReader = new CSVReader(new InputStreamReader(multipartFile.getInputStream()));
        Map<String, Department> departmentMap = new HashMap<>();
        String[] line;
        csvReader.readNext();
        try {
            while ((line = csvReader.readNext()) != null) {
                String departmentCode = getValueFromCSVLine(line, DEPARTMENT_CODE_INDEX);
                if (departmentCode.isBlank()) {
                    continue;
                }
                String cityName = getValueFromCSVLine(line, CITY_INDEX);
                int populationTotal = getValueFromCSVLine(line, POPULATION_TOTAL_INDEX).isBlank() ? 0 :
                        Integer.parseInt(getValueFromCSVLine(line, POPULATION_TOTAL_INDEX));
                String departmentName = getValueFromCSVLine(line, DEPARTMENT_NAME_INDEX);
                Department departmentFromMap = departmentMap.get(departmentCode);
                if (departmentFromMap == null){
                    departmentMap.put(departmentCode, new Department(departmentName, populationTotal, Map.of(cityName, populationTotal)));
                } else {
                    departmentFromMap.setTotalPopulation(populationTotal + departmentFromMap.getTotalPopulation());
                    int populationTotalFromMap = departmentFromMap.getCityPopulationMap().values().stream().findFirst()
                            .orElse(0);
                    if (populationTotal > populationTotalFromMap) {
                        departmentFromMap.setCityPopulationMap(Map.of(cityName, populationTotal));
                    }
                }
            }
        } catch (RuntimeException e){
            log.error("the csv format is invalid");
            throw e;
        }
        inputStreamReader.close();
        csvReader.close();
        return outputResult(departmentMap);
    }

    private String outputResult(Map<String, Department> departmentMap){
        StringBuilder stringBuilder = new StringBuilder();
        AtomicInteger smallestPopulation = new AtomicInteger(Integer.MAX_VALUE);
        AtomicReference<String> departmentWithSmallestPopulation = new AtomicReference<>("");
        departmentMap.values().stream().forEach(department -> {
            stringBuilder.append("Department name : "+ department.getName() +
                    " || Total population : "+department.getTotalPopulation() +
                    " || City with largest population : " + department.getCityPopulationMap().keySet().stream().findFirst().orElse("") + "\n");
            if (department.getTotalPopulation() < smallestPopulation.get()) {
                smallestPopulation.set(department.getTotalPopulation());
                departmentWithSmallestPopulation.set(department.getName());
            }
        });
        stringBuilder.append("***********************\n");
        stringBuilder.append("Department with smallest population : "+departmentWithSmallestPopulation.get());
        log.info("The processing finished successfully");
        return stringBuilder.toString();
    }

    private String getValueFromCSVLine(String[] line, int index) {
        return line[0].split(LINE_SEPARATOR).length == 4 ? line[0].split(LINE_SEPARATOR)[index] : "";

    }
}
