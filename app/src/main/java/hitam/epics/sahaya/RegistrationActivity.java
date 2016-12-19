package hitam.epics.sahaya;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ScrollView registrationFormLinearLayout;
    private LinearLayout loadingLinearLayout;
    private TextInputLayout name;
    private TextInputLayout email;
    private TextInputLayout phone;
    private TextInputLayout occupation;
    private TextInputLayout password;
    private TextInputLayout confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registrationFormLinearLayout = (ScrollView) findViewById(R.id.registration_form);
        loadingLinearLayout = (LinearLayout) findViewById(R.id.registration_loading);

        name = (TextInputLayout) findViewById(R.id.registration_name);
        email = (TextInputLayout) findViewById(R.id.registration_email);
        password = (TextInputLayout) findViewById(R.id.registration_password);
        confirmPassword = (TextInputLayout) findViewById(R.id.registration_confirm_password);
        phone = (TextInputLayout) findViewById(R.id.registration_phone);
        occupation = (TextInputLayout) findViewById(R.id.registration_occupation);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
    }


    public void register(View view) {
        if (valid()) {
            registrationFormLinearLayout.setVisibility(View.GONE);
            loadingLinearLayout.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email.getEditText().getText().toString(), password.getEditText().getText().toString())
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
                                    .setDisplayName(name.getEditText().getText().toString())
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
                                String occupationOfUser = occupation.getEditText().getText().toString().trim();
                                if (occupationOfUser.length() > 0)
                                    mFirebaseAnalytics.setUserProperty("occupation", occupationOfUser);
                            }
                        }
                    });
        }
    }

    private boolean valid() {
        boolean result = true;
        name.setErrorEnabled(false);
        email.setErrorEnabled(false);
        password.setErrorEnabled(false);
        confirmPassword.setErrorEnabled(false);
        if (Objects.equals(name.getEditText().getText().toString(), "")) {
            name.setError("Required");
            result = false;
        }
        if (Objects.equals(email.getEditText().getText().toString(), "")) {
            email.setError("Required");
            result = false;
        }
        if (password.getEditText().getText().toString().length() < 6) {
            password.setError("Required: Minimum 6 Characters");
            result = false;
        } else if (!Objects.equals(confirmPassword.getEditText().getText().toString(), password.getEditText().getText().toString())) {
            confirmPassword.setError("Passwords do not match");
            result = false;
        }
        return result;
    }
}
