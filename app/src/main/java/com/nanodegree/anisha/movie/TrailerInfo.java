package com.nanodegree.anisha.movie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anisha on 20/2/16.
 */
public class TrailerInfo implements Parcelable {
    String name = "name";
    String key = "key";

    public TrailerInfo(String name, String key) {
        this.name = name;
        this.key = key;
    }

    private TrailerInfo(Parcel in) {
        name = in.readString();
        key = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(key);
    }

    public static final Parcelable.Creator<TrailerInfo> CREATOR = new Parcelable.Creator<TrailerInfo>() {
        public TrailerInfo createFromParcel(Parcel in) {
            return new TrailerInfo(in);
        }

        public TrailerInfo[] newArray(int size) {
            return new TrailerInfo[size];
        }
    };
}
