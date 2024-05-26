package org.semtech.program.controller;

import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.semtech.program.processor.PopulationProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
public class FileUploadController {

    @Autowired
    private PopulationProcessor populationProcessor;

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile multipartFile)
            throws IOException, CsvValidationException {
        String output = populationProcessor.processPopulation(multipartFile);
        log.info(output);
        //if the output gets bigger, better to log the file.
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
