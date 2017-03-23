package com.lab2.lab2nav;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteCursor;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class mapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button searchButton;
    MapView mMapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                mMap = map;
                ArrayList<String> markers = new ArrayList<String>();
                markers = new DbHelper(getActivity().getApplicationContext()).getAllData();
                double lat=0, lon=0;

                for(int index=0; index<markers.size(); index++) {
                    StringTokenizer token = new StringTokenizer(markers.get(index),",");
                    String id = token.nextToken();
                    String property= token.nextToken();
                    String street= token.nextToken();
                    String state=token.nextToken();
                    String city = token.nextToken();
                    String zip=token.nextToken();
                    String loan=token.nextToken();
                    String apr =token.nextToken();
                    String monthly =token.nextToken();
                    lat = Double.parseDouble(token.nextToken());
                    lon = Double.parseDouble(token.nextToken());
                    property=property.equals("#")?"":property;
                    street=street.equals("#")?"":street;
                    state=state.equals("#")?"":state;
                    city=city.equals("#")?"":city;
                    zip=zip.equals("#")?"":zip;
                    loan=loan.equals("#")?"":loan;
                    apr=apr.equals("#")?"":apr;
                    monthly=monthly.equals("#")?"":monthly;

                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(lat,lon))
                                            .title(property)
                                            .snippet(
                                              street+" "+ city+" "+state+" "+zip+"\n"+"Loan: "+loan+"\n"+"APR: "+apr+"\nMonthly payment: "+monthly
                                            ));
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lon)));
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        //Toast.makeText(getActivity().getApplicationContext(),marker.getSnippet(),Toast.LENGTH_SHORT).show();
                        showDialog(marker.getSnippet());
                        return false;
                    }
                });
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void showDialog(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Morgage Info");
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.mipmap.ic_launcher_round);
        alertDialog.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.create().show();
    }
}
