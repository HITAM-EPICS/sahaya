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

import hitam.epics.sahaya.support.AnnouncementItem;
import hitam.epics.sahaya.support.AnnouncementsAdapter;

public class AnnouncementsActivity extends AppCompatActivity {

    private ListView AnnouncementListView;
    private AnnouncementsAdapter announcementsAdapter;
    private ArrayList<AnnouncementItem> items;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements_activity);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("announcements");

        AnnouncementListView = (ListView) findViewById(R.id.announcement_list);

        items = new ArrayList<>();
        announcementsAdapter = new AnnouncementsAdapter(this, items);
        AnnouncementListView.setAdapter(announcementsAdapter);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AnnouncementItem announcementItem = dataSnapshot.getValue(AnnouncementItem.class);
                items.add(announcementItem);
                announcementsAdapter.notifyDataSetChanged();
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
