package hitam.epics.sahaya;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

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

        menuItems.add(new DashboardItem(R.drawable.sahaya_logo, "Timetable"));
        menuItems.add(new DashboardItem(R.drawable.sahaya_logo, "Materials"));
        menuItems.add(new DashboardItem(R.drawable.sahaya_logo, "Profile"));
        menuItems.add(new DashboardItem(R.drawable.sahaya_logo, "Discussion"));
        menuItems.add(new DashboardItem(R.drawable.sahaya_logo, "About Us"));
        menuItems.add(new DashboardItem(R.drawable.sahaya_logo, "Contact Us"));

        menuAdapter.notifyDataSetChanged();

    }

}
