package hitam.epics.sahaya.support;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import hitam.epics.sahaya.R;

/**
 * Created by sanjit on 3/1/17.
 */

public class NotesAdapter extends ArrayAdapter<Note> {
    private final Context context;

    public NotesAdapter(Context context, List<Note> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = convertView;
        if (newView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            newView = inflater.inflate(R.layout.note_item, parent, false);
        }

        final Note item = getItem(position);
        if (item != null) {

            TextView name = (TextView) newView.findViewById(R.id.note_name);
            TextView time = (TextView) newView.findViewById(R.id.note_time);
            TextView text = (TextView) newView.findViewById(R.id.note_text);
            Button viewPicture = (Button) newView.findViewById(R.id.view_picture_button);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            name.setText(item.getName());
            time.setText(simpleDateFormat.format(item.getTime()));
            text.setText(item.getNote());
            if (item.getPath() != null) {
                viewPicture.setVisibility(View.VISIBLE);
            } else {
                viewPicture.setVisibility(View.GONE);
            }

            viewPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent NotePictureIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getPath()));
                    context.startActivity(NotePictureIntent);
                }
            });
        }

        return newView;
    }
}
