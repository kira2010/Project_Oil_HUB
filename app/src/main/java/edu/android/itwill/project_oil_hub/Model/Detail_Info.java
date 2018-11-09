package edu.android.itwill.project_oil_hub.Model;

import java.util.List;

public class Detail_Info {
    private String telNumber;
    private String lpg_YN;
    private boolean maint_YN;
    private boolean car_wash_YN;
    private boolean cvs_YN;
    private List<Price> list;

    public Detail_Info(String telNumber,
                       String lpg_YN,
                       boolean maint_YN,
                       boolean car_wash_YN,
                       boolean cvs_YN,
                       List<Price> list) {
        this.telNumber = telNumber;
        this.lpg_YN = lpg_YN;
        this.maint_YN = maint_YN;
        this.car_wash_YN = car_wash_YN;
        this.cvs_YN = cvs_YN;
        this.list = list;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getLpg_YN() {
        return lpg_YN;
    }

    public void setLpg_YN(String lpg_YN) {
        this.lpg_YN = lpg_YN;
    }

    public boolean isMaint_YN() {
        return maint_YN;
    }

    public void setMaint_YN(boolean maint_YN) {
        this.maint_YN = maint_YN;
    }

    public boolean isCar_wash_YN() {
        return car_wash_YN;
    }

    public void setCar_wash_YN(boolean car_wash_YN) {
        this.car_wash_YN = car_wash_YN;
    }

    public boolean isCvs_YN() {
        return cvs_YN;
    }

    public void setCvs_YN(boolean cvs_YN) {
        this.cvs_YN = cvs_YN;
    }

    public List<Price> getList() {
        return list;
    }

    public void setList(List<Price> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Detail_Info{" +
                "telNumber='" + telNumber + '\'' +
                ", lpg_YN='" + lpg_YN + '\'' +
                ", maint_YN=" + maint_YN +
                ", cvs_YN=" + cvs_YN +
                ", list=" + list +
                '}';
    }


}
