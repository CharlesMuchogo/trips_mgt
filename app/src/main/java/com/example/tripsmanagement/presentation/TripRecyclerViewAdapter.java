package com.example.tripsmanagement.presentation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tripsmanagement.R;
import com.example.tripsmanagement.data.Trip;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TripRecyclerViewAdapter extends RecyclerView.Adapter<TripRecyclerViewAdapter.PatientViewHolder> {
   private final List<Trip> tripsList;
    private Calendar myCalendar;

    private String myFormat="dd MMMM";
    LayoutInflater inflater;
    Trip p;
    View dialogView;
    String  patientNumber;
    private EditText dischargeDate;
    private EditText dischargeNote;
   private  SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
   private final Context ctx;

    public TripRecyclerViewAdapter(Context context, List<Trip> patients){
        this.ctx = context;
        this.tripsList = patients;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.patients_card_view, parent, false);
        return new PatientViewHolder(view, parent);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        p = tripsList.get(position);



    }

    @Override
    public int getItemCount() {
        return tripsList.size();
    }

     class PatientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView patientsName, patientsAge, patientAdmissionDate;
        public PatientViewHolder(View itemView, ViewGroup parent){
            super(itemView);

            inflater = LayoutInflater.from(ctx);
            patientsName = itemView.findViewById(R.id.patientName);
            patientsAge = itemView.findViewById(R.id.patientAge);
            patientAdmissionDate =  itemView.findViewById(R.id.patientAdmissionDate);
            myCalendar = Calendar.getInstance();


            itemView.setOnClickListener(this);

        }

         @SuppressLint("NewApi")
         public LocalDateTime convertToLocalDateViaInstant(Date dateToConvert) {
             return dateToConvert.toInstant()
                     .atZone(ZoneId.systemDefault())
                     .toLocalDateTime();
         }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()){

                default:
                    Trip patient = tripsList.get(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("patient", patient);
//                    Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_patientsDetails, bundle);
                    break;
            }
        }

    }

    private void updateLabel(){
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dischargeDate.setText(dateFormat.format(myCalendar.getTime()));
    }
}
