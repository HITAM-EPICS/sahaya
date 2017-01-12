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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import hitam.epics.sahaya.R;

/**
 * Created by sanjit on 2/1/17.
 */

public class DiscussionAdapter extends ArrayAdapter<DiscussionMessage> {
    private Context context;
    private FirebaseUser user;

    public DiscussionAdapter(@NonNull Context context, @NonNull List<DiscussionMessage> objects) {
        super(context, 0, objects);
        this.context = context;
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = convertView;
        DiscussionMessage currentItem = getItem(position);

        if (currentItem != null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            if (currentItem.getUid().equals(user.getUid())) {
                newView = inflater.inflate(R.layout.discussion_message_self, parent, false);
            } else {
                newView = inflater.inflate(R.layout.discussion_message, parent, false);
            }

            TextView username = (TextView) newView.findViewById(R.id.message_username);
            TextView time = (TextView) newView.findViewById(R.id.message_time);
            TextView message = (TextView) newView.findViewById(R.id.message_text);
            ImageView picture = (ImageView) newView.findViewById(R.id.profile_pic);

            username.setText(currentItem.getName());
            time.setText(
                    DateUtils.getRelativeTimeSpanString(
                            currentItem.getTime(),
                            System.currentTimeMillis(),
                            DateUtils.SECOND_IN_MILLIS
                    ).toString()
            );
            message.setText(currentItem.getMessage());

            if (!currentItem.getPhotoUrl().equals("")) {
                Glide.with(context)
                        .load(currentItem.getPhotoUrl())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(picture);
            }
        }

        return newView;
    }
}

