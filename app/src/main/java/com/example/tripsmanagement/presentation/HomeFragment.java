package com.example.tripsmanagement.presentation;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripsmanagement.R;
import com.example.tripsmanagement.data.DatabaseClient;
import com.example.tripsmanagement.data.Trip;
import com.example.tripsmanagement.databinding.FragmentHomeBinding;

import java.util.List;

public class HomeFragment extends Fragment {
    private View view;
    private FragmentHomeBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        view = binding.getRoot();
        getTrips();
        return view;
    }

    private void getTrips(){
        @SuppressLint("StaticFieldLeak")
        class GetTrips extends AsyncTask<Void, Void, List<Trip>> {

            @Override
            protected List<Trip> doInBackground(Void... voids) {
                return DatabaseClient
                        .getInstance(requireContext())
                        .getAppDatabase()
                        .patientDao()
                        .getAll();
            }

            @SuppressLint({"LogNotTimber", "RestrictedApi"})
            @Override
            protected void onPostExecute(List<Trip> trips) {
                super.onPostExecute(trips);
                Log.d("App database", "onPostExecute: " + trips.size());
            }
        }

        GetTrips getPatients = new GetTrips();
        getPatients.execute();
    }
}