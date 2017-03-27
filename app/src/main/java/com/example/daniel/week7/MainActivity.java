package com.example.daniel.week7;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager mLocationManager;
    private String TAG= "LocalizacionApp";
    private TextView Latitud,Longitud;
    private final int MY_PERMISSIONS_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Latitud= (TextView) findViewById(R.id.textViewLatitud);
        Longitud=(TextView) findViewById(R.id.textViewLongitud);
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    public void onClickGo(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"Faltan Permisos");
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSIONS_REQUEST);
            return;
        }
        final LocationManager manager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps();
        }else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id){
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener(){
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id){
                        dialog.cancel();
                    }
                });
        final AlertDialog alert=builder.create();
        alert.show();
    }

    public void onClickStop(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"Faltan Permisos");
            return;
        }
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onDestroy() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"Faltan Permisos");
            return;
        }
        mLocationManager.removeUpdates(this);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],int[] grantResults) {
        Log.d(TAG,"MainActivity onRequestPermissionsResult requestCode "+requestCode);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST: {
                if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Perfecto",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this,"???",Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Latitud.setText(location.getLatitude()+"");
        Longitud.setText(location.getLongitude()+"");

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}
