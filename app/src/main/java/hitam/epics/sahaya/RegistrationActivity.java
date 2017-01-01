package hitam.epics.sahaya;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class RegistrationActivity extends Activity {
    private FirebaseAuth mAuth;
    private ScrollView registrationFormLinearLayout;
    private LinearLayout loadingLinearLayout;
    private EditText name;
    private EditText email;
    private EditText phone;
    private EditText occupation;
    private EditText password;
    private EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registrationFormLinearLayout = (ScrollView) findViewById(R.id.registration_form);
        loadingLinearLayout = (LinearLayout) findViewById(R.id.registration_loading);

        name = (EditText) findViewById(R.id.registration_name);
        email = (EditText) findViewById(R.id.registration_email);
        password = (EditText) findViewById(R.id.registration_password);
        confirmPassword = (EditText) findViewById(R.id.registration_confirm_password);
        phone = (EditText) findViewById(R.id.registration_phone);
        occupation = (EditText) findViewById(R.id.registration_occupation);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
    }


    public void register(View view) {
        if (valid()) {
            registrationFormLinearLayout.setVisibility(View.GONE);
            loadingLinearLayout.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("FIREBASE", "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                registrationFormLinearLayout.setVisibility(View.VISIBLE);
                                loadingLinearLayout.setVisibility(View.GONE);
                            }
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name.getText().toString())
                                    .build();
                            if (user != null) {
                                user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                RegistrationActivity.this.finish();
                                            }
                                        });
                                    }
                                });
                                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(RegistrationActivity.this);
//                                String phoneNumber = phone.getEditText().getText().toString().trim();
//                                if (phoneNumber.length() > 0)
//                                    mFirebaseAnalytics.setUserProperty("phone", phoneNumber);
                                String occupationOfUser = occupation.getText().toString().trim();
                                if (occupationOfUser.length() > 0)
                                    mFirebaseAnalytics.setUserProperty("occupation", occupationOfUser);
                            }
                        }
                    });
        }
    }

    private boolean valid() {
        boolean result = true;
        name.setError("");
        email.setError("");
        password.setError("");
        confirmPassword.setError("");
        if (Objects.equals(name.getText().toString(), "")) {
            name.setError("Required");
            result = false;
        }
        if (Objects.equals(email.getText().toString(), "")) {
            email.setError("Required");
            result = false;
        }
        if (password.getText().toString().length() < 6) {
            password.setError("Required: Minimum 6 Characters");
            result = false;
        } else if (!Objects.equals(confirmPassword.getText().toString(), password.getText().toString())) {
            confirmPassword.setError("Passwords do not match");
            result = false;
        }
        return result;
    }
}
