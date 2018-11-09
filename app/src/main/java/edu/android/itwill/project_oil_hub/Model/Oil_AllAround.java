package edu.android.itwill.project_oil_hub.Model;

public class Oil_AllAround {
    private String gasStationCode;
    private int price;
    private String brandName;
    private String gasStationName;
    private String oldAddress;
    private String newAddress;
    private int distance;
    private int imageLogo;
    private double xCOOR;
    private double yCOOR;

    public Oil_AllAround(String gasStationCode,
                         int price,
                         String brandName,
                         String gasStationName,
                         String oldAddress,
                         String newAddress,
                         int distance,
                         int imageLogo,
                         double xCOOR,
                         double yCOOR) {
        this.gasStationCode = gasStationCode;
        this.price = price;
        this.brandName = brandName;
        this.gasStationName = gasStationName;
        this.oldAddress = oldAddress;
        this.newAddress = newAddress;
        this.distance = distance;
        this.imageLogo = imageLogo;
        this.xCOOR = xCOOR;
        this.yCOOR = yCOOR;
    }

    public String getGasStationCode() {
        return gasStationCode;
    }

    public void setGasStationCode(String gasStationCode) {
        this.gasStationCode = gasStationCode;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getGasStationName() {
        return gasStationName;
    }

    public void setGasStationName(String gasStationName) {
        this.gasStationName = gasStationName;
    }

    public String getOldAddress() {
        return oldAddress;
    }

    public void setOldAddress(String oldAddress) {
        this.oldAddress = oldAddress;
    }

    public String getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getImageLogo() {
        return imageLogo;
    }

    public void setImageLogo(int imageLogo) {
        this.imageLogo = imageLogo;
    }

    public double getxCOOR() {
        return xCOOR;
    }

    public void setxCOOR(double xCOOR) {
        this.xCOOR = xCOOR;
    }

    public double getyCOOR() {
        return yCOOR;
    }

    public void setyCOOR(double yCOOR) {
        this.yCOOR = yCOOR;
    }

    @Override
    public String toString() {
        return "Oil{" +
                "gasStationCode='" + gasStationCode + '\'' +
                ", price=" + price +
                ", brandName='" + brandName + '\'' +
                ", gasStationName='" + gasStationName + '\'' +
                ", oldAddress='" + oldAddress + '\'' +
                ", newAddress='" + newAddress + '\'' +
                ", distance=" + distance +
                ", xCOOR=" + xCOOR +
                ", yCOOR=" + yCOOR +
                '}';
    }
}
