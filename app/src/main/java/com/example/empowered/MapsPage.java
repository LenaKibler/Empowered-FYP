package com.example.empowered;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.paris.Paris;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.empowered.databinding.ActivityMapsPageBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MapsPage extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsPageBinding binding;
    private BottomNavigationView bottomNavigationView;
    String value;
    String formattedDate;
    ExtendedFloatingActionButton mainKey, elecOnly, dualFuel, coolOff, inProgress;
    Boolean isAllFabsVisible;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_logo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1A759F")));

        binding = ActivityMapsPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mainKey = findViewById(R.id.mainKey);
        elecOnly = findViewById(R.id.elecOnly);
        dualFuel = findViewById(R.id.dualFuel);
        coolOff = findViewById(R.id.coolOff);
        inProgress = findViewById(R.id.inProgress);

        elecOnly.setVisibility(View.GONE);
        dualFuel.setVisibility(View.GONE);
        coolOff.setVisibility(View.GONE);
        inProgress.setVisibility(View.GONE);

        mainKey.shrink();

        isAllFabsVisible = false;

        mainKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isAllFabsVisible){

                    elecOnly.show();
                    dualFuel.show();
                    coolOff.show();
                    inProgress.show();

                    mainKey.extend();

                    isAllFabsVisible = true;
                }else{

                    elecOnly.hide();
                    dualFuel.hide();
                    coolOff.hide();
                    inProgress.hide();

                    mainKey.shrink();

                    isAllFabsVisible = false;
                }
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_black_mode));

        //users current location
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


        //Dual fuel markers in housing estates in Tallaght
        LatLng tallaght = new LatLng(53.282493387306225, -6.355186526732735);
        mMap.addMarker(new MarkerOptions().position(tallaght).title("Millbrook Lawns")
                .icon(BitmapFromVector(getApplicationContext(), R.drawable.electrivityandgas_img)));

        LatLng tallaght1 = new LatLng(53.2915869046008, -6.339506856425444);
        mMap.addMarker(new MarkerOptions().position(tallaght1).title("Balrothery")
                .icon(BitmapFromVector(getApplicationContext(), R.drawable.electrivityandgas_img)));

        LatLng tallaght2 = new LatLng(53.30349742956821, -6.371455265341584);
        mMap.addMarker(new MarkerOptions().position(tallaght2).title("Kingswood Heights")
                .icon(BitmapFromVector(getApplicationContext(), R.drawable.electrivityandgas_img)));


        //Electricity only markers in Knocklyon
        LatLng knocklyon = new LatLng(53.27402396102162, -6.314584754262116);
        mMap.addMarker(new MarkerOptions().position(knocklyon).title("White Pines")
                .icon(BitmapFromVector(getApplicationContext(), R.drawable.elec)));



        //adding on click listeners to the markers
        mMap.setOnMarkerClickListener(marker -> {

            //getting title of marker and assigning it to string variable
            String markerName = marker.getTitle();
            String markerCompleted = "IN COOLING OFF PERIOD";
            String markerInProgress = "IN PROGRESS";
            EditText doorNr = new EditText(MapsPage.this);
            TextView dNr = new EditText(MapsPage.this);
            dNr.setEnabled(false);
            dNr.setKeyListener(null);


            //if the title of the marker is equals to markerCompleted then do the following:
            if (marker.getTitle().equals(markerCompleted)) {


                AlertDialog.Builder altBuilder = new AlertDialog.Builder(MapsPage.this);
                altBuilder.setTitle(markerName);
                altBuilder.setMessage("You have completed this area. " +
                        "It is now in a 2 month cool off period. " +
                        "The date of completion is: " + formattedDate)
                        .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                altBuilder.create();
                altBuilder.show();

                //if the title of the marker is not equals to markerCompleted and MarkerInProgress
                //then do following:
            }else if(!marker.getTitle().equals(markerCompleted) && !marker.getTitle().equals(markerInProgress)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MapsPage.this);
                builder.setTitle(markerName);
                builder.setMessage("Have you completed this area ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                marker.setIcon((BitmapFromVector(getApplicationContext(), R.drawable.timer)));
                                Date currentD = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                formattedDate = df.format(currentD);

                                builder.setTitle(markerName + " IN COOLING OFF PERIOD");
                                builder.setMessage("You have completed this area." +
                                        "It is now in a 2 month cool-off period." +
                                        "The date of completion is: " + formattedDate);
                                builder.setPositiveButton("", null);
                                builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                marker.setTitle(markerCompleted);

                                builder.show();

                                new CountDownTimer(30000, 1000) {

                                    public void onTick(long countDown) {


                                    }

                                    public void onFinish() {

                                        marker.setIcon((BitmapFromVector(getApplicationContext(), R.drawable.electrivityandgas_img)));

                                        marker.setTitle(markerName);

                                    }
                                }.start();

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                LinearLayout linearLayout = new LinearLayout(MapsPage.this);
                                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                Paris.style(linearLayout).apply(R.style.container);

                                Button btn = new Button(MapsPage.this);
                                btn.setText("Add");
                                btn.setBackgroundColor(getResources().getColor(R.color.white));
                                btn.setTextColor(getResources().getColor(R.color.color10));

                                linearLayout.addView(doorNr);
                                linearLayout.addView(dNr);
                                linearLayout.addView(btn);

                                AlertDialog.Builder eBuilder = new AlertDialog.Builder(MapsPage.this);
                                eBuilder.setTitle(markerName);
                                eBuilder.setMessage("Are you currently in process of completing this area")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                eBuilder.setTitle(markerInProgress);
                                                eBuilder.setMessage("This area is now in a 'IN PROGRESS' state." +
                                                        "What is the number of the last door you knocked ?");
                                                eBuilder.setView(linearLayout);
                                                dNr.setVisibility(View.GONE);

                                                btn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                        value = doorNr.getText().toString();
                                                        dNr.setText(value);
                                                        dNr.setVisibility(View.VISIBLE);
                                                        doorNr.setVisibility(View.GONE);
                                                    }
                                                });


                                                eBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        marker.setIcon((BitmapFromVector(getApplicationContext(), R.drawable.in_progress)));
                                                        marker.setTitle(markerInProgress);
                                                    }
                                                });
                                                eBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                });
                                                eBuilder.show();

                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {


                                            }
                                        });

                                eBuilder.create();
                                eBuilder.show();

                            }
                        });

                builder.create();
                builder.show();

                //if the title of the marker if equals to markerInProgress then do the following:
            } else if (marker.getTitle().equals(markerInProgress)){


                LinearLayout linearLayout = new LinearLayout(MapsPage.this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                Paris.style(linearLayout).apply(R.style.container);

                Button btn = new Button(MapsPage.this);
                btn.setText("Edit");
                btn.setBackgroundColor(getResources().getColor(R.color.white));
                btn.setTextColor(getResources().getColor(R.color.color10));

                Button btnSave = new Button(MapsPage.this);
                btnSave.setText("Save");
                btnSave.setBackgroundColor(getResources().getColor(R.color.white));
                btnSave.setTextColor(getResources().getColor(R.color.color10));

                linearLayout.addView(dNr);
                linearLayout.addView(btn);
                linearLayout.addView(btnSave);

                AlertDialog.Builder iBuilder = new AlertDialog.Builder(MapsPage.this);
                iBuilder.setTitle(markerInProgress);
                iBuilder.setMessage("You are currently making your way through this area. " +
                        "Please press the 'Completed' button if you have finished this area "
                        + "The most recent door that was knocked is: ");
                dNr.setText(value);
                iBuilder.setView(linearLayout);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dNr.setEnabled(true);

                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dNr.setEnabled(false);

                    }
                });
                iBuilder.setPositiveButton("Completed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        marker.setTitle(markerCompleted);
                        marker.setIcon((BitmapFromVector(getApplicationContext(), R.drawable.timer)));
                        Date currentD = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        formattedDate = df.format(currentD);

                        new CountDownTimer(30000, 1000) {

                            public void onTick(long countDown) {


                            }

                            public void onFinish() {

                                marker.setIcon((BitmapFromVector(getApplicationContext(), R.drawable.electrivityandgas_img)));

                                marker.setTitle(markerName);

                            }
                        }.start();

                    }
                })
                        .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                iBuilder.create();
                iBuilder.show();
            }
            return false;
        });

    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {

        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //Menu Navigation Method
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {


                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {

                    switch (menuitem.getItemId()) {

                        case R.id.map:
                            //startActivity(new Intent(MapsPage.this, MapPage.class));
                            break;

                        case R.id.sales:

                            Intent intentSales = new Intent(MapsPage.this, SalesFlowPage.class);
                            //The purpose of this flag is to bring existing activity instance to the
                            //foreground if one already exists
                            //This ensures that contents created by user in different activities
                            //won't be deleted as the user navigates through the app
                            intentSales.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intentSales);

                            break;

                        case R.id.logout:

                            //Firebase logout functionality
                            FirebaseAuth.getInstance().signOut();

                            //Navigate user back to login page
                            startActivity(new Intent(MapsPage.this, LoginPage.class));

                            break;

                        case R.id.calculator:

                            Intent intentCalculator = new Intent(MapsPage.this, CalculatorPage.class);
                            intentCalculator.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intentCalculator);

                            break;

                    }
                    return false;
                }
            };
}