package hitam.epics.sahaya;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {
    TextView nameTextView;
    TextView emailTextView;
    TextView photoUrlTextView;
    TextView UIDTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        nameTextView = (TextView) findViewById(R.id.dashboard_name);
        emailTextView = (TextView) findViewById(R.id.dashboard_email);
        photoUrlTextView = (TextView) findViewById(R.id.dashboard_picture_url);
        UIDTextView = (TextView) findViewById(R.id.dashboard_UID);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
           // String name = user.getDisplayName();
            String email = user.getEmail();
           // Uri photoUrl = user.getPhotoUrl();

           // nameTextView.setText(name);
            emailTextView.setText(email);
           // photoUrlTextView.setText(photoUrl.toString());
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
            UIDTextView.setText(uid);
        }
    }
}
