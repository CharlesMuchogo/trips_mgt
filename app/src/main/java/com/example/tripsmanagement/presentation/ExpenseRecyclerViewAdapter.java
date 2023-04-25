package com.example.tripsmanagement.presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
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
import com.example.tripsmanagement.data.Expense;

import java.util.ArrayList;
import java.util.List;


public class ExpenseRecyclerViewAdapter extends RecyclerView.Adapter<ExpenseRecyclerViewAdapter.ExpenseViewHolder> implements Filterable {

    private Context context;
    private List<Expense> expenses;
    private  ValueFilter valueFilter;


    public ExpenseRecyclerViewAdapter(Context context, List<Expense> expense) {
        this.context = context;
        this.expenses = expense;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.expense_card_view, parent, false);
        return new ExpenseViewHolder(view, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense trip = expenses.get(position);
        holder.amount.setText(trip.amount);
        holder.expenseType.setText(trip.expense_type);
        holder.expenseTime.setText(trip.expense_time);

        holder.deleteExpense.setOnClickListener(view -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Delete Expense");
            alertDialog.setCancelable(true);
            alertDialog.setMessage("Do you want to delete this expense?");
            alertDialog.setNegativeButton(
                    "No",
                    (dialog, id) -> {
                        dialog.dismiss();
                    });
            alertDialog.setPositiveButton(
                    "Yes",
                    (dialog, id) -> {
                        deleteExpense(trip);
                        dialog.dismiss();
                    });
            alertDialog.show();

        });
    }

    @Override
    public int getItemCount() {
        return expenses.size();
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
                List<Expense> filterList = new ArrayList<>();
                for (int i = 0; i < expenses.size(); i++) {
                    if ((expenses.get(i).expense_type.toUpperCase()).contains(constraint.toString().toUpperCase()) || (expenses.get(i).getAdditional_comments().toUpperCase()).contains(constraint.toString().toUpperCase()) || (expenses.get(i).getExpense_time().toUpperCase()).contains(constraint.toString().toUpperCase()) ) {
                        filterList.add(expenses.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = expenses.size();
                results.values = expenses;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            expenses = (List<Expense>) results.values;
            notifyDataSetChanged();
        }

    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder{
        public TextView expenseType;
        public TextView amount;
        public TextView expenseTime;
        private ImageView deleteExpense;

        public ExpenseViewHolder(@NonNull View itemView, ViewGroup parent) {
            super(itemView);
            expenseType = itemView.findViewById(R.id.expenseType);
            amount = itemView.findViewById(R.id.Amount);
            expenseTime = itemView.findViewById(R.id.expenseTime);
            deleteExpense =  itemView.findViewById(R.id.deleteExpense);



        }

    }

    private void deleteExpense(Expense expense){
        @SuppressLint("StaticFieldLeak")
        class DeleteExpense extends AsyncTask<Void, Void, Void> {
            @SuppressLint("NewApi")
            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase().ExpenseDao().delete(expense.id);

                return null;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Toast toast = Toast.makeText(context,"Expense Deleted successfully", Toast.LENGTH_SHORT );
                toast.show();
                expenses.remove(expense);
                notifyDataSetChanged();
            }
        }

        DeleteExpense deleteExpense = new DeleteExpense();
        deleteExpense.execute();
    }

}