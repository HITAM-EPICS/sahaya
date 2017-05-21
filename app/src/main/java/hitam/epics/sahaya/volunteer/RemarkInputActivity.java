package hitam.epics.sahaya.volunteer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hitam.epics.sahaya.R;
import hitam.epics.sahaya.support.Remark;

public class RemarkInputActivity extends AppCompatActivity {
    private DatabaseReference remarks;
    private EditText remarkEditText;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark_input);
        Bundle extras = getIntent().getExtras();
        remarks = FirebaseDatabase.getInstance().getReference("remarks")
                .child(String.valueOf(extras.getInt("class")))
                .child(String.valueOf(extras.getInt("subject")));
        user = FirebaseAuth.getInstance().getCurrentUser();
        remarkEditText = (EditText) findViewById(R.id.remark);
    }

    public void cancel(View view) {
        finish();
    }

    public void sendRemark(View view) {
        String remark = remarkEditText.getText().toString().trim();
        if (remark.length() == 0) {
            remarkEditText.setError("Remark cannot be empty.");
            return;
        }
        findViewById(R.id.remark_main).setVisibility(View.GONE);
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        Remark remarkItem = new Remark(System.currentTimeMillis(), user.getDisplayName(), remark);
        remarks.child(String.valueOf(remarkItem.getTime()))
                .setValue(remarkItem)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(RemarkInputActivity.this,
                                "Remark Sent",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RemarkInputActivity.this,
                        "Unable to send remark. Please try again later",
                        Toast.LENGTH_LONG).show();

                findViewById(R.id.remark_main).setVisibility(View.GONE);
                findViewById(R.id.loading).setVisibility(View.VISIBLE);
            }
        });

    }
}
