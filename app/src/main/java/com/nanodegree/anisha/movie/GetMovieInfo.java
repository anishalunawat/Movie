package com.nanodegree.anisha.movie;

/**
 * Created by anisha on 30/1/16.
 */
public class GetMovieInfo {
    String ID = "id";
    String POSTER_PATH = "poster path";
    String POSTER="poster";
    String OVERVIEW = "overview";
    String RELEASE_DATE = "date";
    String TITLE = "title";
    double POPULARITY = 0.0;
    int VOTECOUNT = 0;
    double VOTEAVERAGE = 0.0;
    String RATING = "rating";

    public String getRATING() {
        return RATING;
    }

    public void setRATING(String RATING) {
        this.RATING = RATING;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPOSTER_PATH() {
        return POSTER_PATH;
    }

    public void setPOSTER_PATH(String POSTER_PATH) {
        this.POSTER_PATH = POSTER_PATH;
    }

    public String getOVERVIEW() {
        return OVERVIEW;
    }

    public void setOVERVIEW(String OVERVIEW) {
        this.OVERVIEW = OVERVIEW;
    }

    public String getRELEASE_DATE() {
        return RELEASE_DATE;
    }

    public void setRELEASE_DATE(String RELEASE_DATE) {
        this.RELEASE_DATE = RELEASE_DATE;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public double getPOPULARITY() {
        return POPULARITY;
    }

    public void setPOPULARITY(double POPULARITY) {
        this.POPULARITY = POPULARITY;
    }

    public int getVOTECOUNT() {
        return VOTECOUNT;
    }

    public void setVOTECOUNT(int VOTECOUNT) {
        this.VOTECOUNT = VOTECOUNT;
    }

    public double getVOTEAVERAGE() {
        return VOTEAVERAGE;
    }

    public void setVOTEAVERAGE(double VOTEAVERAGE) {
        this.VOTEAVERAGE = VOTEAVERAGE;
    }

    public String getPOSTER() {
        return POSTER;
    }

    public void setPOSTER(String POSTER) {
        this.POSTER = POSTER;
    }
}
