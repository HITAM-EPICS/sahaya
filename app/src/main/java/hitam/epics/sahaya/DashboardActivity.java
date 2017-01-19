package hitam.epics.sahaya;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hitam.epics.sahaya.support.DashboardItem;
import hitam.epics.sahaya.support.DashboardMenuAdapter;
import hitam.epics.sahaya.support.UserData;

public class DashboardActivity extends Activity {
    private FirebaseAuth auth;
    private DashboardMenuAdapter menuAdapter;
    private ArrayList<DashboardItem> menuItems;
    private GridView dashboardMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        verifyAuthentication();
        getUserType();
        initReferences();
        addMenuItems();

    }

    private void getUserType() {
        final DatabaseReference UserTypeDatabase = FirebaseDatabase.getInstance().getReference("user_types/");
        final String email = auth.getCurrentUser().getEmail()
                .replace(".", "(dot)")
                .replace("#", "(hash)")
                .replace("$", "(dollar)")
                .replace("[", "(bropen)")
                .replace("]", "(brclose)");
        UserTypeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(email)) {
                    UserData.userType = dataSnapshot.child(email).getValue(UserData.UserType.class);
                    Log.e("onDataChange: ", UserData.userType.toString());
                    enableAdminFeatures();
                } else {
                    UserTypeDatabase.child(email).setValue(UserData.UserType.VOLUNTEER);
                    UserData.userType = UserData.UserType.VOLUNTEER;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void enableAdminFeatures() {
        menuItems.add(0, new DashboardItem(R.drawable.profile, "Admin Menu", ProfileActivity.class));
        menuAdapter.notifyDataSetChanged();
    }

    private void addMenuItems() {
        menuItems.add(new DashboardItem(R.drawable.timetable, "Timetable", TimetableActivity.class));
        menuItems.add(new DashboardItem(R.drawable.study_material, "Materials", MaterialActivity.class));
        menuItems.add(new DashboardItem(R.drawable.profile, "Profile", ProfileActivity.class));
        menuItems.add(new DashboardItem(R.drawable.discussion, "Discussion", DiscussionActivity.class));
        menuItems.add(new DashboardItem(R.drawable.about_us, "About Us", AboutActivity.class));
        menuItems.add(new DashboardItem(R.drawable.contact_us, "Contact Us", ContactUsActivity.class));
        menuAdapter.notifyDataSetChanged();
    }

    private void initReferences() {
        dashboardMenu = (GridView) findViewById(R.id.dashboard_menu);
        menuItems = new ArrayList<>();

        menuAdapter = new DashboardMenuAdapter(this, menuItems);
        dashboardMenu.setAdapter(menuAdapter);

        dashboardMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(DashboardActivity.this, menuItems.get(position).getAssociatedClass()));
            }
        });
    }

    private void verifyAuthentication() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            System.exit(0);
        }
    }
}
