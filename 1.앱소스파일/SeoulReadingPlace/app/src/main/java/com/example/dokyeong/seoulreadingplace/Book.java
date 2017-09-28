package com.example.dokyeong.seoulreadingplace;

import java.io.Serializable;

/**
 * Created by DoKyeong on 2016. 9. 4..
 */
public class Book implements Serializable{
    private String mString_Title;
    private String mString_Cover;
    private String mString_Author;
    private String mString_Publisher;
    private String mString_Date;
    private String mString_Url;
    private String mString_Isbn;
    private String mString_Start;
    private int mInt_Page;
    private int mInt_Finish;

    public Book(String string_Title, String string_Cover, String string_Author, String string_Publisher, String string_Date, String string_Url,
                String string_Isbn, String string_Start, int int_Page, int int_Finish){
        mString_Cover = string_Cover;
        mString_Title = string_Title;
        mString_Author = string_Author;
        mString_Publisher = string_Publisher;
        mString_Date = string_Date;
        mString_Url = string_Url;
        mString_Isbn = string_Isbn;
        mString_Start = string_Start;
        mInt_Page = int_Page;
        mInt_Finish = int_Finish;
    }

    public void setStart(String string_Start){
        mString_Start = string_Start;
    }

    public void setPage(int int_Page){
        mInt_Page = int_Page;
    }

    public void setFinish(int int_Finish){
        mInt_Finish = int_Finish;
    }

    public String getTitle(){
        return mString_Title;
    }

    public String getCover() { return mString_Cover; }

    public String getAuthor(){
        return mString_Author;
    }

    public String getPublisher(){
        return mString_Publisher;
    }

    public String getDate(){
        return mString_Date;
    }

    public String getUrl() { return mString_Url; }

    public String getIsbn() { return mString_Isbn; }

    public String getStart() { return mString_Start; }

    public int getPage() { return mInt_Page; }

    public int getFinish() { return mInt_Finish; }
}
