package carparser;

// JsonCars: class for representing serialized JSON enabling it to be easily deserialized
// by Gson library

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JsonCars {
    public class Search {
        @SerializedName("VehicleList")
        public ArrayList<Car> vehicleList;

        public ArrayList<Car> getVehicleList() {
            return vehicleList;
        }
    }

    @SerializedName("Search")
    public Search search;

    public ArrayList<Car> getVehicleList() {
        return search.getVehicleList();
    }
}
