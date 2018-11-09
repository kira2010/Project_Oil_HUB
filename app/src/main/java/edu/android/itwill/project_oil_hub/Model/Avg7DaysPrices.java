package edu.android.itwill.project_oil_hub.Model;

public class Avg7DaysPrices {
    private String date;
    private String avgPrice;

    public Avg7DaysPrices(String date, String avgPrice) {
        this.date = date;
        this.avgPrice = avgPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    @Override
    public String toString() {
        return "Avg7DaysPrices{" +
                "date='" + date + '\'' +
                ", avgPrice='" + avgPrice + '\'' +
                '}';
    }
}
