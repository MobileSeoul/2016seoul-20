package com.example.dokyeong.seoulreadingplace;

/**
 * Created by DoKyeong on 2016. 10. 30..
 */

public class MarkerTag {
    private BookPlace mBookPlace_One;
    private int mInt_Picture;

    public MarkerTag(BookPlace bookPlace_One, int int_Picture){
        mBookPlace_One = bookPlace_One;
        mInt_Picture = int_Picture;
    }

    public BookPlace getBookPlace(){
        return mBookPlace_One;
    }

    public int getPicture(){
        return mInt_Picture;
    }
}
