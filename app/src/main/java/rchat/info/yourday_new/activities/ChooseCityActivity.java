package rchat.info.yourday_new.activities;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.containers.weather.Weather;
import rchat.info.yourday_new.others.Connection;
import rchat.info.yourday_new.others.SaveSharedPreferences;

public class ChooseCityActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;
    LatLng chosenLocation;
    FloatingActionButton button;
    String locName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkPlayServices()) {
            setContentView(R.layout.choose_city);
            button = findViewById(R.id.floatingActionButton);
            button.hide();
            locName = "";
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SaveSharedPreferences.setWeatherInfo(ChooseCityActivity.this, locName, chosenLocation);
                    onBackPressed();
                }
            });
            searchView = findViewById(R.id.search);
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.google_map);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    String loc = searchView.getQuery().toString();
                    List<Address> addressList = null;
                    if (loc != null || !loc.equals("")) {
                        try {
                            Geocoder geocoder = new Geocoder(ChooseCityActivity.this);
                            try {
                                addressList = geocoder.getFromLocationName(loc, 1);
                            } catch (IOException e) {

                            }
                            Address address = addressList.get(0);
                            locName = address.getLocality();
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            map.clear();
                            map.addMarker(new MarkerOptions().position(latLng).title(locName));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            chosenLocation = latLng;
                            button.show();
                        } catch (Exception e) {
                            button.hide();
                        }
                    } else {
                        button.hide();
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            mapFragment.getMapAsync(this);

        } else {
            Toast.makeText(this, "На Вашем устройстве отсутствуют Google Play сервисы", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                try {
                    String location;
                    List<Address> addressList = null;
                    Geocoder geocoder = new Geocoder(ChooseCityActivity.this);
                    addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    location = addressList.get(0).getLocality();
                    if (location == null) {
                        throw new IOException("This is not a city!");
                    }
                    map.clear();
                    map.addMarker(new MarkerOptions().position(latLng).title(addressList.get(0).getLocality()));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    chosenLocation = latLng;
                    locName = addressList.get(0).getLocality();
                    button.show();
                } catch (Exception e) {
                    e = e;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Connection connection = Connection.getConnection();
        for (int i = 0; i < connection.props.size(); i++) {
            if (connection.props.get(i) instanceof Weather) {
                connection.props.remove(i);
                break;
            }
        }
        if (locName == null) {
            Toast.makeText(this, "Произошла ошибка. Попробуйте выбрать другой город", Toast.LENGTH_SHORT).show();
        } else {
            if (locName.equals("")) {
                if (SaveSharedPreferences.getCityName(this) == null) {
                    SaveSharedPreferences.setWeatherInfo(this, "", new LatLng(0, 0));
                }
            } else if (!locName.equals("")) {
                SaveSharedPreferences.setWeatherInfo(this, locName, chosenLocation);
            }
            super.onBackPressed();
        }

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability gApi = GoogleApiAvailability.getInstance();
        int resultCode = gApi.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            return false;
        } else {
            return true;
        }
    }
}