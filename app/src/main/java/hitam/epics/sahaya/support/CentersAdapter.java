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

public class CentersAdapter extends ArrayAdapter<CenterItem> {
    private final Context context;

    public CentersAdapter(Context context, List<CenterItem> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = convertView;
        if (newView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            newView = inflater.inflate(R.layout.center_item, parent, false);
        }

        CenterItem item = getItem(position);

        if (item != null) {
            TextView name = (TextView) newView.findViewById(R.id.center_name);
            TextView location = (TextView) newView.findViewById(R.id.center_location);

            name.setText(item.getName());
            location.setText(item.getLocation());
        }

        return newView;
    }
}
