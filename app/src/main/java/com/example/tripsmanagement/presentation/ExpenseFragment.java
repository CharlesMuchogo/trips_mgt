package com.example.tripsmanagement.presentation;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.tripsmanagement.R;
import com.example.tripsmanagement.data.DatabaseClient;
import com.example.tripsmanagement.data.Expense;
import com.example.tripsmanagement.data.Trip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ExpenseFragment extends Fragment {
    private View view;
    private FloatingActionButton floatingActionButton;
    private RecyclerView tripsView;
    private SearchView search_view;
    private List<Expense> expenses;
    private Trip trip;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_expense, container, false);
        tripsView = view.findViewById(R.id.tripsView);
        search_view = view.findViewById(R.id.search_view);
        expenses = new ArrayList<>();

        floatingActionButton = view.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("trip", trip);
            Navigation.findNavController(view).navigate(R.id.action_ExpenseFragment_to_addExpenseFragment, bundle);
        });

        if(getArguments() != null){
            trip = (Trip) getArguments().getSerializable("trip");
        }

        getExpenses();
        return view;
    }

    private void getExpenses() {
        @SuppressLint("StaticFieldLeak")
        class GetExpenses extends AsyncTask<Void, Void, List<Expense>> {

            @Override
            protected List<Expense> doInBackground(Void... voids) {
                return DatabaseClient.getInstance(requireContext())
                        .getAppDatabase().ExpenseDao().getAllTripExpenses(trip.id);
            }

            @Override
            protected void onPostExecute(List<Expense> expenses) {
                super.onPostExecute(expenses);
                expenses = expenses;
                RecyclerView recyclerView = view.findViewById(R.id.tripsView);
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                ExpenseRecyclerViewAdapter adapter = new ExpenseRecyclerViewAdapter(requireContext(), expenses);
                recyclerView.setAdapter(adapter);

            }
        }
        GetExpenses getTrips = new GetExpenses();
        getTrips.execute();
    }
}