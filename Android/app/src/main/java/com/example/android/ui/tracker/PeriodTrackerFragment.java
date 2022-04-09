package com.example.android.ui.tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.ArrayList;

public class PeriodTrackerFragment extends Fragment {

    private PeriodTrackerViewModel periodTrackerViewModel;
    Button startDate, endDate,go;
    TextView results;
    static int m2, d2;
    AutoCompleteTextView customerAutoTV;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        periodTrackerViewModel = new ViewModelProvider(this).get(PeriodTrackerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_period_tracker, container, false);

        customerAutoTV = root.findViewById(R.id.customerTextView);
        startDate = root.findViewById(R.id.startDate);
        endDate = root.findViewById(R.id.endDate);
        go = root.findViewById(R.id.go);
        results = root.findViewById(R.id.results);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String end = endDate.getText().toString();
                int day = findDay(end);
                int month = findMonth(end);
                int year = findYear(end);
                int days = 28;
                String newDate = addDays(day,month,year,days);
                results.setText("Predicted Date: "+newDate);
            }
        });
        initialiseSpinner();
        initDates();

        return root;
    }

    static boolean isLeap(int y)
    {
        if (y % 100 != 0 && y % 4 == 0 || y % 400 == 0)
            return true;

        return false;
    }

    static int offsetDays(int d, int m, int y)
    {
        int offset = d;

        if(m - 1 == 11)
            offset += 335;
        if(m - 1 == 10)
            offset += 304;
        if(m - 1 == 9)
            offset += 273;
        if(m - 1 == 8)
            offset += 243;
        if(m - 1 == 7)
            offset += 212;
        if(m - 1 == 6)
            offset += 181;
        if(m - 1 == 5)
            offset += 151;
        if(m - 1 == 4)
            offset += 120;
        if(m - 1 == 3)
            offset += 90;
        if(m - 1 == 2)
            offset += 59;
        if(m - 1 == 1)
            offset += 31;

        if (isLeap(y) && m > 2)
            offset += 1;

        return offset;
    }

    static void revoffsetDays(int offset, int y)
    {
        int []month={ 0, 31, 28, 31, 30, 31, 30,
                31, 31, 30, 31, 30, 31 };

        if (isLeap(y))
            month[2] = 29;

        int i;
        for (i = 1; i <= 12; i++)
        {
            if (offset <= month[i])
                break;
            offset = offset - month[i];
        }

        d2 = offset;
        m2 = i;
    }

    // Add x days to the given date.
    static String addDays(int d1, int m1, int y1, int x)
    {
        int offset1 = offsetDays(d1, m1, y1);
        int remDays = isLeap(y1) ? (366 - offset1) : (365 - offset1);

        int y2, offset2 = 0;
        if (x <= remDays)
        {
            y2 = y1;
            offset2 =offset1 + x;
        }

        else
        {
            x -= remDays;
            y2 = y1 + 1;
            int y2days = isLeap(y2) ? 366 : 365;
            while (x >= y2days)
            {
                x -= y2days;
                y2++;
                y2days = isLeap(y2) ? 366 : 365;
            }
            offset2 = x;
        }
        revoffsetDays(offset2, y2);
        StringBuilder sb = new StringBuilder("");
        sb.append(String.valueOf(d2)+"/"+String.valueOf(m2)+"/"+String.valueOf(y2));
        return sb.toString();
    }

    private int findYear(String end) {
        int space = end.lastIndexOf(" ");
        StringBuilder sb = new StringBuilder("");
        for(int i=space+1;i<end.length();i++){
            sb.append(String.valueOf(end.charAt(i)));
        }
        return Integer.parseInt(sb.toString());
    }

    private int findMonth(String end) {
        StringBuilder sb = new StringBuilder("");
        sb.append(String.valueOf(end.charAt(0)));
        sb.append(String.valueOf(end.charAt(1)));
        sb.append(String.valueOf(end.charAt(2)));
        String month = sb.toString();
        if(month.equals("Jan")){
            return 1;
        }
        if(month.equals("Feb")){
            return 2;
        }
        if(month.equals("Mar")){
            return 3;
        }
        if(month.equals("Apr")){
            return 4;
        }
        if(month.equals("May")){
            return 5;
        }
        if(month.equals("Jun")){
            return 6;
        }
        if(month.equals("Jul")){
            return 7;
        }
        if(month.equals("Aug")){
            return 8;
        }
        if(month.equals("Sep")){
            return 9;
        }
        if(month.equals("Oct")){
            return 10;
        }
        if(month.equals("Nov")){
            return 11;
        }
        if(month.equals("Dec")){
            return 12;
        }
        return -1;

    }

    private int findDay(String end) {
        int space = end.indexOf(" ");
        int comma = end.indexOf(",");
        StringBuilder sb = new StringBuilder("");
        for(int i=space+1;i<comma;i++){
            sb.append(String.valueOf(end.charAt(i)));
        }
        return Integer.parseInt(sb.toString());
    }

    private void initDates() {
        MaterialDatePicker.Builder sdBuilder = MaterialDatePicker.Builder.datePicker();
        sdBuilder.setTitleText("SELECT APPOINTMENT DATE");
        final MaterialDatePicker sdPicker = sdBuilder.build();
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sdPicker.show(getParentFragmentManager()  , "MATERIAL_DATE_PICKER");
            }
        });
        sdPicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                startDate.setText(sdPicker.getHeaderText());
            }
        });

        MaterialDatePicker.Builder edBuilder = MaterialDatePicker.Builder.datePicker();
        edBuilder.setTitleText("SELECT APPOINTMENT DATE");
        final MaterialDatePicker edPicker = edBuilder.build();
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edPicker.show(getParentFragmentManager()  , "MATERIAL_DATE_PICKER");
            }
        });
        edPicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                endDate.setText(edPicker.getHeaderText());
            }
        });
    }

    private void initialiseSpinner() {

        ArrayList<String> customerList = getMonthList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, customerList);

        customerAutoTV.setAdapter(adapter);

    }

    private ArrayList<String> getMonthList() {
        ArrayList<String> customers = new ArrayList<>();
        customers.add("January");
        customers.add("February");
        customers.add("March");
        customers.add("April");
        customers.add("May");
        customers.add("June");
        customers.add("July");
        customers.add("August");
        customers.add("September");
        customers.add("October");
        customers.add("November");
        customers.add("December");
        return customers;
    }

}
