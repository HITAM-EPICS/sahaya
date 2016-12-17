package hitam.epics.sahaya;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void register(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
        finish();
    }

    public void login(View view) {
        Snackbar.make(view,"Not Implemented",Snackbar.LENGTH_SHORT).show();
    }
}
