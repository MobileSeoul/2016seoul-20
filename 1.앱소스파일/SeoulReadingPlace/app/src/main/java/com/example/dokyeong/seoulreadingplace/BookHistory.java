package com.example.dokyeong.seoulreadingplace;

import java.io.Serializable;

/**
 * Created by DoKyeong on 2016. 10. 27..
 */

public class BookHistory implements Serializable {
    private String mString_Title;
    private String mString_Isbn;
    private int mInt_Category;
    private String mString_Name;
    private String mString_Date;

    public BookHistory(String string_Title, String string_Isbn, int int_Category, String string_Name, String string_Date) {
        mString_Title = string_Title;
        mString_Isbn = string_Isbn;
        mInt_Category = int_Category;
        mString_Name = string_Name;
        mString_Date = string_Date;
    }

    public String getTitle(){
        return mString_Title;
    }

    public String getIsbn() { return mString_Isbn; }

    public int getCategory() { return mInt_Category; }

    public String getName(){
        return mString_Name;
    }

    public String getDate(){
        return mString_Date;
    }
}
