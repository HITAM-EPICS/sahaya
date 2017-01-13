package hitam.epics.sahaya;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends Activity {

    private ImageView logo;

    private ViewFlipper flipper;

    private FirebaseAuth mAuth;
    @Nullable
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logo = (ImageView) findViewById(R.id.logo);

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
                    Log.e("onAuthStateChanged: ", user.getProviders().get(0));
                    if (!user.getProviders().get(0).equals("password") || user.isEmailVerified()) {
                        startActivity(new Intent(HomeActivity.this, DashboardActivity.class));
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
        Intent intent = new Intent(this, LoginActivity.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this, HomeActivity.this.logo, "logo");
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    public void register(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    public void about(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }
}
