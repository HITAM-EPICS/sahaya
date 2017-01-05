package hitam.epics.sahaya.support;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

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
        DiscussionMessage currentItem = getItem(position);

        if (currentItem != null) {
            if (newView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                if (Objects.equals(currentItem.getName(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
                    newView = inflater.inflate(R.layout.discussion_message_self, parent, false);
                } else {
                    newView = inflater.inflate(R.layout.discussion_message, parent, false);
                }
            }

            TextView username = (TextView) newView.findViewById(R.id.message_username);
            TextView time = (TextView) newView.findViewById(R.id.message_time);
            TextView message = (TextView) newView.findViewById(R.id.message_text);

            username.setText(currentItem.getName());
            time.setText(
                    DateUtils.getRelativeTimeSpanString(
                            currentItem.getTime(),
                            System.currentTimeMillis(),
                            DateUtils.SECOND_IN_MILLIS
                    ).toString()
            );
            message.setText(currentItem.getMessage());
        }


        return newView;
    }
}

