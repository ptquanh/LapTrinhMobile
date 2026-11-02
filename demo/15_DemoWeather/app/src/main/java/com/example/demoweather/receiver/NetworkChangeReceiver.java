package com.example.demoweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkChangeReceiver";

    public interface OnNetworkChangeListener {
        void onNetworkChanged(boolean isConnected, String networkType);
    }

    private OnNetworkChangeListener listener;

    public NetworkChangeReceiver() {
    }

    public NetworkChangeReceiver(OnNetworkChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String networkType = getNetworkType(context);
        boolean isConnected = !"Không có mạng".equals(networkType);

        Log.d(TAG, "Mạng thay đổi: " + networkType + " | Connected: " + isConnected);

        if (listener != null) {
            listener.onNetworkChanged(isConnected, networkType);
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        return !"Không có mạng".equals(getNetworkType(context));
    }

    public static String getNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return "Không có mạng";
        }

        Network activeNetwork = cm.getActiveNetwork();
        if (activeNetwork == null) {
            return "Không có mạng";
        }

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(activeNetwork);
        if (capabilities == null
                || !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            return "Không có mạng";
        }

        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return "WiFi";
        }
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return "Mobile Data";
        }
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return "Ethernet";
        }
        return "Khác";
    }
}

