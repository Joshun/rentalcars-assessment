package carparser;

// Sipp: parse Sipp codes into separate values

import java.util.HashMap;


public class Sipp {
    public static final HashMap<String, String> carTypes = new HashMap<String, String>();
    public static final HashMap<String, String> doorTypes = new HashMap<String, String>();

    static {
        carTypes.put("M", "Mini");
        carTypes.put("E", "Economy");
        carTypes.put("C", "Compact");
        carTypes.put("I", "Intermediate");
        carTypes.put("S", "Standard");
        carTypes.put("F", "Full size");
        carTypes.put("P", "Premium");
        carTypes.put("L", "Luxury");
        carTypes.put("X", "Special");

        doorTypes.put("B", "2 doors");
        doorTypes.put("C", "4 doors");
        doorTypes.put("D", "5 doors");
        doorTypes.put("W", "Estate");
        doorTypes.put("T", "Convertible");
        doorTypes.put("F", "SUV");
        doorTypes.put("P", "Pick up");
        doorTypes.put("V", "Passenger Van");
    }

    private String sippSpec;

    public String getSippSpec() {
        return sippSpec;
    }

    public String getCarType() {
        return carType;
    }

    public String getDoorType() {
        return doorType;
    }

    public String getTransmission() {
        return transmission;
    }

    public String getFuel() {
        return fuel;
    }

    public String getAircon() {
        return aircon;
    }

    private String carType;
    private String doorType;
    private String transmission;
    private String fuel;
    private String aircon;




    public Sipp(String sippSpec) throws InvalidSippException {
        if (sippSpec.length() != 4) {
            throw new InvalidSippException("sipp length invalid", new Throwable(sippSpec));
        }

        this.sippSpec = sippSpec;

        String carTypeCode = String.valueOf(sippSpec.charAt(0));
        String doorTypeCode = String.valueOf(sippSpec.charAt(1));
        String transmissionTypeCode = String.valueOf(sippSpec.charAt(2));
        String fuelAirconCode = String.valueOf(sippSpec.charAt(3));

        String carType = carTypes.get(carTypeCode);
        String doorType = doorTypes.get(doorTypeCode);

        if (transmissionTypeCode.equals("M")) {
            transmission = "Manual";
        }
        else if (transmissionTypeCode.equals("A")) {
            transmission = "Automatic";
        }
        else {
            throw new InvalidSippException("sipp transmission invalid",
                    new Throwable(transmissionTypeCode));
        }

        if (fuelAirconCode.equals("N")) {
            fuel = "Petrol";
            aircon = "no A/C";
        }
        else if (fuelAirconCode.equals("R")) {
            fuel = "Petro";
            aircon = "A/C";
        }
        else {
            throw new InvalidSippException("sipp fuel aircon invalid",
                    new Throwable(fuelAirconCode));
        }

        this.carType = carType;
        this.doorType = doorType;
    }

    public String toString() {
        return carType + " - " + doorType + " - " + transmission + " - " + fuel + " - " + aircon;
    }
}
