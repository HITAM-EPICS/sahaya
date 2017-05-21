package hitam.epics.sahaya.volunteer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hitam.epics.sahaya.R;

public class CenterDetailsActivity extends AppCompatActivity {


    private String center_name;
    private String center_location;
    private String center_address;
    private double center_latitude;
    private double center_longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_details);

        TextView centerNameView = (TextView) findViewById(R.id.center_name);
        TextView centerAddressView = (TextView) findViewById(R.id.center_address);
        MapView mapView = (MapView) findViewById(R.id.map);

        Bundle extras = getIntent().getExtras();
        center_name = extras.getString("center_name");
        center_location = extras.getString("center_location");
        center_address = extras.getString("center_address");
        center_latitude = extras.getDouble("center_latitude");
        center_longitude = extras.getDouble("center_longitude");
        if (extras.getBoolean("admin")) {
            findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
        }

        centerNameView.setText(center_name);
        centerAddressView.setText(center_address);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(center_latitude, center_longitude))
                        .title(center_address));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(center_latitude, center_longitude), 15));
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
        mapView.onResume();
    }

    public void directions(View view) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + center_latitude + "," + center_longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void delete(View view) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/centers/" + center_name + "-" + center_location);
        reference.removeValue();
        finish();
    }

}
