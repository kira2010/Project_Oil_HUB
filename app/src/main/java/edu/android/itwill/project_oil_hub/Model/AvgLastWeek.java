package edu.android.itwill.project_oil_hub.Model;

public class AvgLastWeek {
    private String week;
    private String startDT;
    private String endDT;
    private String areaCD;
    private String prodNM;
    private String avgPrice;

    public AvgLastWeek(String week,
                       String startDT,
                       String endDT,
                       String areaCD,
                       String prodNM,
                       String avgPrice) {
        this.week = week;
        this.startDT = startDT;
        this.endDT = endDT;
        this.areaCD = areaCD;
        this.prodNM = prodNM;
        this.avgPrice = avgPrice;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getStartDT() {
        return startDT;
    }

    public void setStartDT(String startDT) {
        this.startDT = startDT;
    }

    public String getEndDT() {
        return endDT;
    }

    public void setEndDT(String endDT) {
        this.endDT = endDT;
    }

    public String getAreaCD() {
        return areaCD;
    }

    public void setAreaCD(String areaCD) {
        this.areaCD = areaCD;
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

    @Override
    public String toString() {
        return "AvgLastWeek{" +
                "week='" + week + '\'' +
                ", startDT='" + startDT + '\'' +
                ", endDT='" + endDT + '\'' +
                ", areaCD='" + areaCD + '\'' +
                ", prodNM='" + prodNM + '\'' +
                ", avgPrice='" + avgPrice + '\'' +
                '}';
    }
}
