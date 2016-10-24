package com.dst.hackathon.fpv.data.model.enums;

/**
 * Created by tisana on 24/10/2559.
 */
public enum BusinessUnit {
    FSG("Financial Service"),
    BPS("Business Process Service"),
    HR("Human Resource"),
    IFDS("International Financial Data Services"),
    ADMIN("Administration"),
    TS("Trading Solution");

    BusinessUnit(String fullName) {
        this.fullName = fullName;
    }

    private String fullName;

    public String getFullName() {
        return fullName;
    }
}
