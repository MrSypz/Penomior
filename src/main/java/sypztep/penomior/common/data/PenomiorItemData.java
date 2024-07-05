package sypztep.penomior.common.data;

import com.google.gson.annotations.SerializedName;

public record PenomiorItemData(
        String itemID,
        int maxLvl,
        @SerializedName("startAccuracy") int startAccuracy,
        @SerializedName("endAccuracy") int endAccuracy
) {}