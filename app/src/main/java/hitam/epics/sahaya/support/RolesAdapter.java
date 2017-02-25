package hitam.epics.sahaya.support;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import hitam.epics.sahaya.R;

/**
 * Created by sanjit on 3/1/17.
 */

public class RolesAdapter extends ArrayAdapter<RoleItem> {
    private final Context context;

    public RolesAdapter(Context context, List<RoleItem> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = convertView;
        if (newView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            newView = inflater.inflate(R.layout.roles_item, parent, false);
        }

        final RoleItem item = getItem(position);

        TextView name = (TextView) newView.findViewById(R.id.username);
        final Switch role = (Switch) newView.findViewById(R.id.role);
        role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert item != null;
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/user_types/" + item.getUsername());
                if (role.isChecked()) {
                    reference.setValue("ADMIN");
                } else {
                    reference.setValue("VOLUNTEER");
                }
            }
        });

        assert item != null;
        name.setText(item.getUsername().replace("(dot)", ".")
                .replace("(hash)", "#")
                .replace("(dollar)", "$")
                .replace("(bropen)", "[")
                .replace("(brclose)", "]"));
        if (item.getRole().equals("ADMIN")) {
            role.setChecked(true);
        } else {
            role.setChecked(false);
        }

        return newView;
    }
}
