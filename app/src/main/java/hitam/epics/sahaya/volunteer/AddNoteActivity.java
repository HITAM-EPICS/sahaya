package hitam.epics.sahaya.volunteer;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import hitam.epics.sahaya.R;
import hitam.epics.sahaya.support.Note;

public class AddNoteActivity extends AppCompatActivity {
    private DatabaseReference notes;
    private EditText noteEditText;
    private FirebaseUser user;
    private Note newNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Bundle extras = getIntent().getExtras();
        notes = FirebaseDatabase.getInstance().getReference("notes")
                .child(String.valueOf(extras.getInt("class")))
                .child(String.valueOf(extras.getInt("subject")));
        user = FirebaseAuth.getInstance().getCurrentUser();
        noteEditText = (EditText) findViewById(R.id.note);
    }

    public void cancel(View view) {
        finish();
    }

    public void sendNote(View view) {
        String note = noteEditText.getText().toString().trim();
        if (note.length() == 0) {
            noteEditText.setError("Note cannot be empty.");
            return;
        }
        findViewById(R.id.notes_main).setVisibility(View.GONE);
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        newNote = new Note(System.currentTimeMillis(), user.getDisplayName(), note, null);

        notes.child(String.valueOf(newNote.getTime()))
                .setValue(newNote)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddNoteActivity.this,
                                "Remark Sent",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNoteActivity.this,
                        "Unable to send remark. Please try again later",
                        Toast.LENGTH_LONG).show();

                findViewById(R.id.notes_main).setVisibility(View.GONE);
                findViewById(R.id.loading).setVisibility(View.VISIBLE);
            }
        });
    }

    public void sendNoteWithPicture(View view) {
        String note = noteEditText.getText().toString().trim();
        if (note.length() == 0) {
            noteEditText.setError("Note cannot be empty.");
            return;
        }
        findViewById(R.id.notes_main).setVisibility(View.GONE);
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        newNote = new Note(System.currentTimeMillis(), user.getDisplayName(), note, null);

        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap NotePic = extras.getParcelable("data");
                uploadPic(NotePic);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        findViewById(R.id.notes_main).setVisibility(View.GONE);
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
    }

    private void uploadPic(Bitmap notePic) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference NoteRef = mStorageRef.child("notes/" + newNote.getTime() + "/pic.jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        notePic.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = NoteRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);
                builder.setTitle("Error")
                        .setMessage("Unable to update profile picture. Please try again later.")
                        .setPositiveButton("Close", null);
                builder.create().show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri photoUrl = taskSnapshot.getDownloadUrl();
                if (photoUrl != null) {
                    newNote.setPath(photoUrl.toString());
                }
                notes.child(String.valueOf(newNote.getTime()))
                        .setValue(newNote)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AddNoteActivity.this,
                                        "Remark Sent",
                                        Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNoteActivity.this,
                                "Unable to send remark. Please try again later",
                                Toast.LENGTH_LONG).show();

                        findViewById(R.id.notes_main).setVisibility(View.GONE);
                        findViewById(R.id.loading).setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
}
