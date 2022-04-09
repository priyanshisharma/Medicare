package com.example.android.ui.slideshow;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.gusakov.library.PulseCountDown;
import com.gusakov.library.java.interfaces.OnCountdownCompleted;

import java.util.ArrayList;


public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private Button start;
    private PulseCountDown countDown;
    private MediaPlayer media;
    private AutoCompleteTextView soundTrack;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);


        soundTrack = root.findViewById(R.id.customerTextView);
        initialiseSpinner();



        final NumberPicker min = root.findViewById(R.id.numpicker_minutes);
        min.setMaxValue(59);
        final NumberPicker sec = root.findViewById(R.id.numpicker_seconds);
        sec.setMaxValue(59);
        start = root.findViewById(R.id.startMeditation);
        countDown = root.findViewById(R.id.pulseCountDown);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mins = Integer.parseInt(String.valueOf(min.getValue()));
                int secs = Integer.parseInt(String.valueOf(sec.getValue()));
                String track = soundTrack.getText().toString().toLowerCase();
                if(track.equals("rain")){
                    media = MediaPlayer.create(getActivity(),R.raw.rain);
                }else{
                    media = MediaPlayer.create(getActivity(),R.raw.beach);
                }
                media.start();
                countDown.setStartValue(mins*60+secs);
                countDown.setTextSize(50);
                countDown.start(new OnCountdownCompleted() {
                    @Override
                    public void completed() {
                        Toast.makeText(getActivity(), "Good Job :)", Toast.LENGTH_SHORT).show();
                        media.stop();
                    }
                });
            }
        });


        return root;
    }

    private void initialiseSpinner() {

        ArrayList<String> customerList = getTrackList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, customerList);
        soundTrack.setAdapter(adapter);

    }

    private ArrayList<String> getTrackList() {
        ArrayList<String> sounds = new ArrayList<>();
        sounds.add("Beach");
        sounds.add("Rain");
        return sounds;
    }

}