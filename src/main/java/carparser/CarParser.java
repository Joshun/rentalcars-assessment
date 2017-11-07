package carparser;

// CarParser: parses cars JSON file into car objects, when run displays them on console

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CarParser {
    private Gson gson = new Gson();

    public ArrayList<Car> carFromJson(String jsonFile) {

        try {
            JsonReader reader = new JsonReader(new FileReader(jsonFile));

            JsonCars jsonCars = gson.fromJson(reader, JsonCars.class);

            ArrayList<Car> cars = jsonCars.getVehicleList();

            return cars;
        }

        catch (java.io.FileNotFoundException e) {
            System.err.println(e);
            return null;
        }
    }

    public void sortCarsByPrice(ArrayList<Car> cars) {

        Collections.sort(cars, new Comparator<Car>() {
            public int compare(Car car, Car t1) {
                if (car.getPrice() > t1.getPrice()) {
                    return 1;
                }
                else if (car.getPrice() < t1.getPrice()) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        });

    }

    public void sortCarsByRatingCarType(ArrayList<Car> cars) {
        Collections.sort(cars, new Comparator<Car>() {
            public int compare(Car car, Car t1) {
                int compareVal = 0;

                try {
                    Sipp carSipp = car.getSipp();
                    Sipp t1Sipp = t1.getSipp();

                    compareVal = carSipp.getCarType().compareTo(t1Sipp.getCarType());
                    if (compareVal == 0) {
                        compareVal = Float.valueOf(car.getRating()).compareTo(t1.getRating());
                    }

                }

                catch (InvalidSippException e) {
                    System.err.println(e);
                    compareVal = 0;
                }

                return compareVal;
            }
        });
    }

    public void sortCarsByScore(ArrayList<Car> cars) {
        Collections.sort(cars, new Comparator<Car>() {
            public int compare(Car car, Car t1) {
                try {
                    float carScoreSum = car.getScoreSum();
                    float t1ScoreSum = t1.getScoreSum();

                    if (carScoreSum > t1ScoreSum) {
                        return 1;
                    }
                    else if (carScoreSum < t1ScoreSum) {
                        return -1;
                    }
                    else {
                        return 0;
                    }
                }
                catch (InvalidSippException e) {
                    System.err.println(e);
                    return 0;
                }
            }
        });
    }

    public static void main(String[] args) {
        CarParser carParser = new CarParser();
        ArrayList<Car> cars = carParser.carFromJson("vehicles.json");
        carParser.sortCarsByPrice(cars);

        System.out.println();
        System.out.println("---Sorting by Price---");
        for (Car c: cars) {
            System.out.println(c.getName() + " - " + + c.getPrice());
        }


        // Sort cars by rating type and print out
        carParser.sortCarsByRatingCarType(cars);

        System.out.println();
        System.out.println("---Sorting by Rating and carparser.Car Type (Desc)---");
        for (Car c: cars) {
            try {
                Sipp sipp = new Sipp(c.getSippCode());
                System.out.println(c.getName() + " - "
                        + sipp.getCarType() + " - " + c.getSupplier()
                        + " - " + " - " + c.getRating());
            }

            catch (InvalidSippException e) {
                System.err.println(e);
            }

        }

        // Sort cars by sum of scores
        carParser.sortCarsByScore(cars);

        System.out.println();
        System.out.println("---Sorting by Sum of Scores---");
        for (Car c: cars) {
            try {
                System.out.println(c.getName() + " - " + c.getScore() + " - "
                        + c.getRating() + " - " + c.getScoreSum());
            }
            catch (InvalidSippException e) {
                System.err.println(e);
            }
        }

    }

}
