package org.calc;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Calculator {
    private final HashMap<String, Long> executionTime = new HashMap<>();

    public void processCalculation(JSONObject json) {
        if (json == null) {
            System.out.println("JSON object is null.");
            return;
        }

        if (json.has("events")) {
            processEvents(json.getJSONArray("events"));
        }
    }

    private void processEvents(JSONArray events) {
        List<JSONObject> eventList = convertToList(events);
        eventList.sort(Comparator.comparingLong(event -> event.getLong("timestamp")));

        HashMap<String, Long> workLoadList = new HashMap<>();
        for (JSONObject event : eventList) {
            handleEvent(event, workLoadList);
        }
    }

    private List<JSONObject> convertToList(JSONArray events) {
        List<JSONObject> eventList = new ArrayList<>();
        for (int i = 0; i < events.length(); i++) {
            eventList.add(events.getJSONObject(i));
        }
        return eventList;
    }

    private void handleEvent(JSONObject event, HashMap<String, Long> workLoadList) {
        String customerId = event.getString("customerId");
        String workloadId = event.getString("workloadId");
        String eventType = event.getString("eventType");
        Long timestamp = event.getLong("timestamp");

        if ("start".equals(eventType) && !workLoadList.containsKey(workloadId)) {
            workLoadList.put(workloadId, timestamp);
        } else if ("stop".equals(eventType) && workLoadList.containsKey(workloadId)) {
            calculateExecutionTime(customerId, workloadId, timestamp, workLoadList);
        }
    }

    private void calculateExecutionTime(String customerId, String workloadId, Long timestamp, HashMap<String, Long> workLoadList) {
        Long startTimestamp = workLoadList.get(workloadId);
        Long duration = timestamp - startTimestamp;

        executionTime.merge(customerId, duration, Long::sum);
    }

    public HashMap<String, Long> getExecutionTime() {
        return executionTime;
    }
}
