package hitam.epics.sahaya.admin;

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
import hitam.epics.sahaya.support.RoleItem;
import hitam.epics.sahaya.support.RolesAdapter;

public class AdminRolesActivity extends AppCompatActivity {

    private ArrayList<RoleItem> roles;
    private RolesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roles);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("/user_types/");

        ListView rolesListView = (ListView) findViewById(R.id.roles_list);
        rolesListView.setEmptyView(findViewById(R.id.empty_view));
        roles = new ArrayList<>();
        adapter = new RolesAdapter(this, roles);
        rolesListView.setAdapter(adapter);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.getKey().equals("sanjeet291196@gmail(dot)com")) {
                    roles.add(new RoleItem(dataSnapshot.getKey(), dataSnapshot.getValue(String.class)));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                for (RoleItem r : roles) {
                    if (r.getUsername().equals(dataSnapshot.getKey())) {
                        r.setRole(dataSnapshot.getValue(String.class));
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for (RoleItem r : roles) {
                    if (r.getUsername().equals(dataSnapshot.getKey())) {
                        roles.remove(r);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
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
