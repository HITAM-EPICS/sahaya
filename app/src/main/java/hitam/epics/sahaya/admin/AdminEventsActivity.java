package hitam.epics.sahaya.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hitam.epics.sahaya.R;
import hitam.epics.sahaya.support.EventItem;
import hitam.epics.sahaya.support.EventsAdapter;
import hitam.epics.sahaya.volunteer.EventDetailActivity;

public class AdminEventsActivity extends Activity {

    private final int PLACE_PICKER_REQUEST = 1;
    private EventsAdapter adapter;
    private EditText eventNameEditText;
    private EditText eventDateEditText;
    private EditText eventStartTimeEditText;
    private EditText eventEndTimeEditText;
    private AlertDialog alertDialog;
    private String eventName;
    private String eventDate;
    private String eventStartTime;
    private String eventEndTime;
    private ArrayList<EventItem> events;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_events);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("/events/");

        ListView evenListView = (ListView) findViewById(R.id.event_list);
        evenListView.setEmptyView(findViewById(R.id.events_empty_view));
        events = new ArrayList<>();

        adapter = new EventsAdapter(this, events);
        evenListView.setAdapter(adapter);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                events.add(dataSnapshot.getValue(EventItem.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                EventItem value = dataSnapshot.getValue(EventItem.class);
                for (EventItem item : events) {
                    if (item.toString().equals(value.toString())) {
                        events.remove(item);
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

        evenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventItem eventItem = events.get(position);
                Intent intent = new Intent(AdminEventsActivity.this, EventDetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString("event_name", eventItem.getName());
                extras.putString("event_date", eventItem.getDate());
                extras.putString("event_time", eventItem.getStart() + "-" + eventItem.getEnd());
                extras.putString("event_desc", eventItem.getArea());
                extras.putDouble("event_latitude", eventItem.getLat());
                extras.putDouble("event_longitude", eventItem.getLon());
                extras.putBoolean("admin", true);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }

    public void addEvent(View view) {
        View alertView = getLayoutInflater().inflate(R.layout.admin_new_event, null);
        eventNameEditText = (EditText) alertView.findViewById(R.id.event_name);
        eventDateEditText = (EditText) alertView.findViewById(R.id.event_date);
        eventStartTimeEditText = (EditText) alertView.findViewById(R.id.event_start_time);
        eventEndTimeEditText = (EditText) alertView.findViewById(R.id.event_end_time);
        Button addEventButton = (Button) alertView.findViewById(R.id.add_event_button);
        Button cancelAddEventButton = (Button) alertView.findViewById(R.id.cancel_add_event_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(alertView)
                .setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();

        addEventButton.setOnClickListener(addEventButtonClick());
        cancelAddEventButton.setOnClickListener(cancelAddEventButtonClick());
    }

    private View.OnClickListener cancelAddEventButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        };
    }

    private View.OnClickListener addEventButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventName = eventNameEditText.getText().toString().trim();
                eventDate = eventDateEditText.getText().toString().trim();
                eventStartTime = eventStartTimeEditText.getText().toString().trim();
                eventEndTime = eventEndTimeEditText.getText().toString().trim();
                boolean isValid = true;
                if (eventName.length() == 0) {
                    eventNameEditText.setError("Name Required");
                    isValid = false;
                }

                if (eventDate.length() == 0) {
                    eventDateEditText.setError("Date Required");
                    isValid = false;
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    dateFormat.setLenient(false);
                    try {
                        Date parse = dateFormat.parse(eventDate);
                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                        eventDate = dateFormat1.format(parse);
                        if (eventDate.charAt(7) == '0') {
                            String[] split = eventDate.split("-");
                            split[2] = split[2].replaceFirst("0", "2");
                            eventDate = split[0] + '-' + split[1] + '-' + split[2];
                        }
                    } catch (ParseException pe) {
                        eventDateEditText.setError("Invalid Date");
                        isValid = false;
                    }
                }

                if (eventStartTime.length() == 0) {
                    eventStartTimeEditText.setError("Time Required");
                    isValid = false;
                } else {
                    String[] time = eventStartTime.split(":");
                    if (time.length != 2) {
                        eventStartTimeEditText.setError("Invalid Time");
                        isValid = false;
                    } else if (Integer.parseInt(time[0]) > 23 || Integer.parseInt(time[0]) < 0 || Integer.parseInt(time[1]) > 59 || Integer.parseInt(time[1]) < 0) {
                        eventStartTimeEditText.setError("Invalid Time");
                        isValid = false;
                    }
                }

                if (eventEndTime.length() == 0) {
                    eventEndTimeEditText.setError("Time Required");
                    isValid = false;
                } else {
                    String[] time = eventStartTime.split(":");
                    if (time.length != 2) {
                        eventEndTimeEditText.setError("Invalid Time");
                        isValid = false;
                    } else if (Integer.parseInt(time[0]) > 23 || Integer.parseInt(time[0]) < 0 || Integer.parseInt(time[1]) > 59 || Integer.parseInt(time[1]) < 0) {
                        eventEndTimeEditText.setError("Invalid Time");
                        isValid = false;
                    }
                }

                if (!isValid) {
                    return;
                }

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(AdminEventsActivity.this), PLACE_PICKER_REQUEST);
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
                reference.child(eventDate + eventStartTime + eventName).setValue(new EventItem(eventName, eventDate, eventStartTime, eventEndTime, place.getName() + ", " + place.getAddress().toString(), place.getLatLng().latitude, place.getLatLng().longitude));
                alertDialog.cancel();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
