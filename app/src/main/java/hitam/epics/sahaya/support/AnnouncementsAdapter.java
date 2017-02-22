package hitam.epics.sahaya.support;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import hitam.epics.sahaya.R;

/**
 * Created by sanjit on 3/1/17.
 */

public class AnnouncementsAdapter extends ArrayAdapter<AnnouncementItem> {
    Context context;

    public AnnouncementsAdapter(Context context, List<AnnouncementItem> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = convertView;
        if (newView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            newView = inflater.inflate(R.layout.announcement_item, parent, false);
        }

        AnnouncementItem item = getItem(position);
        if (item != null) {

            TextView name = (TextView) newView.findViewById(R.id.announcement_name);
            TextView time = (TextView) newView.findViewById(R.id.announcement_time);
            TextView text = (TextView) newView.findViewById(R.id.announcement_text);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            name.setText(item.getName());
            time.setText(simpleDateFormat.format(item.getTime()));
            text.setText(item.getAnnouncement());
        }

        return newView;
    }
}
