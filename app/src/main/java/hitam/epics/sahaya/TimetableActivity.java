package hitam.epics.sahaya;

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
import java.util.Objects;

import hitam.epics.sahaya.support.CalendarItem;
import hitam.epics.sahaya.support.CalendarItemMenuAdapter;

public class TimetableActivity extends Activity {
    FirebaseDatabase database;
    DatabaseReference reference;
    private ArrayList<String> eventDateList;
    private ArrayList<CalendarItem> currentDateEventList;
    private ArrayList<CalendarItem> eventList;
    private CalendarItemMenuAdapter menuAdapter;
    private GridView EventListGridView;
    private MaterialCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("/events/");

        eventDateList = new ArrayList<>();
        eventList = new ArrayList<>();
        currentDateEventList = new ArrayList<>();

        menuAdapter = new CalendarItemMenuAdapter(this, currentDateEventList);
        EventListGridView = (GridView) findViewById(R.id.event_list);
        EventListGridView.setAdapter(menuAdapter);
        EventListGridView.setEmptyView(findViewById(R.id.empty_view));

        calendarView = (MaterialCalendarView) findViewById(R.id.calendar);
        calendarView.setCurrentDate(new Date());

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                currentDateEventList.clear();
                for (CalendarItem item : eventList) {
                    if (Objects.equals(item.getDate(), simpleDateFormat.format(date.getDate()))) {
                        currentDateEventList.add(item);
                    }
                }
                menuAdapter.notifyDataSetChanged();
            }
        });

        EventListGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CalendarItem calendarItem = currentDateEventList.get(position);
                Intent intent = new Intent(TimetableActivity.this, TimetableEventDetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString("event_name", calendarItem.getName());
                extras.putString("event_date", calendarItem.getDate());
                extras.putString("event_time", calendarItem.getStart() + "-" + calendarItem.getEnd());
                extras.putString("event_desc", calendarItem.getArea());
                extras.putDouble("event_latitude", calendarItem.getLat());
                extras.putDouble("event_longitude", calendarItem.getLon());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        calendarView.setSelectedDate(new Date());

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                eventList.add(dataSnapshot.getValue(CalendarItem.class));
                Log.e("onChildAdded: ", dataSnapshot.getValue(CalendarItem.class).toString());
                eventDateList.clear();
                for (CalendarItem item : eventList) {
                    eventDateList.add(item.getDate());
                }
                menuAdapter.notifyDataSetChanged();
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
                CalendarItem value = dataSnapshot.getValue(CalendarItem.class);
                for (CalendarItem item : eventList) {
                    if (item.toString().equals(value.toString())) {
                        eventList.remove(item);
                        break;
                    }
                }
                eventDateList.clear();
                for (CalendarItem item : eventList) {
                    eventDateList.add(item.getDate());
                }

                menuAdapter.notifyDataSetChanged();
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
