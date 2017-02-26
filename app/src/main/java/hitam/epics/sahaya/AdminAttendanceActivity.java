package hitam.epics.sahaya;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import hitam.epics.sahaya.support.AttendanceAdapter;
import hitam.epics.sahaya.support.UserDetails;

public class AdminAttendanceActivity extends AppCompatActivity {
    private ArrayList<UserDetails> AttendanceList;
    private AttendanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_attendance);

        Initialisation();
        DatabaseConnection();
    }

    private void DatabaseConnection() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("user_details");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserDetails userDetail = dataSnapshot.getValue(UserDetails.class);
                AttendanceList.add(userDetail);
                Collections.sort(AttendanceList, new Comparator<UserDetails>() {
                    @Override
                    public int compare(UserDetails user1, UserDetails user2) {

                        return user1.getName().compareTo(user2.getName());
                    }
                });
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void Initialisation() {
        ListView attendanceListView = (ListView) findViewById(R.id.attendance_list);
        attendanceListView.setEmptyView(findViewById(R.id.empty_view));
        AttendanceList = new ArrayList<>();
        adapter = new AttendanceAdapter(this, AttendanceList);
        attendanceListView.setAdapter(adapter);
    }
}
