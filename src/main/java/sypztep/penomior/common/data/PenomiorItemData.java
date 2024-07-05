package sypztep.penomior.common.data;

import com.google.gson.annotations.SerializedName;

public class PenomiorItemData {
    private String itemID;
    private int maxLvl;
    @SerializedName("startAccuracy")
    private int startAccuracy;
    @SerializedName("endAccuracy")
    private int endAccuracy;

    public PenomiorItemData(String itemID, int maxLvl, int startAccuracy, int endAccuracy) {
        this.itemID = itemID;
        this.maxLvl = maxLvl;
        this.startAccuracy = startAccuracy;
        this.endAccuracy = endAccuracy;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public int getMaxLvl() {
        return maxLvl;
    }

    public void setMaxLvl(int maxLvl) {
        this.maxLvl = maxLvl;
    }

    public int getStartAccuracy() {
        return startAccuracy;
    }

    public void setStartAccuracy(int startAccuracy) {
        this.startAccuracy = startAccuracy;
    }

    public int getEndAccuracy() {
        return endAccuracy;
    }

    public void setEndAccuracy(int endAccuracy) {
        this.endAccuracy = endAccuracy;
    }
}
