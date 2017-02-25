package hitam.epics.sahaya;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class EventDetailActivity extends Activity {

    private String event_name;
    private String event_date;
    private String event_time;
    private String event_desc;
    private double event_latitude;
    private double event_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        TextView eventNameView = (TextView) findViewById(R.id.timetable_event_name);
        TextView eventDateView = (TextView) findViewById(R.id.timetable_event_date);
        TextView eventTimeView = (TextView) findViewById(R.id.timetable_event_time);
        TextView eventDescView = (TextView) findViewById(R.id.timetable_event_desc);
        MapView mapView = (MapView) findViewById(R.id.map);

        Bundle extras = getIntent().getExtras();
        event_name = extras.getString("event_name");
        event_date = extras.getString("event_date");
        event_time = extras.getString("event_time");
        event_desc = extras.getString("event_desc");
        event_latitude = extras.getDouble("event_latitude");
        event_longitude = extras.getDouble("event_longitude");
        if (extras.getBoolean("admin")) {
            findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
        }

        eventNameView.setText(event_name);
        eventDateView.setText(event_date);
        eventTimeView.setText(event_time);
        eventDescView.setText(event_desc);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(event_latitude, event_longitude))
                        .title(event_desc));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(event_latitude, event_longitude), 15));
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
        mapView.onResume();


    }

    public void directions(View view) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + event_latitude + "," + event_longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void delete(View view) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/events/" + event_date + event_time.split("-")[0] + event_name);
        reference.removeValue();
        finish();
    }

}
