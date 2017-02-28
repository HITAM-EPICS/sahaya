package hitam.epics.sahaya;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import jp.wasabeef.blurry.Blurry;

public class HomeActivity extends Activity {

    private FirebaseAuth mAuth;
    @Nullable
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ViewGroup dashboardBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dashboardBackground = (ViewGroup) findViewById(R.id.home_background);
        blurBackground();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    DatabaseReference reference = FirebaseDatabase
                            .getInstance()
                            .getReference("user_details");
                    if (reference.child(user.getUid()).getKey() == null) {
                        Intent additionalIntent = new Intent(HomeActivity.this, AdditionalInfoActivity.class);
                        startActivity(additionalIntent);
                    }
                    if (!user.getProviders().get(0).equals("password") || user.isEmailVerified()) {
                        startActivity(new Intent(HomeActivity.this, DashboardActivity.class));
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            finish();
                        }
                    } else {
                        Toast.makeText(HomeActivity.this, "Verify Your Email to login", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AppThemeAlert);
                        builder.setMessage("Please verify your email and then login. If you cant see the confirmation mail, check spam or click resend.")
                                .setPositiveButton("OK", null)
                                .setNeutralButton("Resend Mail", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        user.sendEmailVerification();
                                    }
                                });
                        builder.create().show();
                        mAuth.signOut();
                    }
                }
            }
        }

        ;
    }

    private void blurBackground() {
        dashboardBackground.post(new Runnable() {
            @Override
            public void run() {
                Blurry.with(HomeActivity.this)
                        .radius(10)
                        .sampling(2)
                        .async()
                        .onto(dashboardBackground);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    public void about(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }
}
