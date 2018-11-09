package edu.android.itwill.project_oil_hub.Model;

public class LocalAverage {
    private String prodNM;
    private String avgPrice;
    private String diff;

    public LocalAverage(String prodNM, String avgPrice, String diff) {
        this.prodNM = prodNM;
        this.avgPrice = avgPrice;
        this.diff = diff;
    }

    public String getProdNM() {
        return prodNM;
    }

    public void setProdNM(String prodNM) {
        this.prodNM = prodNM;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    @Override
    public String toString() {
        return "Average_Value{" +
                "prodNM='" + prodNM + '\'' +
                ", avgPrice=" + avgPrice +
                ", diff='" + diff + '\'' +
                '}';
    }
}
