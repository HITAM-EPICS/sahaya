package hitam.epics.sahaya.admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import hitam.epics.sahaya.R;
import hitam.epics.sahaya.support.CenterItem;
import hitam.epics.sahaya.support.CentersAdapter;
import hitam.epics.sahaya.support.EventItem;
import hitam.epics.sahaya.volunteer.CenterDetailsActivity;

public class AdminCenterManagementActivity extends AppCompatActivity {

    private final int PLACE_PICKER_REQUEST = 1;
    private CentersAdapter adapter;
    private EditText centerNameEditText;
    private AlertDialog alertDialog;
    private String centerName;
    private ArrayList<CenterItem> centers;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_center_management);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("/centers/");

        ListView centerListView = (ListView) findViewById(R.id.centers_list);
        centerListView.setEmptyView(findViewById(R.id.center_empty_view));
        centers = new ArrayList<>();

        adapter = new CentersAdapter(this, centers);
        centerListView.setAdapter(adapter);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                centers.add(dataSnapshot.getValue(CenterItem.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                EventItem value = dataSnapshot.getValue(EventItem.class);
                for (CenterItem item : centers) {
                    if (item.toString().equals(value.toString())) {
                        centers.remove(item);
                        break;
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        centerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CenterItem centerItem = centers.get(position);
                Intent intent = new Intent(AdminCenterManagementActivity.this, CenterDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("center_name", centerItem.getName());
                extras.putString("center_location", centerItem.getLocation());
                extras.putString("center_address", centerItem.getAddress());
                extras.putDouble("center_latitude", centerItem.getLat());
                extras.putDouble("center_longitude", centerItem.getLon());
                extras.putBoolean("admin", true);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

    }

    public void addCenter(View view) {
        View alertView = getLayoutInflater().inflate(R.layout.admin_new_center, null);
        centerNameEditText = (EditText) alertView.findViewById(R.id.center_name);
        Button addCenterButton = (Button) alertView.findViewById(R.id.add_center_button);
        Button cancelAddCenterButton = (Button) alertView.findViewById(R.id.cancel_add_center_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(alertView)
                .setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();

        addCenterButton.setOnClickListener(addCenterButtonClick());
        cancelAddCenterButton.setOnClickListener(cancelAddCenterButtonClick());
    }

    private View.OnClickListener cancelAddCenterButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        };
    }

    private View.OnClickListener addCenterButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centerName = centerNameEditText.getText().toString().trim();
                if (centerName.length() == 0) {
                    centerNameEditText.setError("Name Required");
                    return;
                }

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(AdminCenterManagementActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                reference.child(centerName + "-" + place.getName().toString()).setValue(
                        new CenterItem(
                                centerName,
                                place.getLatLng().latitude,
                                place.getLatLng().longitude,
                                place.getName().toString(),
                                place.getAddress().toString()
                        )
                );
                alertDialog.cancel();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
