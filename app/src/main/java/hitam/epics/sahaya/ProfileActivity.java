package hitam.epics.sahaya;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends Activity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseUser user;
    private TextView ProfileName;
    private TextView ProfileLevel;
    private TextView ProfilePoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ProfileName = (TextView) findViewById(R.id.profile_name);
        ProfileLevel = (TextView) findViewById(R.id.profile_level);
        ProfilePoints = (TextView) findViewById(R.id.profile_points);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        ProfileName.setText(user.getDisplayName());
        ProfileLevel.setText("Associate");
        ProfilePoints.setText("10");
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void EditName(View view) {
        findViewById(R.id.name_view).setVisibility(View.GONE);
        findViewById(R.id.name_edit).setVisibility(View.VISIBLE);

        ((EditText) findViewById(R.id.profile_name_edit)).setText(user.getDisplayName());
    }

    public void SaveName(View view) {
        String newName = ((EditText) findViewById(R.id.profile_name_edit)).getText().toString().trim();

        if (!newName.equals("")) {
            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build();
            user.updateProfile(userProfileChangeRequest);
            ((TextView) findViewById(R.id.profile_name)).setText(newName);
        }

        findViewById(R.id.name_view).setVisibility(View.VISIBLE);
        findViewById(R.id.name_edit).setVisibility(View.GONE);
    }

    public void CancelNameEdit(View view) {
        findViewById(R.id.name_view).setVisibility(View.VISIBLE);
        findViewById(R.id.name_edit).setVisibility(View.GONE);
    }
}
