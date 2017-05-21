package hitam.epics.sahaya.volunteer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import hitam.epics.sahaya.R;
import hitam.epics.sahaya.support.CenterItem;
import hitam.epics.sahaya.support.CentersAdapter;
import hitam.epics.sahaya.support.EventItem;

public class DiscussionForumListActivity extends AppCompatActivity {
    private CentersAdapter adapter;
    private ArrayList<CenterItem> centers;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_forum_list);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("/centers/");

        ListView centerListView = (ListView) findViewById(R.id.centers_list);
        centerListView.setEmptyView(findViewById(R.id.center_empty_view));
        centers = new ArrayList<>();

        adapter = new CentersAdapter(this, centers);
        centerListView.setAdapter(adapter);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                centers.add(dataSnapshot.getValue(CenterItem.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                EventItem value = dataSnapshot.getValue(EventItem.class);
                for (CenterItem item : centers) {
                    if (item.toString().equals(value.toString())) {
                        centers.remove(item);
                        break;
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        centerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CenterItem centerItem = centers.get(position);
                Intent intent = new Intent(DiscussionForumListActivity.this, DiscussionActivity.class);
                Bundle extras = new Bundle();
                extras.putString("center_name", centerItem.getName());
                extras.putString("center_location", centerItem.getLocation());
                extras.putString("center_address", centerItem.getAddress());
                extras.putDouble("center_latitude", centerItem.getLat());
                extras.putDouble("center_longitude", centerItem.getLon());
                extras.putBoolean("admin", false);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

    }
}
