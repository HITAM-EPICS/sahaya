package hitam.epics.sahaya;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

public class ArchivesActivity extends Activity {
    private Spinner ClassSpinner;
    private Spinner SubjectSpinner;
    private ArrayList<String> SubjectList;
    private ArrayAdapter<String> SubjectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archives);
        ClassSpinner = (Spinner) findViewById(R.id.class_spinner);
        SubjectSpinner = (Spinner) findViewById(R.id.subject_spinner);

        String s = ClassSpinner.getSelectedItem().toString();
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

    public void DownloadTextbook(View view) {
        String[] TextbookLinks = getResources().getStringArray(R.array.textbooks);
        int SelectedClass = ClassSpinner.getSelectedItemPosition();
        String[] SelectedClassTextBookLinks = TextbookLinks[SelectedClass].split(";");
        int SelectedSubject = SubjectSpinner.getSelectedItemPosition();
        String SelectedSubjectLink = SelectedClassTextBookLinks[SelectedSubject];

        String path = getString(R.string.textbooks_path);

        Intent TextbookDownloadIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(path + SelectedSubjectLink));
        startActivity(TextbookDownloadIntent);
    }
}
