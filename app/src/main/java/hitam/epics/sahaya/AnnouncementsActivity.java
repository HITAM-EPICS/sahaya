package hitam.epics.sahaya;

import android.app.NotificationManager;
import android.app.Service;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import hitam.epics.sahaya.support.AnnouncementItem;
import hitam.epics.sahaya.support.AnnouncementsAdapter;

public class AnnouncementsActivity extends AppCompatActivity {

    private AnnouncementsAdapter announcementsAdapter;
    private ArrayList<AnnouncementItem> items;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements_activity);

        notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("announcements");

        ListView announcementListView = (ListView) findViewById(R.id.announcement_list);

        items = new ArrayList<>();
        announcementsAdapter = new AnnouncementsAdapter(this, items);
        announcementListView.setAdapter(announcementsAdapter);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AnnouncementItem announcementItem = dataSnapshot.getValue(AnnouncementItem.class);
                items.add(announcementItem);
                announcementsAdapter.notifyDataSetChanged();
                notificationManager.cancel(0);
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
}
