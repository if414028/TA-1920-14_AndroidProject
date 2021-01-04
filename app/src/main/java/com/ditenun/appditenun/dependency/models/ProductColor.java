package com.ditenun.appditenun.dependency.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductColor implements Parcelable {

    private int colorId;
    private String colorCode;
    private String colorName;
    private boolean isSelected;

    public ProductColor() {
    }

    protected ProductColor(Parcel in) {
        colorId = in.readInt();
        colorCode = in.readString();
        colorName = in.readString();
        isSelected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(colorId);
        dest.writeString(colorCode);
        dest.writeString(colorName);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductColor> CREATOR = new Creator<ProductColor>() {
        @Override
        public ProductColor createFromParcel(Parcel in) {
            return new ProductColor(in);
        }

        @Override
        public ProductColor[] newArray(int size) {
            return new ProductColor[size];
        }
    };

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
