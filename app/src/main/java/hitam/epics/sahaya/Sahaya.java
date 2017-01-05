package hitam.epics.sahaya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import hitam.epics.sahaya.services.DiscussionForumService;

public class Sahaya extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sahaya);

        Intent intent = new Intent(this, DiscussionForumService.class);
        startService(intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Sahaya.this, HomeActivity.class);
                startActivity(intent);
                Sahaya.this.finish();
            }
        }, 1500);
    }

}