package com.project.shahbazi.spring_multiThread_pro.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ErrorWriter {

    private static final String ERROR_FILE_PATH = "errors.json";
    private final List<ErrorRecord> errorRecords = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Method to add an error record
    public void writeErrorRecord(ErrorRecord errorRecord) {
        log.debug("adding error record: {}", errorRecord);
        errorRecords.add(errorRecord);
    }

    // Method to save all errors to the JSON file after batch processing is complete
    public void saveErrors() throws Exception {
        if (!errorRecords.isEmpty()) {
            log.debug("Writing error record: {}", errorRecords);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // enabling pretty printing
            objectMapper.writeValue(new File(ERROR_FILE_PATH), errorRecords);
        }
    }
}

