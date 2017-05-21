package hitam.epics.sahaya.volunteer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import hitam.epics.sahaya.R;
import hitam.epics.sahaya.support.Note;
import hitam.epics.sahaya.support.NotesAdapter;

public class ViewNotesActivity extends AppCompatActivity {
    private ListView NotesListView;
    private ArrayList<Note> notes;
    private NotesAdapter adapter;
    private DatabaseReference reference;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);

        extras = getIntent().getExtras();

        initialise();
        connectDatabase();
    }

    private void initialise() {
        NotesListView = (ListView) findViewById(R.id.notes_list);
        NotesListView.setEmptyView(findViewById(R.id.empty_view));

        notes = new ArrayList<>();
        adapter = new NotesAdapter(this, notes);
        NotesListView.setAdapter(adapter);

        reference = FirebaseDatabase.getInstance()
                .getReference("notes")
                .child(String.valueOf(extras.getInt("class")))
                .child(String.valueOf(extras.getInt("subject")));
    }

    private void connectDatabase() {
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Note note = dataSnapshot.getValue(Note.class);
                notes.add(note);
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
}
