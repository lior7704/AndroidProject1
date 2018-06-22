package com.example.lior7.project1.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lior7.project1.Activities.HighScoresActivity;
import com.example.lior7.project1.Object_Classes.UserDetails;
import com.example.lior7.project1.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback {


    MapView mapView;
    private GoogleMap googleMap;
    private List<UserDetails> userDetailsList = new ArrayList<>();

    private LatLng lastLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        userDetailsList = ((HighScoresActivity)getActivity()).userDetailsList;

        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);

        return rootView;
    }

    @SuppressLint("MissingPermission")
    private void checkPermissions() {
        if(HighScoresActivity.givePermission)
            googleMap.setMyLocationEnabled(true);
        else
            googleMap.setMyLocationEnabled(false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        checkPermissions();

        DisplayHighScores();
    }

    private void DisplayHighScores() {
        for (UserDetails userDetails : userDetailsList) {
            if(userDetails.getLatitude() != -1)
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(userDetails.getLatitude(), userDetails.getLongitude()))
                        .title(userDetails.getName() + "\t-\t" + String.valueOf(userDetails.getScore())));
        }
        setMapAtMyLocation();
    }

    public void setMapAtMyLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        else{
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                if (location != null) {
                    lastLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(lastLocation.latitude, lastLocation.longitude, 1);
                        if (addressList.size() > 0) {
                            googleMap.addMarker(new MarkerOptions()
                                .position(lastLocation)
                                .title(getString(R.string.curr_location)));
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(lastLocation).zoom(12).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                }
            });
        }
    }

    //region Override Fragment Methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    //endregion
}
