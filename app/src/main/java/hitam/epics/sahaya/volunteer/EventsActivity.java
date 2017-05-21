package hitam.epics.sahaya.volunteer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hitam.epics.sahaya.R;
import hitam.epics.sahaya.support.EventItem;
import hitam.epics.sahaya.support.EventsAdapter;

public class EventsActivity extends Activity {
    private ArrayList<String> eventDateList;
    private ArrayList<EventItem> currentDateEventList;
    private ArrayList<EventItem> eventList;
    private EventsAdapter adapter;
    private MaterialCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("/events/");

        eventDateList = new ArrayList<>();
        eventList = new ArrayList<>();
        currentDateEventList = new ArrayList<>();

        adapter = new EventsAdapter(this, currentDateEventList);
        GridView eventListGridView = (GridView) findViewById(R.id.event_list);
        eventListGridView.setAdapter(adapter);
        eventListGridView.setEmptyView(findViewById(R.id.empty_view));

        calendarView = (MaterialCalendarView) findViewById(R.id.calendar);
        calendarView.setCurrentDate(new Date());

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                currentDateEventList.clear();
                for (EventItem item : eventList) {
                    if (item.getDate().equals(simpleDateFormat.format(date.getDate()))) {
                        currentDateEventList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        eventListGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventItem eventItem = currentDateEventList.get(position);
                Intent intent = new Intent(EventsActivity.this, EventDetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString("event_name", eventItem.getName());
                extras.putString("event_date", eventItem.getDate());
                extras.putString("event_time", eventItem.getStart() + "-" + eventItem.getEnd());
                extras.putString("event_desc", eventItem.getArea());
                extras.putDouble("event_latitude", eventItem.getLat());
                extras.putDouble("event_longitude", eventItem.getLon());
                extras.putBoolean("admin", false);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        calendarView.setSelectedDate(new Date());

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                eventList.add(dataSnapshot.getValue(EventItem.class));
                Log.e("onChildAdded: ", dataSnapshot.getValue(EventItem.class).toString());
                eventDateList.clear();
                for (EventItem item : eventList) {
                    eventDateList.add(item.getDate());
                }
                adapter.notifyDataSetChanged();
                calendarView.removeDecorators();
                calendarView.addDecorator(new DayViewDecorator() {
                    @Override
                    public boolean shouldDecorate(CalendarDay day) {
                        Date date = day.getDate();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        return eventDateList.contains(simpleDateFormat.format(date));
                    }

                    @Override
                    public void decorate(DayViewFacade view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            view.setBackgroundDrawable(getDrawable(R.drawable.round));
                        } else {
                            view.setBackgroundDrawable(new ColorDrawable(Color.GREEN));
                        }
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                EventItem value = dataSnapshot.getValue(EventItem.class);
                for (EventItem item : eventList) {
                    if (item.toString().equals(value.toString())) {
                        eventList.remove(item);
                        break;
                    }
                }
                eventDateList.clear();
                for (EventItem item : eventList) {
                    eventDateList.add(item.getDate());
                }

                adapter.notifyDataSetChanged();
                calendarView.removeDecorators();
                calendarView.addDecorator(new DayViewDecorator() {
                    @Override
                    public boolean shouldDecorate(CalendarDay day) {
                        Date date = day.getDate();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        return eventDateList.contains(simpleDateFormat.format(date));
                    }

                    @Override
                    public void decorate(DayViewFacade view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            view.setBackgroundDrawable(getDrawable(R.drawable.round));
                        } else {
                            view.setBackgroundDrawable(new ColorDrawable(Color.GREEN));
                        }
                    }
                });
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
