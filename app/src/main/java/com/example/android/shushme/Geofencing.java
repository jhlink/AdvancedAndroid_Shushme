package com.example.android.shushme;

import android.app.PendingIntent;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;

import java.util.ArrayList;

public class Geofencing {
    private static final int GEOFENCE_TIMEOUT = 3600;
    private static final float GEOFENCE_RADIUS = 1.0f;

    private PendingIntent mGeofencePendingIntent;
    private GoogleApiClient mGoogleApiClient;
    private Context mContext;
    private ArrayList<Geofence> mGeofenceList;

    public Geofencing(Context context, GoogleApiClient client) {
        this.mContext = context;
        this.mGoogleApiClient = client;

        mGeofencePendingIntent = null;
        mGeofenceList = new ArrayList<>();
    }

    public void updateGeofencingList(PlaceBuffer places) {
        mGeofenceList = new ArrayList<>();
        if ( places == null || places.getCount() == 0) return;
        for ( Place place : places ) {
            String placeUID = place.getId();
            double placeLat = place.getLatLng().latitude;
            double placeLng = place.getLatLng().longitude;

            Geofence geofence = new Geofence.Builder()
                    .setRequestId(placeUID)
                    .setExpirationDuration(GEOFENCE_TIMEOUT)
                    .setCircularRegion(placeLat, placeLng, GEOFENCE_RADIUS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();
            mGeofenceList.add(geofence);

        }
    }
}
