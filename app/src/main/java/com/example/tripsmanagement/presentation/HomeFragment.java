package com.example.tripsmanagement.presentation;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.tripsmanagement.R;
import com.example.tripsmanagement.data.DatabaseClient;
import com.example.tripsmanagement.data.ExportData;
import com.example.tripsmanagement.data.Trip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private View view;
    private FloatingActionButton floatingActionButton;
    private RecyclerView tripsView;
    private SearchView search_view;
    private List<Trip> trips;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        tripsView = view.findViewById(R.id.tripsView);
        search_view = view.findViewById(R.id.search_view);
        trips = new ArrayList<>();

        floatingActionButton = view.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_addTripFragment);
        });

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                RecyclerView recyclerView = view.findViewById(R.id.tripsView);
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                TripRecyclerViewAdapter adapter = new TripRecyclerViewAdapter(requireContext(), trips);
                recyclerView.setAdapter(adapter);
                adapter.getFilter().filter(s);





                return false;
            }
        });

        getTrips();
        return view;
    }

    private void getTrips() {
        @SuppressLint("StaticFieldLeak")
        class GetTrips extends AsyncTask<Void, Void, List<Trip>> {

            @Override
            protected List<Trip> doInBackground(Void... voids) {
                return DatabaseClient.getInstance(requireContext())
                        .getAppDatabase().TripDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Trip> tripslist) {
                super.onPostExecute(trips);
                trips = tripslist;
                RecyclerView recyclerView = view.findViewById(R.id.tripsView);
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                TripRecyclerViewAdapter adapter = new TripRecyclerViewAdapter(requireContext(), trips);
                recyclerView.setAdapter(adapter);
                ExportData exportData = new ExportData();
                exportData.exportData(requireContext());
            }
        }
        GetTrips getTrips = new GetTrips();
        getTrips.execute();
    }
}
