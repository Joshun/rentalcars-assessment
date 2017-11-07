package carapi;

// CarApi: provides a REST API for querying car information

import carparser.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.Request;

import java.util.ArrayList;
import java.util.Collections;

import static spark.Spark.*;

public class CarApi {

    private static Gson gson = new Gson();

    private static CarParser carParser = new CarParser();
    private static ArrayList<Car> carArrayList = carParser.carFromJson("vehicles.json");

    // Helper to generate a JSON formatted error message in format
    // { "error": "reason" }
    private static String makeError(String error) {
        JsonObject j = new JsonObject();
        j.addProperty("error", error);
        return j.toString();
    }

    // Helper for carrying out sort based on query parameter
    // Throws InvalidSortParameterException it is invalid
    private static void sortByParameterType(Request request, ArrayList<Car> carArrayList)
            throws InvalidSortParameterException {

        String sortType = request.queryParams("sort_type");
        Boolean shouldReverse = Boolean.parseBoolean(request.queryParams("reverse"));

        // sort_type parameter omitted, use default of sorting by price
        if (sortType == null) {
            carParser.sortCarsByPrice(carArrayList);
        }

        // else handle each of the sort types
        else {
            switch (sortType) {
                case "score":
                    System.out.println("sorting by score...");
                    carParser.sortCarsByScore(carArrayList);
                    break;
                case "supplier_rating":
                    System.out.println("sorting by supplier rating...");
                    carParser.sortCarsByRatingCarType(carArrayList);
                    break;
                case "price":
                    System.out.println("sorting by price...");
                    carParser.sortCarsByPrice(carArrayList);
                    break;
                // sort type is not valid
                default:
                    System.out.println("invalid sortType");
                    throw new InvalidSortParameterException("Parameter is not valid", new Throwable(sortType));
            }
        }

        // if reverse parameter present, reverse the results
        if (shouldReverse) {
            Collections.reverse(carArrayList);
        }
    }


    public static void main(String[] args) {
        get("/", (req, res) ->
                "This server is for making requests to the RentalCars API. See docs for details.");

        // endpoint to retrieve all cars
        get("/cars", (req, res) -> {
            try {
                sortByParameterType(req, carArrayList);
                return gson.toJson(carArrayList);
            }
            catch (InvalidSortParameterException e) {
                return makeError("Invalid parameter type " + e.getCause().toString());
            }
        });

        // endpoint to retrieve car by index
        // provides more detailed information
        get("/cars/:car", (req, res) -> {
            try {
                sortByParameterType(req, carArrayList);

                int carIndex = Integer.parseInt(req.params(":car"));
                if (carIndex >= carArrayList.size()) {
                    return makeError("index too big");
                }
                else {
                    Car car = carArrayList.get(carIndex);
                    Sipp sipp = car.getSipp();
                    int score = car.getScore();
                    float scoreSum = car.getScoreSum();

                    // pack values of car into JSON object

                    JsonObject jsonCar = gson.toJsonTree(car).getAsJsonObject();
                    jsonCar.addProperty("score", score);
                    jsonCar.addProperty("summed_score", scoreSum);
                    jsonCar.addProperty("sipp", sipp.toString());

                    JsonObject sippDetail = new JsonObject();
                    sippDetail.addProperty("car_type", sipp.getCarType());
                    sippDetail.addProperty("car_type_doors", sipp.getDoorType());
                    sippDetail.addProperty("transmission", sipp.getTransmission());
                    sippDetail.addProperty("fuel", sipp.getFuel());
                    sippDetail.addProperty("aircon", sipp.getAircon());
                    jsonCar.add("sipp_detail", sippDetail);

                    return jsonCar.toString();
                }

            }

            // handle invalid car index
            catch (java.lang.NumberFormatException e) {
                return makeError("car index \"" + e.getCause() + "\" is not valid");
            }

            // handle invalid sort parameter
            catch (InvalidSortParameterException e) {
                return makeError("parameter \"" + e.getCause().toString() + "\" is not valid");
            }

            // handle invalid sipp code
            catch (InvalidSippException e) {
                return makeError("car sipp \"" + e.getCause() + "\" is invalid");
            }

        });
    }

}
