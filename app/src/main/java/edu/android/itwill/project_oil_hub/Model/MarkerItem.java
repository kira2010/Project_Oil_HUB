package edu.android.itwill.project_oil_hub.Model;

public class MarkerItem {

    double lat;
    double lon;
    int price;
    String brandName;

    public MarkerItem(double lat, double lon, int price, String brandName) {
        this.lat = lat;
        this.lon = lon;
        this.price = price;
        this.brandName = brandName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
