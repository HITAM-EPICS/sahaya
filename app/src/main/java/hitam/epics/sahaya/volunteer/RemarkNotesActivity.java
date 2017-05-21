package hitam.epics.sahaya.volunteer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

import hitam.epics.sahaya.R;

public class RemarkNotesActivity extends AppCompatActivity {
    private Spinner ClassSpinner;
    private Spinner SubjectSpinner;
    private ArrayList<String> SubjectList;
    private ArrayAdapter<String> SubjectAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark_notes);
        ClassSpinner = (Spinner) findViewById(R.id.class_spinner);
        SubjectSpinner = (Spinner) findViewById(R.id.subject_spinner);

        SubjectList = new ArrayList<>();
        SubjectList.addAll(Arrays.asList(getResources().getStringArray(R.array.FIRST)));
        SubjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SubjectList);
        SubjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SubjectSpinner.setAdapter(SubjectAdapter);

        ClassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SubjectList.clear();
                switch (position) {
                    case 0:
                        SubjectList.addAll(Arrays.asList(getResources().getStringArray(R.array.FIRST)));
                        break;
                    case 1:
                        SubjectList.addAll(Arrays.asList(getResources().getStringArray(R.array.SECOND)));
                        break;
                    case 2:
                        SubjectList.addAll(Arrays.asList(getResources().getStringArray(R.array.THIRD)));
                        break;
                    case 3:
                        SubjectList.addAll(Arrays.asList(getResources().getStringArray(R.array.FOURTH)));
                        break;
                    case 4:
                        SubjectList.addAll(Arrays.asList(getResources().getStringArray(R.array.FIFTH)));
                        break;
                    case 5:
                        SubjectList.addAll(Arrays.asList(getResources().getStringArray(R.array.SIXTH)));
                        break;
                    case 6:
                        SubjectList.addAll(Arrays.asList(getResources().getStringArray(R.array.SEVENTH)));
                        break;
                    case 7:
                        SubjectList.addAll(Arrays.asList(getResources().getStringArray(R.array.EIGHTH)));
                        break;
                }
                SubjectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void LeaveRemark(View view) {
        int Class = ClassSpinner.getSelectedItemPosition();
        int Subject = SubjectSpinner.getSelectedItemPosition();

        Intent intent = new Intent(this, RemarkInputActivity.class);
        intent.putExtra("class", Class);
        intent.putExtra("subject", Subject);
        startActivity(intent);
        finish();
    }

    public void AddNote(View view) {
        int Class = ClassSpinner.getSelectedItemPosition();
        int Subject = SubjectSpinner.getSelectedItemPosition();

        Intent intent = new Intent(this, AddNoteActivity.class);
        intent.putExtra("class", Class);
        intent.putExtra("subject", Subject);
        startActivity(intent);
        finish();
    }
}
