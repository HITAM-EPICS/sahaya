package hitam.epics.sahaya.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
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

import hitam.epics.sahaya.DiscussionActivity;
import hitam.epics.sahaya.R;
import hitam.epics.sahaya.support.DiscussionMessage;

public class DiscussionForumService extends Service {
    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference discussionRef;

    public DiscussionForumService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        discussionRef = database.getReference("discussion");
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        final ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                long lastMessageTime = sharedPref.getLong("last_message", System.currentTimeMillis());

                DiscussionMessage message = dataSnapshot.getValue(DiscussionMessage.class);

                if (message.getTime() > lastMessageTime) {
                    Notification.Builder builder = new Notification.Builder(DiscussionForumService.this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(message.getName())
                            .setContentText(message.getMessage());

                    Intent resultIntent = new Intent(DiscussionForumService.this, DiscussionActivity.class);

                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(DiscussionForumService.this);
                    stackBuilder.addParentStack(DiscussionActivity.class);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );


                    builder.setContentIntent(resultPendingIntent);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

                    notificationManager.notify(0, builder.build());
                }

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putLong("last_message", System.currentTimeMillis());
                editor.apply();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
