package hitam.epics.sahaya;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
        eventList.add(new CalendarItem("ZPHS", "04-01-2017", "01:00 PM", "04:00 PM", "Kukatpally"));
        eventList.add(new CalendarItem("ZPHS", "07-01-2017", "01:00 PM", "04:00 PM", "Bachupally"));
        eventList.add(new CalendarItem("ZPHS", "14-01-2017", "01:00 PM", "04:00 PM", "Miyapur"));

        for (CalendarItem item : eventList) {
            eventDateList.add(item.getDate());
        }

        currentDateEventList = new ArrayList<>();
        currentDateEventList.add(new CalendarItem("Select a date from calender to View schedule", ""));

        menuAdapter = new CalendarItemMenuAdapter(this, currentDateEventList);

        EventListGridView = (GridView) findViewById(R.id.event_list);
        EventListGridView.setAdapter(menuAdapter);

        calendarView = (MaterialCalendarView) findViewById(R.id.calendar);
        calendarView.setCurrentDate(new Date());
        calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                Date date = day.getDate();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                if (eventDateList.contains(simpleDateFormat.format(date)))
                    return true;
                else
                    return false;
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
                if (currentDateEventList.size() == 0) {
                    currentDateEventList.add(new CalendarItem("No Event Found", ""));
                }
                menuAdapter.notifyDataSetChanged();
            }
        });
    }
}
