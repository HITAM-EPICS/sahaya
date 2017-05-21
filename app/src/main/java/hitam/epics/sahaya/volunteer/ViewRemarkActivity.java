package hitam.epics.sahaya.volunteer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import hitam.epics.sahaya.R;
import hitam.epics.sahaya.support.Remark;
import hitam.epics.sahaya.support.RemarksAdapter;

public class ViewRemarkActivity extends AppCompatActivity {
    private ListView RemarkListView;
    private ArrayList<Remark> remarks;
    private RemarksAdapter adapter;
    private DatabaseReference reference;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_remark);

        extras = getIntent().getExtras();

        initialise();
        connectDatabase();

    }

    private void connectDatabase() {
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Remark remark = dataSnapshot.getValue(Remark.class);
                remarks.add(remark);
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

    private void initialise() {
        RemarkListView = (ListView) findViewById(R.id.remark_list);
        RemarkListView.setEmptyView(findViewById(R.id.empty_view));

        remarks = new ArrayList<>();
        adapter = new RemarksAdapter(this, remarks);
        RemarkListView.setAdapter(adapter);

        reference = FirebaseDatabase.getInstance()
                .getReference("remarks")
                .child(String.valueOf(extras.getInt("class")))
                .child(String.valueOf(extras.getInt("subject")));
    }
}
