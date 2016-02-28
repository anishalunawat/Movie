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
    String id = "id";
    String poster_path = "poster path";
    String overview = "overview";
    String release_date = "date";
    String title = "title";
    double popularity = 0.0;
    int vote_count = 0;
    double vote_average = 0.0;
    String rating = "rating";

    public GetMovieInfo(String id, String poster_path, String overview, String release_date, String title, double popularity, int vote_count, double vote_average) {
        this.id = id;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.title = title;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.vote_average = vote_average;
    }

    public GetMovieInfo(String id, String title, String poster_path, String release_date, String overview, String rating) {
        this.id = id;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.title = title;
        this.rating = rating;
    }

    private GetMovieInfo(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.poster_path = in.readString();
        this.release_date=in.readString();
        this.overview = in.readString();
        this.rating = in.readString();
    }

    public String getRATING() {
        return rating;
    }

    public String getID() {
        return id;
    }

    public String getPOSTER_PATH() {
        return poster_path;
    }

    public String getOVERVIEW() {
        return overview;
    }

    public String getRELEASE_DATE() {
        return release_date;
    }

    public String getTITLE() {
        return title;
    }

    public double getPOPULARITY() {
        return popularity;
    }

    public int getVOTECOUNT() {
        return vote_count;
    }

    public double getVOTEAVERAGE() {
        return vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeString(release_date);
        dest.writeString(overview);
        dest.writeString(rating);
    }

}
