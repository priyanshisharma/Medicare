package com.example.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;*/

import java.util.ArrayList;

public class CallsActivity extends AppCompatActivity {

    public int ActivityNumPat = 2;
    public int ActivityNumDoc = 1;
    private SharedPreferences prefs;
    String currUserRole;
    private BottomNavigationView bottomNavigationView;
    ArrayList<CallInfoModel> callInfos;
    ArrayList<String> callRoomIds;
    long numOfCalls;
    int currCount=0;
    ListView callList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("role",MODE_PRIVATE);
        currUserRole = prefs.getString("userRole","");

        setContentView(R.layout.activity_calls);
        setUpBottomNavigationViewForDoctor();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        currUserRole = "doctor";

        callList = findViewById(R.id.callList);
        initCallList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.patient_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        signoutCurrUser();
        return(super.onOptionsItemSelected(item));
    }

    private void signoutCurrUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user!=null){
            getSharedPreferences("role",MODE_PRIVATE).edit().clear().commit();
            Intent intent = new Intent(CallsActivity.this,LoginActivity.class);
            auth.signOut();
            startActivity(intent);
            finish();
        }
    }

    /*private void startmeet(String code){
        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder().setRoom(code).setWelcomePageEnabled(false).build();
        JitsiMeetActivity.launch(CallsActivity.this,options);
    }*/

    private void initCallList() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child(currUserRole).child(userID);
        callRoomIds = new ArrayList<>();
        callInfos = new ArrayList<>();
        myref.child("callRooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numOfCalls = snapshot.getChildrenCount();
                for(DataSnapshot ds:snapshot.getChildren()){
                    callRoomIds.add(ds.getKey());
                }
                addInfosForCallRooms();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CallsActivity.this,"Cancelled "+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addInfosForCallRooms() {
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("callRooms");
        for(String callRoomID: callRoomIds){
            myref.child(callRoomID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    CallInfoModel infoModel = snapshot.getValue(CallInfoModel.class);
                    callInfos.add(infoModel);
                    currCount++;
                    if(currCount==numOfCalls){
                        addAdapterToActivity();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void addAdapterToActivity() {
        CallListAdapter adapter = new CallListAdapter(this,callInfos);
        callList.setAdapter(adapter);
    }

    private void setUpBottomNavigationViewForDoctor() {
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavViewBar);
        bottomNavigationView.setItemIconTintList(null);
        BottomNavigationHelperForDoctor.enableNavigation(CallsActivity.this,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ActivityNumDoc);
        menuItem.setChecked(true);
    }

    /*public void goToMeet(View view) {
        TextView linkTxt = view.findViewById(R.id.link);
        startmeet(linkTxt.getText().toString());
        //Toast.makeText(CallsActivity.this,linkTxt.getText().toString(),Toast.LENGTH_SHORT).show();
    }

    private void startmeet(String code){
        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder().setRoom(code).setWelcomePageEnabled(false).build();
        JitsiMeetActivity.launch(CallsActivity.this,options);
    }*/
}