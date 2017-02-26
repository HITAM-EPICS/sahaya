package hitam.epics.sahaya;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import hitam.epics.sahaya.support.UserDetails;

public class AdminContactsActivity extends AppCompatActivity {
    private ArrayAdapter<UserDetails> adapter;
    private ArrayList<UserDetails> userDetails;
    private Intent pendindIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_contacts);

        initialise();
        connectDatabase();
    }

    private void connectDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("user_details");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserDetails userDetail = dataSnapshot.getValue(UserDetails.class);
                userDetails.add(userDetail);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initialise() {
        ListView contactListView = (ListView) findViewById(R.id.contacts_list);
        userDetails = new ArrayList<>();
        userDetails.add(new UserDetails("0", "-Send Mail to All-", "", "", "", 0L));
        adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, userDetails);
        contactListView.setAdapter(adapter);

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final UserDetails userDetail = userDetails.get(position);
                if (userDetail.getUid().equals("0")) {
                    sendMailToAll();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AdminContactsActivity.this);
                    builder.setItems(new CharSequence[]{"Call", "Mail"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + userDetail.getPhone()));
                                if (ActivityCompat.checkSelfPermission(AdminContactsActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    pendindIntent = call;
                                    ActivityCompat.requestPermissions(AdminContactsActivity.this,
                                            new String[]{Manifest.permission.CALL_PHONE}, 0);
                                    return;
                                }
                                startActivity(call);
                            } else {
                                Intent mail = new Intent(Intent.ACTION_SEND);
                                mail.setType("text/html");
                                mail.putExtra(Intent.EXTRA_EMAIL, userDetail.getEmail());
                                mail.putExtra(Intent.EXTRA_SUBJECT, "Sahaya Mail");
                                startActivity(Intent.createChooser(mail, "Send Email"));
                            }
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("Close", null);
                    builder.show();
                }
            }
        });
    }

    private void sendMailToAll() {
        String to = "";
        for (UserDetails detail : userDetails) {
            if (!detail.getUid().equals("0")) {
                if (to.length() > 0) {
                    to = to + ";";
                }
                to = to + detail.getEmail();
            }
        }
        Intent mail = new Intent(Intent.ACTION_SEND);
        mail.setType("text/html");
        mail.putExtra(Intent.EXTRA_EMAIL, to);
        mail.putExtra(Intent.EXTRA_SUBJECT, "Sahaya Mail");
        startActivity(Intent.createChooser(mail, "Send Email"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(pendindIntent);
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
