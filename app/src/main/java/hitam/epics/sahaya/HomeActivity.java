package hitam.epics.sahaya;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private ViewFlipper flipper;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        flipper = (ViewFlipper) findViewById(R.id.homeImageFlipper);
        flipper.setFlipInterval(4000);
        flipper.setAutoStart(true);
        flipper.setInAnimation(HomeActivity.this, R.anim.in_from_right);
        flipper.setOutAnimation(HomeActivity.this, R.anim.out_from_left);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if (user.isEmailVerified()) {
                        startActivity(new Intent(HomeActivity.this, DashboardActivity.class));
                        HomeActivity.this.finish();
                    } else {
                        Toast.makeText(HomeActivity.this, "Verify Your Email to login", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
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
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        flipper.startFlipping();
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
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void register(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    public void openAbout(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }

    public void donate(View view) {
        startActivity(new Intent(this, DonateActivity.class));
    }
}
