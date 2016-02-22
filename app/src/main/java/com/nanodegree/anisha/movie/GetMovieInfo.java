package com.nanodegree.anisha.movie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anisha on 30/1/16.
 */
public class GetMovieInfo implements Parcelable {
    public static final Parcelable.Creator<GetMovieInfo> CREATOR = new Parcelable.Creator<GetMovieInfo>() {

        @Override
        public GetMovieInfo createFromParcel(Parcel source) {
            return new GetMovieInfo(source);
        }

        @Override
        public GetMovieInfo[] newArray(int size) {
            return new GetMovieInfo[size];
        }
    };
    String ID = "id";
    String POSTER_PATH = "poster path";
    String POSTER = "poster";
    String OVERVIEW = "overview";
    String RELEASE_DATE = "date";
    String TITLE = "title";
    double POPULARITY = 0.0;
    int VOTECOUNT = 0;
    double VOTEAVERAGE = 0.0;
    String RATING = "rating";

    public GetMovieInfo(String ID, String POSTER_PATH, String POSTER, String OVERVIEW, String RELEASE_DATE, String TITLE, double POPULARITY, int VOTECOUNT, double VOTEAVERAGE) {
        this.ID = ID;
        this.POSTER_PATH = POSTER_PATH;
        this.POSTER = POSTER;
        this.OVERVIEW = OVERVIEW;
        this.RELEASE_DATE = RELEASE_DATE;
        this.TITLE = TITLE;
        this.POPULARITY = POPULARITY;
        this.VOTECOUNT = VOTECOUNT;
        this.VOTEAVERAGE = VOTEAVERAGE;
    }

    public GetMovieInfo(String ID, String TITLE, String POSTER_PATH, String RELEASE_DATE, String OVERVIEW, String RATING) {
        this.ID = ID;
        this.POSTER_PATH = POSTER_PATH;
        this.OVERVIEW = OVERVIEW;
        this.RELEASE_DATE = RELEASE_DATE;
        this.TITLE = TITLE;
        this.RATING = RATING;
    }

    private GetMovieInfo(Parcel in) {
        this.ID = in.readString();
        this.TITLE = in.readString();
        this.POSTER_PATH = in.readString();
        this.POSTER = in.readString();
        this.OVERVIEW = in.readString();
        this.RATING = in.readString();
    }

    public String getRATING() {
        return RATING;
    }

    public String getID() {
        return ID;
    }

    public String getPOSTER_PATH() {
        return POSTER_PATH;
    }

    public String getOVERVIEW() {
        return OVERVIEW;
    }

    public String getRELEASE_DATE() {
        return RELEASE_DATE;
    }

    public String getTITLE() {
        return TITLE;
    }

    public double getPOPULARITY() {
        return POPULARITY;
    }

    public int getVOTECOUNT() {
        return VOTECOUNT;
    }

    public double getVOTEAVERAGE() {
        return VOTEAVERAGE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(TITLE);
        dest.writeString(POSTER_PATH);
        dest.writeString(RELEASE_DATE);
        dest.writeString(OVERVIEW);
        dest.writeString(RATING);
    }

}
