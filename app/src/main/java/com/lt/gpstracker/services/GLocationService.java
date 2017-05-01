package com.lt.gpstracker.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lt.gpstracker.R;
import com.lt.gpstracker.fragments.MyTrackerFragment;
import com.lt.gpstracker.utility.Preference;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;

public class GLocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "LocationService";


    private String defaultUploadWebsite;

    private boolean currentlyProcessingLocation = false;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient = null;
    private LocationManager locationManager = null;
    private Location lastKnownLocation = null;
    private boolean bGPSEnabled = false;
    private Context context = null;
    private static GLocationService service = null;


    public static GLocationService getServiceInstance() {
        return service;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Construct the default web service url with the defautl data
        String dataURL = "id=357646054489439&code=0xF020&android=$";
        defaultUploadWebsite = getString(R.string.default_upload_website) + dataURL;
        Log.d(TAG, "Default URL :- " + defaultUploadWebsite);
        service = this;
    }

    public void initLocation(Context context) {
        this.context = context;
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            bGPSEnabled = true;
        } else {
            bGPSEnabled = false;
        }

        // Get the last known location if available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    /**
     * Get the last know location from Location manager
     *
     * @return lastKnownLocation
     */
    public Location GetLocation() {
        if (lastKnownLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return lastKnownLocation;
    }

    /**
     * Return true if Gps enabled on the device
     *
     * @return boolean bGPSEnabled
     */
    public boolean bGPSEnabledOnDevice() {
        return bGPSEnabled;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // if we are currently trying to get a location and the alarm manager has called this again,
        // no need to start processing a new location.
        if (!currentlyProcessingLocation) {
            currentlyProcessingLocation = true;
            startTracking();
        }

        return START_NOT_STICKY;
    }

    /**
     * Start the tracking if Google pLay service is available
     */
    private void startTracking() {
        Log.d(TAG, "startTracking");

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (googleApiClient != null) {
            //if its not in the state of connected or connecting reconnect it
            if (!googleApiClient.isConnected() || !googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        } else {
            Log.e(TAG, "unable to connect to google play services.");
        }
    }

    /**
     * Post the the location data to the web service
     *
     * @param location
     */
    protected void sendLocationDataToWebsite(Location location) {
        Preference preference = new Preference();

        // formatted for mysql datetime format
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date date = new Date(location.getTime());

        //Get the total distance in Meters  value
        float totalDistanceInMeters = preference.getDistance(context);

        //Get the first time getting position
        boolean firstTimeGettingPosition = preference.getFirstTimeLoad(context);

        //if its first time set it to false
        if (firstTimeGettingPosition) {
            preference.storeFirstTimeLoad(false, context);
        } else {

            //Construct the location object
            Location previousLocation = new Location("");

            //Set the previous latitude and longitude
            previousLocation.setLatitude(preference.getPreviousLatitude(context));
            previousLocation.setLongitude(preference.getPreviousLongitude(context));

            //Get the distance
            float distance = location.distanceTo(previousLocation);
            totalDistanceInMeters += distance;

            //Add the total distance in meters
            preference.storeDistance(totalDistanceInMeters, context);
        }
        //Save the previous lat and long with current lat and long to use it later in above else condition
        preference.storePreviousLatitutde((float) location.getLatitude(), context);
        preference.storePreviousLongitude((float) location.getLongitude(), context);

        //Construct the request params object
        final RequestParams requestParams = new RequestParams();
        //Add the latitude and longitude values
        requestParams.put("latitude", Double.toString(location.getLatitude()));
        requestParams.put("longitude", Double.toString(location.getLongitude()));

        //Add the speed
        Double speedInMilesPerHour = location.getSpeed() * 2.2369;
        requestParams.put("speed", Integer.toString(speedInMilesPerHour.intValue()));

        try {

            //Add the date
            requestParams.put("date", URLEncoder.encode(dateFormat.format(date), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error While Adding date in requset param : " + e.getMessage());
        }
        //Add the location method
        requestParams.put("locationmethod", location.getProvider());

        //Check the total distance in meters if its greater than 0 calculate it else put 0.0
        if (totalDistanceInMeters > 0) {
            requestParams.put("distance", String.format("%.1f", totalDistanceInMeters / 1609)); // in miles,
        } else {
            requestParams.put("distance", "0.0"); // in miles
        }

        //Add the user details
        requestParams.put("username", preference.getMemberName(context));
        requestParams.put("appID", preference.getRegistrationId(context)); // uuid

        //Add the accuracy in feet
        Double accuracyInFeet = location.getAccuracy() * 3.28;
        requestParams.put("accuracy", Integer.toString(accuracyInFeet.intValue()));

        //Add the altitude in feet
        Double altitudeInFeet = location.getAltitude() * 3.28;
        requestParams.put("extrainfo", Integer.toString(altitudeInFeet.intValue()));

        //Add the type of event
        requestParams.put("eventtype", "android");

        //Add the direction
        Float direction = location.getBearing();
        requestParams.put("direction", Integer.toString(direction.intValue()));
        requestParams.put("MemberID", preference.getMemberID(context));

        //Get the web service url
        final String uploadWebsite = preference.getLocationServiceURL(context) + "?request=locationUpdate";


        //Post the data asny
        LoopjHttpClient.get(uploadWebsite, requestParams, new AsyncHttpResponseHandler() {
            /**
             * Fired when a request returns successfully, override to handle in your own code
             *
             * @param statusCode   the status code of the response
             * @param headers      return headers, if any
             * @param responseBody the body of the HTTP response from the server
             */
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                LoopjHttpClient.debugLoopJ("sendLocationDataToWebsite - success", uploadWebsite, requestParams,
                        responseBody, headers, statusCode, null);

                stopSelf();
            }

            /**
             * Fired when a request fails to complete, override to handle in your own code
             *
             * @param statusCode   return HTTP status code
             * @param headers      return headers, if any
             * @param responseBody the response body, if any
             * @param error        the underlying cause of the failure
             */
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                LoopjHttpClient.debugLoopJ("sendLocationDataToWebsite - failure", uploadWebsite, requestParams,
                        responseBody, headers, statusCode, error);
                stopSelf();
                MyTrackerFragment
                        .getInstance()
                        .showTostMessage(
                                "Unable To Send Data To Web APP. \n Please make sure Web APP is running or you have a active internet connection. ");
            }


        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.e(TAG, "position: " + location.getLatitude() + ", " + location.getLongitude() + " accuracy: "
                    + location.getAccuracy());

            // we have our desired accuracy of 500 meters so lets quit this service,
            // onDestroy will be called and stop our location updates
            if (location.getAccuracy() < 500.0f) {
                stopLocationUpdates();
                sendLocationDataToWebsite(location);
                //Update the location text
                MyTrackerFragment.getInstance().updateLatLong(location.getLatitude(), location.getLongitude());
            }
        }
    }

    /**
     * Stop all location service and remove the location udpate
     */
    private void stopLocationUpdates() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    /**
     * Called by Location Services when the request to connect the client
     * finishes successfully. At this point, you can request the current
     * location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000); // milliseconds
        locationRequest.setFastestInterval(1000); // the fastest rate in milliseconds at which your app can handle location updates
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onDisconnected");

        stopLocationUpdates();
        stopSelf();
    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed");

        stopLocationUpdates();
        stopSelf();
    }
}
