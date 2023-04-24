package com.example.tripsmanagement.presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tripsmanagement.R;
import com.example.tripsmanagement.data.DatabaseClient;
import com.example.tripsmanagement.data.Trip;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTripFragment extends Fragment {

    private EditText name;
    private EditText destination;
    private EditText description;
    private EditText dateText;
    private CheckBox checkbox;
    private MaterialButton save_Trip;
    private ProgressBar addTripProgressBar;
    private Calendar myCalendar;
    String myFormat="dd/MM/yyyy";
    SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add_trip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.name);
        dateText = view.findViewById(R.id.date);
        checkbox = view.findViewById(R.id.checkBox);
        save_Trip = view.findViewById(R.id.save_Trip);
        description = view.findViewById(R.id.description);
        destination = view.findViewById(R.id.destination);
        myCalendar = Calendar.getInstance();
        addTripProgressBar = view.findViewById(R.id.addTripProgressBar);

        DatePickerDialog.OnDateSetListener date = (datePicker, year, month, day) -> {
            Log.d("month", "onViewCreated: "+month);
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month );
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            updateLabel();
        };

        updateLabel();
        dateText.setOnClickListener(view1 -> {
            DatePickerDialog datePicker = new DatePickerDialog(this.getContext(), date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
            Calendar now = Calendar.getInstance();
            datePicker.getDatePicker().setMinDate(now.getTimeInMillis());
            datePicker.show();
        });

        save_Trip.setOnClickListener(view1 -> {
            boolean isChecked = checkbox.isChecked();

            boolean errors = false;
            if(name.equals("")){
                errors = true;
                this.name.setError("Name field is required");
                this.name.requestFocus();
            }

            if(destination.equals("")){
                errors = true;
                this.destination.setError("Destination field is required");
            }


            if(errors){
                return;
            }else{
                Trip trip = new Trip();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());

                trip.setName(name.getText().toString());
                trip.setDestination(destination.getText().toString());
                trip.setDescription(description.getText().toString());
                trip.setDate(dateFormat.format(myCalendar.getTime()));
                alertDialog.setTitle("Trip Details");

                if (isChecked) {
                    trip.setRisk_assessment("Yes");
                    alertDialog.setMessage("Trip Requires Risk Assesment ");

                }
                alertDialog.setMessage("Trip Name: "+ name.getText().toString()+ "\n\nTrip destination: "+ destination.getText().toString() + "\n\nTrip description: "+ description.getText().toString() + "\n\nTrip date: "+ dateFormat.format(myCalendar.getTime()));
                alertDialog.setPositiveButton(
                        "Save",
                        (dialog, id) ->{
                            save_Trip.setVisibility(View.GONE);
                            addTripProgressBar.setVisibility(View.VISIBLE);
                            saveTripLocally(trip);
                        });
                alertDialog.setNegativeButton(
                        "Edit",
                        (dialog, id) -> {
                            dialog.dismiss();
                        });
                alertDialog.show();




            }
        });






    }

    public void saveTripLocally(Trip trip){


        @SuppressLint("StaticFieldLeak")
        class SaveTrip extends AsyncTask<Void, Void, Void> {
            @SuppressLint("NewApi")
            @Override
            protected Void doInBackground(Void... voids) {


                DatabaseClient.getInstance(requireContext()).getAppDatabase()
                        .TripDao()
                        .insert(trip);

                return null;
            }


            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);

                Toast.makeText(requireContext(), "Trip saved successfully" , Toast.LENGTH_LONG).show();
                    Navigation.findNavController(requireView()).navigate(R.id.action_addPatientFragment_to_homeFragment);



            }
        }

        SaveTrip saveTrip = new SaveTrip();
        saveTrip.execute();
    }




    private void updateLabel(){
        dateText.setText(dateFormat.format(myCalendar.getTime()));

    }
}