package hitam.epics.sahaya;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import hitam.epics.sahaya.support.DiscussionAdapter;
import hitam.epics.sahaya.support.DiscussionMessage;

public class DiscussionActivity extends Activity {
    private DatabaseReference discussionRef;


    private ChildEventListener childEventListener;
    private NotificationManager notificationManager;
    private String userName;
    private ListView discussionMessagesListView;
    private DiscussionAdapter discussionAdapter;
    private ArrayList<DiscussionMessage> discussionMessages;
    private EditText messageInput;
    private String center_name;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        extras = getIntent().getExtras();
        center_name = extras.getString("center_name");

        discussionMessagesListView = (ListView) findViewById(R.id.discussion_message_list);
        messageInput = (EditText) findViewById(R.id.message_input);

        notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        discussionRef = database.getReference("discussion_forum").child(center_name);

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userName = user.getDisplayName();
        }

        discussionMessages = new ArrayList<>();
        discussionAdapter = new DiscussionAdapter(this, discussionMessages);
        discussionMessagesListView.setAdapter(discussionAdapter);

        TextView emptyView = (TextView) findViewById(R.id.empty_view);
        discussionMessagesListView.setEmptyView(emptyView);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DiscussionMessage message = dataSnapshot.getValue(DiscussionMessage.class);
                addMessage(message);
                notificationManager.cancel(Integer.parseInt(center_name));
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

        messageInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    send();
                    return true;
                }
                return false;
            }
        });
    }

    public void SendMessage(View view) {
        send();
    }

    private void send() {
        String message = messageInput.getText().toString().trim();
        if (message.length() > 0) {
            DiscussionMessage newMessage = new DiscussionMessage(userName, message);
            discussionRef.child(newMessage.getTime() + "").setValue(newMessage);
        }
        messageInput.setText("");
    }

    private void addMessage(DiscussionMessage message) {
        discussionMessages.add(message);
        discussionAdapter.notifyDataSetChanged();
        discussionMessagesListView.setSelection(discussionMessages.size() - 1);
        if (discussionMessages.size() > 30) {
            DiscussionMessage message1 = discussionMessages.get(discussionMessages.size() - 31);
            discussionRef.child(String.valueOf(message1.getTime())).removeValue();
        }
    }

    @Override
    protected void onStop() {
        discussionRef.removeEventListener(childEventListener);
        super.onStop();
    }

    @Override
    protected void onResume() {
        discussionRef.limitToLast(30).addChildEventListener(childEventListener);
        super.onResume();
    }

    @Override
    protected void onPause() {
        discussionRef.removeEventListener(childEventListener);
        super.onPause();
    }

    public void showCenterDetail(View view) {
        Intent intent = new Intent(this, CenterDetailsActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
