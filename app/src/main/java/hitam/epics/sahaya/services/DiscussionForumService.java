package hitam.epics.sahaya.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Iterator;

import hitam.epics.sahaya.R;
import hitam.epics.sahaya.Sahaya;
import hitam.epics.sahaya.volunteer.DiscussionForumListActivity;

/**
 * @author sanjit
 */
public class DiscussionForumService extends Service {
    private DatabaseReference discussionRef;
    private SharedPreferences sharedPref;

    /*Constructor*/
    public DiscussionForumService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        /*Get Firebase Authentication Instance*/
        FirebaseAuth auth = FirebaseAuth.getInstance();

        /*Get Firebase Database Instance*/
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        /*Get discussion database reference*/
        discussionRef = database.getReference("discussion_forum");

        /*Get Default Shared Preferences associated with app*/
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        /*ChildEventListener to handle discussion database changes*/
        final ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                sendNotification(dataSnapshot, s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        /*set childListener to database reference based on user*/
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    discussionRef.addChildEventListener(childEventListener);
                } else {
                    discussionRef.removeEventListener(childEventListener);
                }
            }
        });
        super.onCreate();
    }

    public void sendNotification(DataSnapshot dataSnapshot, String s) {
        /*Retrieve last read message time from preferences*/
        long lastMessageTime = sharedPref.getLong("last_message", System.currentTimeMillis());

        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
        DataSnapshot child = iterator.next();
        while (iterator.hasNext()) {
            child = iterator.next();
        }

        Long key = Long.valueOf(child.getKey());

        /*compare time of last read message and message from database*/
        if (key > lastMessageTime) {
                    /*builder for push notification*/
            Notification.Builder builder = new Notification.Builder(DiscussionForumService.this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Sahaya Discussion Forum")
                    .setContentText("Your have new discussion messages in " + dataSnapshot.getKey());

                    /*intent for push notification*/
            Intent resultIntent = new Intent(DiscussionForumService.this, DiscussionForumListActivity.class);

                    /*build stack for intent calls*/
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(DiscussionForumService.this);
            stackBuilder.addParentStack(Sahaya.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

                    /*add resultPendingIndent to builder*/
            builder.setContentIntent(resultPendingIntent);

                    /*build push notification*/
            Notification notification = builder.build();
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;

                    /*get notification manager*/
            NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

                    /*show notification*/
            notificationManager.notify(Integer.parseInt(dataSnapshot.getKey()), notification);

        }

                /*set last read message time as current time*/
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("last_message", System.currentTimeMillis());
        editor.apply();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*start service in STICKY mode*/
        return START_STICKY;
    }
}
