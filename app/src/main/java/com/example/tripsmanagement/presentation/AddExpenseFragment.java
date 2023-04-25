package com.example.tripsmanagement.presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripsmanagement.R;
import com.example.tripsmanagement.data.DatabaseClient;
import com.example.tripsmanagement.data.Expense;
import com.example.tripsmanagement.data.Trip;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AddExpenseFragment extends Fragment {

   private Spinner activityType;
   private String activity_type_result;
   private EditText amount, comments,expensedate;
   private MaterialButton save_Expense;
    private Calendar myCalendar = Calendar.getInstance();
    private Trip trip;

    String myFormat="dd/MM/yyyy";
    SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityType = view.findViewById(R.id.expenseType);
         amount = view.findViewById(R.id.amount);
         comments = view.findViewById(R.id.comments);
        expensedate = view.findViewById(R.id.date);
        save_Expense = view.findViewById(R.id.save_Expense);
        if(getArguments() != null){
            trip = (Trip) getArguments().getSerializable("trip");
        }

        ArrayAdapter<CharSequence> num_fetus_adapter = ArrayAdapter.createFromResource(getContext(), R.array.num_fetus_array, android.R.layout.simple_spinner_item);
        num_fetus_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityType.setAdapter(num_fetus_adapter);
        activityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                activity_type_result = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                activity_type_result = "N/A";
            }
        });

        DatePickerDialog.OnDateSetListener date = (datePicker, year, month, day) -> {
            Log.d("month", "onViewCreated: "+month);
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month );
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            updateLabel();
        };

        updateLabel();
        expensedate.setOnClickListener(view1 -> {
            DatePickerDialog datePicker = new DatePickerDialog(this.getContext(), date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
            Calendar now = Calendar.getInstance();
            datePicker.getDatePicker().setMinDate(now.getTimeInMillis());
            datePicker.show();
        });



        save_Expense.setOnClickListener(view1 -> {

            boolean errors = false;
            if(amount.getText().toString().equals("")){
                errors = true;
                this.amount.setError("Amount field is required");
                this.amount.requestFocus();
            }

            if(activity_type_result.equals("N/A")){
                errors = true;
                View spinnerView = activityType.getSelectedView();
                ((TextView) spinnerView).setError("Activity type is required");

            }


            if(errors){
                return;
            }else{

                Expense expense = new Expense();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());

                expense.setExpense_time(dateFormat.format(myCalendar.getTime()));
                expense.setExpense_type(activity_type_result);
                expense.setAmount(amount.getText().toString());
                expense.setTrip_id(trip.id);
                expense.setAdditional_comments(comments.getText().toString());

                alertDialog.setTitle("Expense Details");


                alertDialog.setMessage("Expense Type: "+ activity_type_result+ "\n\nExpense Amount: "+ amount.getText().toString() + "\n\nAdditional comments: "+ comments.getText().toString() + "\n\nExpense date: "+ dateFormat.format(myCalendar.getTime()));
                alertDialog.setPositiveButton(
                        "Save",
                        (dialog, id) ->{
                            saveExpenseLocally(expense);

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

    public void saveExpenseLocally(Expense expense){


        @SuppressLint("StaticFieldLeak")
        class SaveTrip extends AsyncTask<Void, Void, Void> {
            @SuppressLint("NewApi")
            @Override
            protected Void doInBackground(Void... voids) {


                DatabaseClient.getInstance(requireContext()).getAppDatabase()
                        .ExpenseDao()
                        .insert(expense);

                return null;
            }


            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Bundle bundle = new Bundle();
                bundle.putSerializable("trip", trip);
                Toast.makeText(requireContext(), "Expense saved successfully" , Toast.LENGTH_LONG).show();
                Navigation.findNavController(requireView()).navigate(R.id.action_addExpenseFragment_to_expenseFragment, bundle);



            }
        }

        SaveTrip saveTrip = new SaveTrip();
        saveTrip.execute();
    }

    private void updateLabel(){
        expensedate.setText(dateFormat.format(myCalendar.getTime()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expense, container, false);
    }
}