package hitam.epics.sahaya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import hitam.epics.sahaya.services.DiscussionForumService;
import jp.wasabeef.blurry.Blurry;

public class Sahaya extends Activity {

    private ViewGroup dashboardBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sahaya);
        dashboardBackground = (ViewGroup) findViewById(R.id.splash_background);
        blurBackground();

        Intent intent = new Intent(this, DiscussionForumService.class);
        startService(intent);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this, getString(R.string.facebook_app_id));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Sahaya.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }

    private void blurBackground() {
        dashboardBackground.post(new Runnable() {
            @Override
            public void run() {
                Blurry.with(Sahaya.this)
                        .radius(10)
                        .sampling(16)
                        .async()
                        .animate(500)
                        .onto(dashboardBackground);
            }
        });
    }
}