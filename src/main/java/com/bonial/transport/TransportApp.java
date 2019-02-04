package com.bonial.transport;

import java.io.File;
import org.codehaus.jackson.*;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.json.JSONObject;

public class TransportApp {

    private static int planes = 0;
    private static int trains = 0;
    private static int cars = 0;
    private static int distinctCars = 0;
    private static int distinctPlanes = 0;
    private static int distinctTrains = 0;
    
    public static void main(String[] args) throws Exception{
        
        JsonFactory f = new MappingJsonFactory();
        JsonParser jp = f.createJsonParser(new File(args[0]));
        
        JsonToken current;

        current = jp.nextToken();
        if (current != JsonToken.START_OBJECT) {
            System.out.println("Error: root should be object: quiting.");
            return;
        }
        long startTime = System.currentTimeMillis();
        int records = 0;
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jp.getCurrentName();
            current = jp.nextToken();
            if (fieldName.equals("transports")) {
                if (current == JsonToken.START_ARRAY) {
                    while (jp.nextToken() != JsonToken.END_ARRAY) {
                        records++;
                        JsonNode node = jp.readValueAsTree();
                        checkForPlane(node);
                        checkForCar(node);
                        checkForTrain(node);
                    }
                } else {
                    System.out.println("Error: records should be an array: skipping.");
                    jp.skipChildren();
                }
            } else {
                System.out.println("Unprocessed property: " + fieldName);
                jp.skipChildren();
            }
        }
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(createJson().toString());
        System.out.println("Time elapsed: " + estimatedTime + " milliseconds\nRecords: " + records);
    }
    
    /**
     * Check if there is a plane in the JSON node and updates respective counters.
     * b-passenger-capacity is a distinct value for planes
     * 
     * @param node 
     */
    private static void checkForPlane(JsonNode node) {
        if(node.findValue("b-passenger-capacity") !=null){
            distinctPlanes += 1;
            int bPassengerCapacity = Integer.parseInt(node.get("b-passenger-capacity").getValueAsText());
            int ePassengerCapacity = Integer.parseInt(node.get("e-passenger-capacity").getValueAsText());
            planes = planes + bPassengerCapacity + ePassengerCapacity;
        }
    }

    /**
     * Check if there is a car in the JSON node updates respective counters.
     * passenger-capacity is a distinct value for cars
     * 
     * @param node 
     */
    private static void checkForCar(JsonNode node) {
        if(node.findValue("passenger-capacity") != null){
            distinctCars += 1;
            cars += Integer.parseInt(node.get("passenger-capacity").getValueAsText());
        }
    } 

    /**
     * Check if there is a train in the JSON node and updates respective counters.
     * w-passenger-capacity is a distinct value for planes
     * 
     * @param node 
     */
    private static void checkForTrain(JsonNode node) {
        if(node.findValue("w-passenger-capacity") != null){
            distinctTrains += 1;
            int numberWagons = Integer.parseInt(node.get("number-wagons").getValueAsText());
            int wPassengerCapacity = Integer.parseInt(node.get("w-passenger-capacity").getValueAsText());
            trains += (numberWagons*wPassengerCapacity);
        }
    }
    
    /**
     * Creates the input JSON object 
     * @return 
     */
    private static JSONObject createJson(){
        JSONObject json = new JSONObject();
        json.put("planes", Integer.toString(planes));
        json.put("trains", Integer.toString(trains));
        json.put("cars", Integer.toString(cars));
        json.put("distinct-cars", Integer.toString(distinctCars));
        json.put("distinct-planes", Integer.toString(distinctPlanes));
        json.put("distinct-trains", Integer.toString(distinctTrains));
        return json;
    }
    
}
