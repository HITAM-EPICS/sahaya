package hitam.epics.sahaya.volunteer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import hitam.epics.sahaya.R;
import hitam.epics.sahaya.support.Curriculum;

public class CurriculumSubActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> curriculum;
    private ArrayAdapter<String> adapter;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curriculum_sub);
        listView = (ListView) findViewById(R.id.list);
        curriculum = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.ListItem, curriculum);
        listView.setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String aClass = getIntent().getExtras().getString("class");
        reference = database.getReference("curriculum").child(aClass).child("curriculum");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Curriculum aClass = dataSnapshot.getValue(Curriculum.class);
                curriculum.add(aClass.toString());
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

    public void addCurriculum(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText idEditText = new EditText(this);
        idEditText.setHint("S. No");
        idEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        final EditText titleEditText = new EditText(this);
        titleEditText.setHint("Title");
        final EditText descEditText = new EditText(this);
        descEditText.setHint("Description");
        descEditText.setSingleLine(false);
        LinearLayout view1 = new LinearLayout(this);
        view1.setPadding(30, 30, 30, 30);
        view1.setOrientation(LinearLayout.VERTICAL);
        view1.addView(idEditText);
        view1.addView(titleEditText);
        view1.addView(descEditText);
        builder.setTitle("Add Class")
                .setMessage("Enter Details Here")
                .setView(view1)
                .setNeutralButton("Cancel", null)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = titleEditText.getText().toString().trim();
                        int id = Integer.parseInt(idEditText.getText().toString().trim());
                        String desc = descEditText.getText().toString().trim();
                        reference.child(String.valueOf(id)).setValue(new Curriculum(id, title, desc));
                    }
                });
        builder.show();
    }
}
