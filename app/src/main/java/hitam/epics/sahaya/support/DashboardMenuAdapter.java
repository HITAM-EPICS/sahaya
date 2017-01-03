package hitam.epics.sahaya.support;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hitam.epics.sahaya.R;

/**
 * Created by sanjit on 2/1/17.
 */

public class DashboardMenuAdapter extends ArrayAdapter<DashboardItem> {
    private Context context;

    public DashboardMenuAdapter(@NonNull Context context, @NonNull List<DashboardItem> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = convertView;
        if (newView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            newView = inflater.inflate(R.layout.classification_item, parent,false);
        }

        DashboardItem currentItem = getItem(position);

        ImageView ItemImageView = (ImageView) newView.findViewById(R.id.item_image);
        TextView ItemNameView = (TextView) newView.findViewById(R.id.item_name);

        ItemImageView.setImageResource(currentItem.getImageResource());
        ItemNameView.setText(currentItem.getName());

        return newView;
    }
}

