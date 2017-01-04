package hitam.epics.sahaya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import hitam.epics.sahaya.support.DashboardItem;
import hitam.epics.sahaya.support.DashboardMenuAdapter;

public class DashboardActivity extends Activity {
    private FirebaseAuth auth;
    private DashboardMenuAdapter menuAdapter;
    private ArrayList<DashboardItem> menuItems;
    private GridView dashboardMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        auth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
        }

        dashboardMenu = (GridView) findViewById(R.id.dashboard_menu);
        menuItems = new ArrayList<>();
        menuAdapter = new DashboardMenuAdapter(this, menuItems);
        dashboardMenu.setAdapter(menuAdapter);

        menuItems.add(new DashboardItem(R.drawable.timetable, "Timetable"));
        menuItems.add(new DashboardItem(R.drawable.study_material, "Materials"));
        menuItems.add(new DashboardItem(R.drawable.profile, "Profile"));
        menuItems.add(new DashboardItem(R.drawable.discussion, "Discussion"));
        menuItems.add(new DashboardItem(R.drawable.about_us, "About Us"));
        menuItems.add(new DashboardItem(R.drawable.contact_us, "Contact Us"));

        menuAdapter.notifyDataSetChanged();

        dashboardMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(DashboardActivity.this, TimetableActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(DashboardActivity.this, AboutActivity.class));
                        break;

                }
            }
        });

    }

}
