package hitam.epics.sahaya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import jp.wasabeef.blurry.Blurry;

public class DashboardActivity extends Activity {
    private FirebaseAuth auth;
    private DashboardMenuAdapter menuAdapter;
    private DashboardMenuAdapter menuAdminAdapter;
    private ArrayList<DashboardItem> menuItems;
    private ArrayList<DashboardItem> menuAdminItems;
    private GridView dashboardMenu;
    private GridView dashboardAdminMenu;
    private ViewGroup dashboardBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        verifyAuthentication();
        initReferences();
        blurBackground();
        getUserType();
        addMenuItems();

    }

    private void blurBackground() {
        dashboardBackground.post(new Runnable() {
            @Override
            public void run() {
                Blurry.with(DashboardActivity.this)
                        .radius(10)
                        .sampling(16)
                        .async()
                        .onto(dashboardBackground);
            }
        });
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
                    if (UserData.userType == UserData.UserType.ADMIN) {
                        enableAdminFeatures();
                    }
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
        menuItems.add(0, new DashboardItem(R.drawable.profile, "Admin Menu", DashboardActivity.class));
        menuAdapter.notifyDataSetChanged();
    }

    private void addMenuItems() {
        menuItems.add(new DashboardItem(R.drawable.events, "Events", EventsActivity.class));
        menuItems.add(new DashboardItem(R.drawable.study_material, "Materials", MaterialActivity.class));
        menuItems.add(new DashboardItem(R.drawable.profile, "Profile", ProfileActivity.class));
        menuItems.add(new DashboardItem(R.drawable.discussion, "Discussion", DiscussionActivity.class));
        menuItems.add(new DashboardItem(R.drawable.about_us, "About Us", AboutActivity.class));
        menuItems.add(new DashboardItem(R.drawable.contact_us, "Contact Us", ContactUsActivity.class));
        menuAdapter.notifyDataSetChanged();

        menuAdminItems.add(new DashboardItem(R.drawable.events, "Manage Events", AdminEventsActivity.class));
        menuAdminItems.add(new DashboardItem(R.drawable.events, "Attendance", EventsActivity.class));
        menuAdminItems.add(new DashboardItem(R.drawable.roles, "Roles", AdminRolesActivity.class));
        menuAdminItems.add(new DashboardItem(R.drawable.events, "Center Management", EventsActivity.class));
        menuAdminItems.add(new DashboardItem(R.drawable.announcement, "Announcement", EventsActivity.class));
        menuAdminItems.add(new DashboardItem(R.drawable.back, "Back", DashboardActivity.class));
        menuAdminAdapter.notifyDataSetChanged();
    }

    private void initReferences() {
        dashboardMenu = (GridView) findViewById(R.id.dashboard_menu);
        dashboardAdminMenu = (GridView) findViewById(R.id.dashboard_admin_menu);
        dashboardBackground = (ViewGroup) findViewById(R.id.dashboard_background);

        menuItems = new ArrayList<>();
        menuAdminItems = new ArrayList<>();

        menuAdapter = new DashboardMenuAdapter(this, menuItems);
        menuAdminAdapter = new DashboardMenuAdapter(this, menuAdminItems);

        dashboardMenu.setAdapter(menuAdapter);
        dashboardAdminMenu.setAdapter(menuAdminAdapter);

        dashboardMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (menuItems.get(position).getAssociatedClass() == DashboardActivity.class) {
                    dashboardMenu.setVisibility(View.GONE);
                    dashboardAdminMenu.setVisibility(View.VISIBLE);
                } else {
                    startActivity(new Intent(DashboardActivity.this, menuItems.get(position).getAssociatedClass()));
                }
            }
        });

        dashboardAdminMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (menuAdminItems.get(position).getAssociatedClass() == DashboardActivity.class) {
                    dashboardAdminMenu.setVisibility(View.GONE);
                    dashboardMenu.setVisibility(View.VISIBLE);
                } else {
                    startActivity(new Intent(DashboardActivity.this, menuAdminItems.get(position).getAssociatedClass()));
                }
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

}
