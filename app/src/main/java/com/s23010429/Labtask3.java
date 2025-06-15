package com.s23010429;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Labtask3 extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText editTextAddress;
    private Button buttonShowLocation;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labtask3);

        // Initialize UI components
        editTextAddress = findViewById(R.id.editTextAddress);
        buttonShowLocation = findViewById(R.id.buttonShowLocation);

        // Initialize Geocoder
        geocoder = new Geocoder(this, Locale.getDefault());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set up button click listener
        buttonShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressOnMap();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set default map settings
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Default location (e.g., San Francisco)
        LatLng defaultLocation = new LatLng(37.7749, -122.4194);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
    }

    /**
     * Gets the address from EditText, geocodes it, and shows it on the map
     */
    private void showAddressOnMap() {
        String addressString = editTextAddress.getText().toString().trim();

        if (addressString.isEmpty()) {
            Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Use Geocoder to get latitude and longitude from address
            List<Address> addressList = geocoder.getFromLocationName(addressString, 1);

            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);

                // Get latitude and longitude
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                LatLng location = new LatLng(latitude, longitude);

                // Clear previous markers
                mMap.clear();

                // Add marker and move camera
                mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(addressString));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error finding location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}