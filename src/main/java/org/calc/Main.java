package org.calc;

import org.json.JSONObject;

import java.util.HashMap;

public class Main {

    private static final String DATASET_URL = "http://localhost:8080/v1/dataset";
    private static final String RESULT_URL = "http://localhost:8080/v1/result";

    public static void main(String[] args) {
        Main mainApp = new Main();
        mainApp.run();
    }

    private void run() {
        try {
            Reader reader = new Reader();
            Calculator calculator = new Calculator();
            Sender sender = new Sender();

            processData(reader, calculator, sender);
        } catch (Exception e) {
            handleError(e);
        }
    }

    private void processData(Reader reader, Calculator calculator, Sender sender) throws Exception {
        reader.read();
        JSONObject jsonObjects = reader.getJsonObject();

        calculator.processCalculation(jsonObjects);
        HashMap<String, Long> executionList = calculator.getExecutionTime();

        sender.processTransmition(executionList);
    }

    private void handleError(Exception e) {
        e.printStackTrace();
    }
}