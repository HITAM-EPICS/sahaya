package hitam.epics.sahaya;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }


    public void register(View view) {
        TextInputLayout email = (TextInputLayout) findViewById(R.id.registration_email);
        TextInputLayout password = (TextInputLayout) findViewById(R.id.registration_password);

        new Sahaya().register(email.getEditText().getText().toString(), password.getEditText().getText().toString());
    }
}
