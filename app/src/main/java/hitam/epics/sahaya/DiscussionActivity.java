package hitam.epics.sahaya;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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
    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference discussionRef;

    private String userName;
    private ListView discussionMessagesListView;
    private DiscussionAdapter discussionAdapter;
    private ArrayList<DiscussionMessage> discussionMessages;
    private EditText messageInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        discussionMessagesListView = (ListView) findViewById(R.id.discussion_message_list);
        messageInput = (EditText) findViewById(R.id.message_input);

        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        discussionRef = database.getReference("discussion");

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userName = user.getDisplayName();
        }

        discussionMessages = new ArrayList<>();
        discussionAdapter = new DiscussionAdapter(this, discussionMessages);
        discussionMessagesListView.setAdapter(discussionAdapter);

        TextView emptyView = (TextView) findViewById(R.id.empty_view);
        discussionMessagesListView.setEmptyView(emptyView);

        discussionRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DiscussionMessage message = dataSnapshot.getValue(DiscussionMessage.class);
                addMessage(message);
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
        });
    }

    public void SendMessage(View view) {
        String message = messageInput.getText().toString().trim();
        DiscussionMessage newMessage = new DiscussionMessage(userName, message);
        discussionRef.child(newMessage.getTime() + "").setValue(newMessage);
        messageInput.setText("");
    }

    private void addMessage(DiscussionMessage message) {
        discussionMessages.add(message);
        if (discussionMessages.size() > 30) {
            discussionMessages.remove(0);
        }
        discussionAdapter.notifyDataSetChanged();
        discussionMessagesListView.setSelection(discussionMessages.size() - 1);
    }
}
