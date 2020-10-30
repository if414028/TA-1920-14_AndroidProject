package com.ditenun.appditenun.dependency.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PaymentMethod implements Parcelable {

    private Integer paymentCode;
    private String paymentName;
    private String paymentDesc;
    private boolean isSelected;

    protected PaymentMethod(Parcel in) {
        if (in.readByte() == 0) {
            paymentCode = null;
        } else {
            paymentCode = in.readInt();
        }
        paymentName = in.readString();
        paymentDesc = in.readString();
        isSelected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (paymentCode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(paymentCode);
        }
        dest.writeString(paymentName);
        dest.writeString(paymentDesc);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaymentMethod> CREATOR = new Creator<PaymentMethod>() {
        @Override
        public PaymentMethod createFromParcel(Parcel in) {
            return new PaymentMethod(in);
        }

        @Override
        public PaymentMethod[] newArray(int size) {
            return new PaymentMethod[size];
        }
    };

    public Integer getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(Integer paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentDesc() {
        return paymentDesc;
    }

    public void setPaymentDesc(String paymentDesc) {
        this.paymentDesc = paymentDesc;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
