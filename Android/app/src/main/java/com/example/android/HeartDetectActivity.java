package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeartDetectActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    public int ActivityNum = 2;
    Button go;
    TextView results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_detect);
        setUpBottomNavigationViewForDoctor();
        go=findViewById(R.id.go);
        results = findViewById(R.id.results);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareData();
            }
        });
    }

    private void prepareData() {
        String age = ((EditText)findViewById(R.id.age)).getText().toString();
        String gender="Male";
        if(((RadioButton)findViewById(R.id.female)).isChecked()){
            gender="Female";
        }
        String cp = "0";
        if(((RadioButton)findViewById(R.id.atyp)).isChecked()){
            gender="1";
        }
        if(((RadioButton)findViewById(R.id.nonTyp)).isChecked()){
            gender="2";
        }
        if(((RadioButton)findViewById(R.id.asymp)).isChecked()){
            gender="3";
        }
        String trestbps = ((EditText)findViewById(R.id.restBP)).getText().toString();
        String chol = ((EditText)findViewById(R.id.serumChol)).getText().toString();
        String fbs="0";
        if(((RadioButton)findViewById(R.id.yesfbs)).isChecked()){
            fbs="1";
        }
        String restecg="0";
        if(((RadioButton)findViewById(R.id.stwaveecg)).isChecked()){
            restecg="1";
        }
        String thalach = ((EditText)findViewById(R.id.numMaxHeartRate)).getText().toString();
        String exang="0";
        if(((RadioButton)findViewById(R.id.yesExang)).isChecked()){
            exang="1";
        }
        String oldpeak = ((EditText)findViewById(R.id.numOldPeak)).getText().toString();
        String slope="0";
        if(((RadioButton)findViewById(R.id.flat)).isChecked()){
            slope="1";
        }
        if(((RadioButton)findViewById(R.id.downSlope)).isChecked()){
            slope="2";
        }
        String ca="0";
        if(((RadioButton)findViewById(R.id.one)).isChecked()){
            ca="1";
        }
        if(((RadioButton)findViewById(R.id.two)).isChecked()){
            ca="2";
        }
        if(((RadioButton)findViewById(R.id.three)).isChecked()){
            ca="3";
        }
        if(((RadioButton)findViewById(R.id.four)).isChecked()){
            ca="4";
        }
        String thal="0";
        if(((RadioButton)findViewById(R.id.thalFix)).isChecked()){
            thal="1";
        }
        if(((RadioButton)findViewById(R.id.thalRev)).isChecked()){
            thal="2";
        }

        HeartDetails details = new HeartDetails(age,gender,cp,trestbps,chol,fbs,restecg,thalach,exang,oldpeak,slope,ca,thal);
        makeCall(details);


    }

    private void makeCall(HeartDetails details) {
        Call<AuthResponse> call = AppClient.getInstance().createService(APIServices.class).getHeartDetails(details);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                try {
                    if (getApplicationContext() != null) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                AuthResponse authResponse = response.body();
                                results.setText("Probability of Heart Disease: "+authResponse.getBoolPrediction());
                            } else
                                Toast.makeText(HeartDetectActivity.this, "Couldn't log you in. Please try again.",Toast.LENGTH_SHORT).show();
                        } else if(response.code() == 400){
                            Toast.makeText(HeartDetectActivity.this, "Invalid Credentials!",Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(HeartDetectActivity.this, "Couldn't .", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {

            }
        });
    }

    private void setUpBottomNavigationViewForDoctor() {
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavViewBar);
        bottomNavigationView.setItemIconTintList(null);
        BottomNavigationHelperForDoctor.enableNavigation(HeartDetectActivity.this,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ActivityNum);
        menuItem.setChecked(true);
    }

}