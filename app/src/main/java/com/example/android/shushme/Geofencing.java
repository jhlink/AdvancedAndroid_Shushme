package com.example.android.shushme;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;

import java.util.ArrayList;

public class Geofencing implements ResultCallback {
    private static final String TAG = Geofencing.class.getSimpleName();
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

    public GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if ( mGeofencePendingIntent != null ) {
            return mGeofencePendingIntent;
        }

        Intent intent = new Intent( mContext, GeofenceBroadcastReceiver.class);
        mGeofencePendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return mGeofencePendingIntent;

    }

    public void registerAllGeofences() {
       if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
           return;
       }

       try {
           LocationServices.GeofencingApi.addGeofences(
                   mGoogleApiClient,
                   getGeofencingRequest(),
                   getGeofencePendingIntent()
           ).setResultCallback(this);
       } catch (SecurityException securityException) {
           Log.e(TAG, securityException.getMessage());
       }
    }

    @Override
    public void onResult(@NonNull Result result) {
        Log.e(TAG, String.format("Error, adding/removing geofence : %s",
                result.getStatus().toString()));
    }
}
