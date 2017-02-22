package hitam.epics.sahaya;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hitam.epics.sahaya.support.AnnouncementItem;

public class AdminAnnouncementActivity extends AppCompatActivity {
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_announcement);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("/announcements/");
    }

    public void cancel(View view) {
        finish();
    }

    public void announce(View view) {
        EditText messageEditText = (EditText) findViewById(R.id.announcement);

        if (messageEditText.getText().toString().trim().equals("")) {
            messageEditText.setError("AnnouncementItem Text Required");
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            AnnouncementItem announcementItem = new AnnouncementItem(
                    user.getDisplayName(),
                    messageEditText.getText().toString().trim(),
                    System.currentTimeMillis()
            );
            reference.child(announcementItem.getTime() + "").setValue(announcementItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    AdminAnnouncementActivity.this.finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminAnnouncementActivity.this);
                    builder.setMessage("Unable to send Announce your announcementItem. Please check your network connection")
                            .setNeutralButton("OK", null);
                    builder.create().show();
                }
            });
        }

    }
}
