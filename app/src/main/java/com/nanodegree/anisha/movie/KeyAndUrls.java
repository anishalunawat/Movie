package com.nanodegree.anisha.movie;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by lunawat on 25-02-2016.
 */
public class KeyAndUrls extends Activity{
    String api_key = "bf0aace1771bea3d22d512b285bbf4c8";
    String baseurl="http://api.themoviedb.org/3";
    String youtubelink="http://image.tmdb.org/t/p/w342";

    public String getYoutubelink() {
        return youtubelink;
    }

    public String getApi_key() {
        return api_key;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public boolean isConnected()
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
}
