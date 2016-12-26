package hitam.epics.sahaya;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends Activity {
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView photoUrlTextView;
    private TextView UIDTextView;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        auth = FirebaseAuth.getInstance();

        nameTextView = (TextView) findViewById(R.id.dashboard_name);
        emailTextView = (TextView) findViewById(R.id.dashboard_email);
        photoUrlTextView = (TextView) findViewById(R.id.dashboard_picture_url);
        UIDTextView = (TextView) findViewById(R.id.dashboard_UID);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            // Uri photoUrl = user.getPhotoUrl();

            nameTextView.setText("Name: " + name);
            emailTextView.setText("Email: " + email);
            // photoUrlTextView.setText(photoUrl.toString());
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
            UIDTextView.setText("Unique Id:" + uid);
        }
    }

    public void logout(View view) {
        auth.signOut();
        finish();
    }
}
