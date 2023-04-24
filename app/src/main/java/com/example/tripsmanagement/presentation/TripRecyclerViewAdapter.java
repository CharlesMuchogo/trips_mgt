package com.example.tripsmanagement.presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tripsmanagement.R;
import com.example.tripsmanagement.data.DatabaseClient;
import com.example.tripsmanagement.data.Trip;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TripRecyclerViewAdapter extends RecyclerView.Adapter<TripRecyclerViewAdapter.TripViewHolder> implements Filterable{

    private Context context;
    private List<Trip> trips;
    private  ValueFilter valueFilter;


    public TripRecyclerViewAdapter(Context context, List<Trip> trips) {
        this.context = context;
        this.trips = trips;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trip_card_view, parent, false);
        return new TripViewHolder(view, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = trips.get(position);
        holder.tripName.setText(trip.getName());
        holder.destination.setText(trip.getDestination());
        holder.tripDate.setText(trip.getDate());
        Log.d("TAG", "onBindViewHolder: " + trips.toString());

        holder.deleteTrip.setOnClickListener(view -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Delete Trip");
            alertDialog.setCancelable(true);
            alertDialog.setMessage("Do you want to delete this Trip?");
            alertDialog.setNegativeButton(
                    "No",
                    (dialog, id) -> {
                        dialog.dismiss();
                    });
            alertDialog.setPositiveButton(
                    "Yes",
                    (dialog, id) -> {
                        deleteTrip(trip);
                        dialog.dismiss();
                    });
            alertDialog.show();

        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<Trip> filterList = new ArrayList<>();
                for (int i = 0; i < trips.size(); i++) {
                    if ((trips.get(i).name.toUpperCase()).contains(constraint.toString().toUpperCase()) || (trips.get(i).getDate().toUpperCase()).contains(constraint.toString().toUpperCase()) || (trips.get(i).getDestination().toUpperCase()).contains(constraint.toString().toUpperCase()) || (trips.get(i).getDescription().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(trips.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = trips.size();
                results.values = trips;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            trips = (List<Trip>) results.values;
            notifyDataSetChanged();
        }

    }

    public class TripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tripName;
        public TextView destination;
        public TextView tripDate;
        private ImageView deleteTrip;

        public TripViewHolder(@NonNull View itemView, ViewGroup parent) {
            super(itemView);
            tripName = itemView.findViewById(R.id.tripName);
            destination = itemView.findViewById(R.id.destination);
            tripDate = itemView.findViewById(R.id.tripDate);
            deleteTrip = itemView.findViewById(R.id.deleteTrip);

            itemView.setOnClickListener(view -> {
                        Trip trip = trips.get(getAdapterPosition());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("trip", trip);
//                        Navigation.findNavController(view).navigate(R.id.action_home_to_addExpense, bundle);

            });


        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){

                default:
                    Trip trip = trips.get(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("trip", trip);
                    Navigation.findNavController(view).navigate(R.id.action_home_to_addExpense, bundle);
                    break;
            }
        }
    }

    private void deleteTrip(Trip trip){
        @SuppressLint("StaticFieldLeak")
        class DeleteTrip extends AsyncTask<Void, Void, Void> {
            @SuppressLint("NewApi")
            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase().TripDao().delete(trip.id);

                return null;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Toast toast = Toast.makeText(context,"Trip Deleted successfully", Toast.LENGTH_SHORT );
                toast.show();
                trips.remove(trip);
                notifyDataSetChanged();
            }
        }

        DeleteTrip deleteTrip = new DeleteTrip();
        deleteTrip.execute();
    }

}