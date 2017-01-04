package hitam.epics.sahaya.support;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import hitam.epics.sahaya.R;

/**
 * Created by sanjit on 2/1/17.
 */

public class DiscussionAdapter extends ArrayAdapter<DiscussionMessage> {
    private Context context;

    public DiscussionAdapter(@NonNull Context context, @NonNull List<DiscussionMessage> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = convertView;
        if (newView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            newView = inflater.inflate(R.layout.discussion_message, parent, false);
        }

        DiscussionMessage currentItem = getItem(position);

        TextView username = (TextView) newView.findViewById(R.id.message_username);
        TextView time = (TextView) newView.findViewById(R.id.message_time);
        TextView message = (TextView) newView.findViewById(R.id.message_text);

        if (currentItem != null) {
            username.setText(currentItem.getName());
            time.setText(currentItem.getTime());
            message.setText(currentItem.getMessage());
        }

        return newView;
    }
}

