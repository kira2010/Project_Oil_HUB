package edu.android.itwill.project_oil_hub.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Oil_LowTop10 implements Parcelable {
    private String gasStationCode;
    private int price;
    private String brandName;
    private String gasStationName;
    private String oldAddress;
    private String newAddress;
    private int imageLogo;

    public Oil_LowTop10(String gasStationCode,
                        int price,
                        String brandName,
                        String gasStationName,
                        String oldAddress,
                        String newAddress,
                        int imageLogo) {
        this.gasStationCode = gasStationCode;
        this.price = price;
        this.brandName = brandName;
        this.gasStationName = gasStationName;
        this.oldAddress = oldAddress;
        this.newAddress = newAddress;
        this.imageLogo = imageLogo;
    }

    protected Oil_LowTop10(Parcel in) {
        gasStationCode = in.readString();
        price = in.readInt();
        brandName = in.readString();
        gasStationName = in.readString();
        oldAddress = in.readString();
        newAddress = in.readString();
        imageLogo = in.readInt();
    }

    public static final Creator<Oil_LowTop10> CREATOR = new Creator<Oil_LowTop10>() {
        @Override
        public Oil_LowTop10 createFromParcel(Parcel in) {
            return new Oil_LowTop10(in);
        }

        @Override
        public Oil_LowTop10[] newArray(int size) {
            return new Oil_LowTop10[size];
        }
    };

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

    public int getImageLogo() {
        return imageLogo;
    }

    public void setImageLogo(int imageLogo) {
        this.imageLogo = imageLogo;
    }

    @Override
    public String toString() {
        return "Oil_LowTop10{" +
                "gasStationCode='" + gasStationCode + '\'' +
                ", price=" + price +
                ", brandName='" + brandName + '\'' +
                ", gasStationName='" + gasStationName + '\'' +
                ", oldAddress='" + oldAddress + '\'' +
                ", newAddress='" + newAddress + '\'' +
                ", imageLogo=" + imageLogo +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gasStationCode);
        dest.writeInt(price);
        dest.writeString(brandName);
        dest.writeString(gasStationName);
        dest.writeString(oldAddress);
        dest.writeString(newAddress);
        dest.writeInt(imageLogo);
    }
}
