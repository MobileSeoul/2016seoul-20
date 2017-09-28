package com.example.dokyeong.seoulreadingplace;

import java.io.Serializable;

/**
 * Created by DoKyeong on 2016. 10. 9..
 */

public class BookPlace implements Serializable{
    private int mInt_Category; // 0 : 도서관, 1 : 서점
    private String mString_AddressGu;
    private String mString_Address;
    private String mString_Name;
    private String mString_Website;
    private String mString_PhoneNumber;
    private String mString_Organization;
    private String mString_Longitude; // 경도
    private String mString_Latitude; // 위도

    public BookPlace(int int_Category, String string_AddressGu, String string_Address, String string_Name,
                     String string_Website, String string_PhoneNumber,
                     String string_Organization, String string_Longitude, String string_Latitude){
        mInt_Category = int_Category;
        mString_AddressGu = string_AddressGu;
        mString_Address = string_Address;
        mString_Name = string_Name;
        mString_Website = string_Website;
        mString_PhoneNumber = string_PhoneNumber;
        mString_Organization = string_Organization;
        mString_Longitude = string_Longitude;
        mString_Latitude = string_Latitude;
    }

    public int getCategory() { return mInt_Category; }

    public String getAddressGu(){
        return mString_AddressGu;
    }

    public String getAddress() { return mString_Address; }

    public String getName(){
        return mString_Name;
    }

    public String getWebsite(){
        return mString_Website;
    }

    public String getPhoneNumber() { return mString_PhoneNumber; }

    public String getOrganization() { return mString_Organization; }

    public String getLongitude(){
        return mString_Longitude;
    }

    public String getLatitude() { return mString_Latitude; }
}
