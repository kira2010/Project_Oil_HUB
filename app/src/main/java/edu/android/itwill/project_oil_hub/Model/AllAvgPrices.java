package edu.android.itwill.project_oil_hub.Model;

public class AllAvgPrices {
    private String tradeDT;
    private String prodName;
    private String avgPrice;
    private String vari_Value;

    public AllAvgPrices(String tradeDT,
                        String prodName,
                        String avgPrice,
                        String vari_Value) {
        this.tradeDT = tradeDT;
        this.prodName = prodName;
        this.avgPrice = avgPrice;
        this.vari_Value = vari_Value;
    }

    public String getTradeDT() {
        return tradeDT;
    }

    public void setTradeDT(String tradeDT) {
        this.tradeDT = tradeDT;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getVari_Value() {
        return vari_Value;
    }

    public void setVari_Value(String vari_Value) {
        this.vari_Value = vari_Value;
    }

    @Override
    public String toString() {
        return "AllAvgPrices{" +
                "tradeDT='" + tradeDT + '\'' +
                ", prodName='" + prodName + '\'' +
                ", avgPrice=" + avgPrice +
                ", vari_Value='" + vari_Value + '\'' +
                '}';
    }
}
