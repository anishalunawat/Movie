package com.nanodegree.anisha.movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lunawat on 26-02-2016.
 */
public class ReviewData {
    int id;
    List<ReviewInfo> results;
    ReviewData()
    {
        results=new ArrayList<>();
    }
}

class ReviewInfo{
    public String author;
    public String content;
}
