package hitam.epics.sahaya;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

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

        eventDateList = new ArrayList<>();

        eventList = new ArrayList<>();
        eventList.add(new CalendarItem("ZPHS", "24-01-2017", "01:00 PM", "04:00 PM", "Kukatpally", 17.484342, 78.413453));
        eventList.add(new CalendarItem("ZPHS", "27-01-2017", "01:00 PM", "04:00 PM", "Sivarampalli", 17.327232, 78.433822));
        eventList.add(new CalendarItem("ZPHS", "14-02-2017", "01:00 PM", "04:00 PM", "Habsiguda", 17.418028, 78.541110));

        for (CalendarItem item : eventList) {
            eventDateList.add(item.getDate());
        }

        currentDateEventList = new ArrayList<>();

        menuAdapter = new CalendarItemMenuAdapter(this, currentDateEventList);
        EventListGridView = (GridView) findViewById(R.id.event_list);
        EventListGridView.setAdapter(menuAdapter);
        EventListGridView.setEmptyView(findViewById(R.id.empty_view));

        calendarView = (MaterialCalendarView) findViewById(R.id.calendar);
        calendarView.setCurrentDate(new Date());
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
                double latitude = currentDateEventList.get(position).getLat();
                double longitude = currentDateEventList.get(position).getLon();
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        calendarView.setSelectedDate(new Date());
    }
}
