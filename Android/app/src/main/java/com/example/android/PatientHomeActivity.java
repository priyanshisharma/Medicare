package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.android.ui.home.HomeFragment;
import com.example.android.ui.tracker.PeriodTrackerFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PatientHomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private HomeFragment home;
    //private SlideshowFragment slide = new SlideshowFragment();
    private PeriodTrackerFragment period = new PeriodTrackerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        home = new HomeFragment();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                home.resetArrays();
                Intent intent = new Intent(PatientHomeActivity.this,NewDoctorSearchActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_periodTracker)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void signoutCurrUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user!=null){
            getSharedPreferences("role",MODE_PRIVATE).edit().clear().commit();
            Intent intent = new Intent(PatientHomeActivity.this,LoginActivity.class);
            auth.signOut();
            startActivity(intent);
            finish();
        }
    }

    public void navigateToChatRoom(View view){
        TextView roomID = view.findViewById(R.id.roomID);
        String chatRoomID = roomID.getText().toString();
        findUserName(chatRoomID);
    }

    public void goToMeet(View view){
        TextView linkTxt = view.findViewById(R.id.link);
        //startmeet(linkTxt.getText().toString());
    }

    /*private void startmeet(String code){
        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder().setRoom(code).setWelcomePageEnabled(false).build();
        JitsiMeetActivity.launch(PatientHomeActivity.this,options);
    }*/

    private void findUserName(String roomID) {
        String currUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("patient").child(currUserId).child("info");
        ref.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.getValue(String.class);
                Intent intent = new Intent(PatientHomeActivity.this, ChatActivity.class);
                intent.putExtra("roomId",roomID);
                intent.putExtra("currUserName",username);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}