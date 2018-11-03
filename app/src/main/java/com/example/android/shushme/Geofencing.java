package com.example.android.shushme;

import android.app.PendingIntent;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;

import java.util.ArrayList;

public class Geofencing {
    private PendingIntent mGeofencePendingIntent;
    private GoogleApiClient mGoogleApiClient;
    private Context mContext;
    private ArrayList<Geofence> mGeofenceList;

    public Geofencing(˜Context context, GoogleApiClient client) {
        this.mContext = context;
        this.mGoogleApiClient = client;

        mGeofencePendingIntent = null;
        mGeofenceList = new ArrayList<>();
    }
}
