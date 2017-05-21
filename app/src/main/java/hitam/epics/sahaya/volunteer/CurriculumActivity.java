package hitam.epics.sahaya.volunteer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import hitam.epics.sahaya.R;

public class CurriculumActivity extends AppCompatActivity {
    private GridView listView;
    private ArrayList<String> curriculum;
    private ArrayAdapter<String> adapter;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curriculum);
        listView = (GridView) findViewById(R.id.list);
        curriculum = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.list_group, R.id.listTitle, curriculum);
        listView.setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("curriculum");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String aClass = dataSnapshot.child("class").getValue(String.class);
                curriculum.add(aClass);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CurriculumActivity.this, CurriculumSubActivity.class);
                intent.putExtra("class", curriculum.get(position));
                startActivity(intent);
            }
        });
    }

    public void addClass(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setPadding(30, 30, 30, 30);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText editText = new EditText(this);
        editText.setHint("Class");
        final EditText idText = new EditText(this);
        idText.setHint("Id (Arranged based on it)");
        idText.setInputType(InputType.TYPE_CLASS_NUMBER);
        linearLayout.addView(idText);
        linearLayout.addView(editText);
        builder.setTitle("Add Class")
                .setMessage("Enter Class Here")
                .setView(linearLayout)
                .setNeutralButton("Cancel", null)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mClass = editText.getText().toString().trim();
                        String id = idText.getText().toString().trim();
                        if (id.length() == 0)
                            return;
                        if (mClass.length() == 0)
                            return;
                        reference.child(id).child("class").setValue(mClass);
                    }
                });
        builder.show();
    }
}
