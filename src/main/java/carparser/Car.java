package carparser;

// Car: represents an individual car object in a sensible format

import com.google.gson.annotations.SerializedName;

public class Car {

    @SerializedName("sipp")
    private String sippCode;
    private String name;
    private float price;
    private String supplier;

    public String getSupplier() {
        return supplier;
    }

    private float rating;

    public String getName() {
        return name;
    }

    public Car(String sippCode, String name, float price, String supplier, float rating) {
        this.sippCode = sippCode;
        this.name = name;

        this.price = price;
        this.supplier = supplier;
        this.rating = rating;
    }

    public String getSippCode() {
        return sippCode;
    }

    public Sipp getSipp() throws InvalidSippException {
        Sipp s = new Sipp(sippCode);
        return s;
    }

    public int getScore() throws InvalidSippException {
        int points = 0;
        Sipp s = new Sipp(sippCode);
        if (s.getTransmission().equals("M")) {
            points += 1;
        }
        else if (s.getTransmission().equals("A")) {
            points += 5;
        }

        if (s.getAircon().equals("A/C")) {
            points += 2;
        }

        return points;
    }

    public float getPrice() {
        return price;
    }

    public float getRating() {
        return rating;
    }

    public float getScoreSum() throws InvalidSippException {
        return rating + getScore();
    }

    public String toString() {
        return "carparser.Car [sippCode=" + sippCode + ", " + "name=" + name + ","
                + "price=" + price + ", " + "supplier=" + supplier + ", "
                + "rating=" + rating + "]";
    }

}
