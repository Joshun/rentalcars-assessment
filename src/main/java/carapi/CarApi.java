package carapi;

import carparser.Car;
import carparser.CarParser;
import carparser.Sipp;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import spark.Request;

import java.util.ArrayList;
import java.util.Collections;

import static spark.Spark.*;

public class CarApi {

    private static Gson gson = new Gson();

    private static CarParser carParser = new CarParser();
    private static ArrayList<Car> carArrayList = carParser.carFromJson("vehicles.json");

    private static String makeError(String error) {
        JsonObject j = new JsonObject();
        j.addProperty("error", error);
        return j.toString();
    }

    private static void sortByParameterType(Request request, ArrayList<Car> carArrayList) {
        String sortType = request.queryParams("sort_type");
        Boolean shouldReverse = Boolean.parseBoolean(request.queryParams("reverse"));

        if (sortType == null) {
            carParser.sortCarsByPrice(carArrayList);
        }

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
                default:
                    System.out.println("sorting by price...");
                    carParser.sortCarsByPrice(carArrayList);
            }
        }

        if (shouldReverse) {
            Collections.reverse(carArrayList);
        }
    }


    public static void main(String[] args) {
        get("/", (req, res) -> "hello");

        get("/cars", (req, res) -> {
            sortByParameterType(req, carArrayList);
            return gson.toJson(carArrayList);
        });

        get("/cars/:car", (req, res) -> {
            sortByParameterType(req, carArrayList);

            try {
                int carIndex = Integer.parseInt(req.params(":car"));
                if (carIndex >= carArrayList.size()) {
                    return makeError("index too big");
                }
                else {
                    Car car = carArrayList.get(carIndex);
                    Sipp sipp = car.getSipp();
                    int score = car.getScore();
                    float scoreSum = car.getScoreSum();

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
            catch (java.lang.NumberFormatException e) {
                return makeError("parameter invalid");
            }
        });
    }

}
