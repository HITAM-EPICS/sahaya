package hitam.epics.sahaya.volunteer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hitam.epics.sahaya.R;
import hitam.epics.sahaya.support.UserDetails;

public class AdditionalInfoActivity extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference;
    private EditText phoneEditText;
    private EditText occupationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_info);

        phoneEditText = (EditText) findViewById(R.id.registration_phone);
        occupationEditText = (EditText) findViewById(R.id.registration_occupation);

        user = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("user_details").child(user.getUid());

    }

    public void continueNext(View view) {
        if (phoneEditText.getText().toString().trim().equals("")) {
            phoneEditText.setError("Required");
            return;
        } else if (phoneEditText.getText().toString().trim().length() < 10) {
            phoneEditText.setError("Invalid Error");
            return;
        }
        if (occupationEditText.getText().toString().trim().equals("")) {
            occupationEditText.setError("Required");
            return;
        }
        if (user != null) {
            UserDetails userDetails = new UserDetails(
                    user.getUid(), user.getDisplayName(), user.getEmail(),
                    phoneEditText.getText().toString().trim(),
                    occupationEditText.getText().toString().trim(),
                    System.currentTimeMillis()
            );
            reference.setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    AdditionalInfoActivity.this.finish();
                }
            });
        }
    }
}
