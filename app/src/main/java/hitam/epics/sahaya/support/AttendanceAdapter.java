package hitam.epics.sahaya.support;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.List;

import hitam.epics.sahaya.R;

/**
 * Created by sanjit on 3/1/17.
 */

public class AttendanceAdapter extends ArrayAdapter<UserDetails> {
    private Context context;
    private DatabaseReference reference;

    public AttendanceAdapter(Context context, List<UserDetails> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = convertView;
        if (newView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            newView = inflater.inflate(R.layout.attendance_item, parent, false);
        }

        final UserDetails item = getItem(position);

        TextView name = (TextView) newView.findViewById(R.id.attendance_name);
        TextView email = (TextView) newView.findViewById(R.id.attendance_email);
        TextView attendanceCount = (TextView) newView.findViewById(R.id.attendance_count);
        CheckBox attendance = (CheckBox) newView.findViewById(R.id.attendance);


        if (item != null) {
            reference = FirebaseDatabase.getInstance().getReference("user_details");

            name.setText(item.getName());
            email.setText(item.getEmail());
            attendanceCount.setText("Total: " + item.getAttendance());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String lastUpdateDate = simpleDateFormat.format(item.getLast_update());
            String today = simpleDateFormat.format(System.currentTimeMillis());
            Log.e("getView: ", lastUpdateDate + ":" + today);

            if (!lastUpdateDate.equals(today)) {
                attendance.setSelected(false);
                item.setLast_update(System.currentTimeMillis());
                item.setLast_update_present(false);
                reference.child(item.getUid()).setValue(item);
                AttendanceAdapter.this.notifyDataSetChanged();
            }
            attendance.setChecked(item.isLast_update_present());
            if (item.isLast_update_present()) {
                attendance.setText("Present");
            } else {
                attendance.setText("Absent");
            }

            attendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.isLast_update_present()) {
                        item.setAttendance(item.getAttendance() - 1);
                    } else {
                        item.setAttendance(item.getAttendance() + 1);
                    }

                    item.setLast_update_present(!item.isLast_update_present());
                    item.setLast_update(System.currentTimeMillis());
                    reference.child(item.getUid()).setValue(item);
                    AttendanceAdapter.this.notifyDataSetChanged();
                }
            });
        }


        return newView;
    }
}
