package com.swapnil.utills;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkAvailablity {
    private static NetworkAvailablity mRefrence = null;
    private static Context context;

    private NetworkAvailablity() {
    }

    /**
     * Singleton method return the instance.
     *
     * @param activity
     * @return
     */
    public static NetworkAvailablity getInstance(Context activity) {
        if (null == mRefrence)
            mRefrence = new NetworkAvailablity();
        context = activity;
        return mRefrence;
    }

    /**
     * Check network availability.
     *
     * @return true
     */
    public boolean checkNetworkStatus() {
        try {
            boolean HaveConnectedWifi = false;
            boolean HaveConnectedMobile = false;
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        HaveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        HaveConnectedMobile = true;
            }
            return HaveConnectedWifi || HaveConnectedMobile;
        } catch (Exception e) {
            return false;
        }
    }
}