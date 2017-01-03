package hitam.epics.sahaya.support;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hitam.epics.sahaya.R;

/**
 * Created by sanjit on 3/1/17.
 */

public class CalendarItemMenuAdapter extends ArrayAdapter<CalendarItem> {
    Context context;

    public CalendarItemMenuAdapter(Context context, List<CalendarItem> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = convertView;
        if (newView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            newView = inflater.inflate(R.layout.calendar_item, parent, false);
        }

        CalendarItem item = getItem(position);

        TextView name = (TextView) newView.findViewById(R.id.event_name);
        TextView time = (TextView) newView.findViewById(R.id.event_time);

        if (item.getStart() != null) {
            name.setText(item.getName() + " - " + item.getArea());
            time.setText(item.getStart() + " - " + item.getEnd());
        } else {
            name.setText(item.getName());
            time.setText("");
        }

        return newView;
    }
}
