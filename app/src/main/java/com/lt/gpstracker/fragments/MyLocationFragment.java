package com.lt.gpstracker.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.lt.gpstracker.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lt.gpstracker.services.GLocationService;
import com.lt.gpstracker.utility.Preference;

public class MyLocationFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MyLocationFragment";
    // Google Map
    private GoogleMap googleMap;
    MapFragment mapFragment;
    private Spinner spinner = null;
    private View customView = null;

    public MyLocationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_locations, container, false);
        try {

            // Check status of Google Play Services
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
            if (status != ConnectionResult.SUCCESS) {
                GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            } else {

                spinner = (Spinner) rootView.findViewById(R.id.map_type);
                spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        String mapType = spinner.getItemAtPosition(arg2).toString();
                        changeMap(mapType);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                        //DO Nothing
                    }
                });

                //Enable display menu on action bar
                setHasOptionsMenu(true);
                // Loading map
                initilizeMap();

                //if tracking is on and Glocation Serivce instance is not null then we add marker with user
                //Last known position
                Preference preference = new Preference();
                boolean isTrackingON = preference.getCurrenltyTracking(getActivity());
                if (isTrackingON) {
                    //Add the marker
                    if (GLocationService.getServiceInstance() != null) {
                        Location location = GLocationService.getServiceInstance().GetLocation();
                        addMarker(location.getLatitude(), location.getLongitude());
                    }
                }
            }
        } catch (IllegalStateException e) {
            Log.e(TAG, "Error : " + e);
        } catch (NullPointerException e) {
            Log.e(TAG, "Error onCreateView: " + e);
        } catch (Exception e) {
            Log.e(TAG, "Error onCreateView: " + e);
        }
        return rootView;
    }

    /**
     * Change the type of map
     *
     * @param type
     */
    private void changeMap(String type) {
        try {
            if (googleMap != null) {
                if (type.equalsIgnoreCase("NORMAL")) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else if (type.equalsIgnoreCase("HYBRID")) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                } else if (type.equalsIgnoreCase("SATELLITE")) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                } else if (type.equalsIgnoreCase("TERRAIN")) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                } else if (type.equalsIgnoreCase("NONE")) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                }
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "Error changeMap: " + e);
        } catch (Exception e) {
            Log.e(TAG, "Error changeMap: " + e);
        }
    }

    @Override
    public void onResume() {
        initilizeMap();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    /**
     * Add marker on map
     *
     * @param latitude
     * @param longitude
     */
    private void addMarker(double latitude, double longitude) {
        try {
            Preference preference = new Preference();
            if (googleMap != null) {
                String markerTitle = preference.getMemberName(getActivity()) + " " + getString(R.string.marker_message);
                // create marker
                MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(markerTitle);

                // adding marker
                googleMap.addMarker(marker);
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "Error onCreateView: " + e);
        } catch (Exception e) {
            Log.e(TAG, "Error onCreateView: " + e);
        }
    }

    /**
     * function to load map. If map is not created it will create it for you
     */
    private void initilizeMap() {
        try {

            if (mapFragment == null) {
                mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

                if (mapFragment == null) {
                    showMaterialDialog("Sorry! unable to create map");
                    return;
                }
                mapFragment.getMapAsync(this);
            }
        } catch (IllegalStateException e) {
            showMaterialDialog(e.getMessage());
            Log.e(TAG, "Error initilizeMap: " + e);
        } catch (Exception e) {
            showMaterialDialog(e.getMessage());
            Log.e(TAG, "Error initilizeMap: " + e);
        }
    }

    /**
     * Enable Location if its already enable then it will disable it
     */
    private void enableLocation() {
        try {
            if (googleMap.isMyLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                Toast.makeText(getActivity(), "Current Location Disabled", Toast.LENGTH_LONG).show();
            } else {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                Toast.makeText(getActivity(), "Current Location Enabled. \n Click on Location Button on Map  ",
                        Toast.LENGTH_LONG).show();
            }
        } catch (IllegalStateException e) {
            showMaterialDialog(e.getMessage());
            Log.e(TAG, "Error enableLocation: " + e);
        } catch (NullPointerException e) {
            showMaterialDialog(e.getMessage());
            Log.e(TAG, "Error enableLocation: " + e);
        } catch (Exception e) {
            showMaterialDialog(e.getMessage());
            Log.e(TAG, "Error enableLocation: " + e);
        }
    }

    /**
     * Enable Zoom. if its already enable then it will disable it
     */
    private void enableZoom() {
        try {

            if (googleMap.getUiSettings().isZoomControlsEnabled()) {
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                googleMap.getUiSettings().setZoomGesturesEnabled(false);
                Toast.makeText(getActivity(), "Zoom Gesture Disabled", Toast.LENGTH_LONG).show();

            } else {
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setZoomGesturesEnabled(true);
                Toast.makeText(getActivity(), "Zoom Gesture Enabled", Toast.LENGTH_LONG).show();
            }
        } catch (IllegalStateException e) {
            showMaterialDialog(e.getMessage());
            Log.e(TAG, "Error enableZoom: " + e);
        } catch (NullPointerException e) {
            showMaterialDialog(e.getMessage());
            Log.e(TAG, "Error enableZoom: " + e);
        } catch (Exception e) {
            showMaterialDialog(e.getMessage());
            Log.e(TAG, "Error enableZoom: " + e);
        }
    }

    /**
     * Enable Map Rotate Gesture. if its already enable then it will disable it
     */
    private void enableMapRotateGesture() {
        try {
            if (googleMap.getUiSettings().isRotateGesturesEnabled()) {
                googleMap.getUiSettings().setRotateGesturesEnabled(false);
                Toast.makeText(getActivity(), "Map Rotate Gesture Disabled", Toast.LENGTH_LONG).show();
            } else {
                googleMap.getUiSettings().setRotateGesturesEnabled(true);
                Toast.makeText(getActivity(), "Map Rotate Gesture Enabled", Toast.LENGTH_LONG).show();
            }
        } catch (IllegalStateException e) {
            Log.e(TAG, "Error enableZoom: " + e);
        } catch (NullPointerException e) {
            showMaterialDialog(e.getMessage());
            Log.e(TAG, "Error enableZoom: " + e);
        } catch (Exception e) {
            Log.e(TAG, "Error enableZoom: " + e);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.location_item:
                enableLocation();
                return true;
            case R.id.zoom_item:
                enableZoom();
                return true;
            case R.id.map_rotate:
                enableMapRotateGesture();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void showMaterialDialog(String message) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

            // Setting Dialog Title
            alertDialog.setTitle(getString(R.string.app_name));

            // Setting Dialog Message
            alertDialog.setMessage(message);

            // Setting alert dialog icon
            //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener
                    () {
                public void onClick(DialogInterface dialog, int which) {

                }
            });


            // Showing Alert Message
            alertDialog.show();
        } catch (NullPointerException e) {
            Log.e(TAG, "Error in showMaterialDialog : " + e);
        } catch (Exception e) {
            Log.e(TAG, "Error  in showMaterialDialog: " + e);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        googleMap.setMyLocationEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
    }
}
