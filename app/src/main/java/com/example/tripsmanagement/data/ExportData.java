package com.example.tripsmanagement.data;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.tripsmanagement.export.ApiConfig;
import com.example.tripsmanagement.export.AppConfig;
import com.example.tripsmanagement.export.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

import retrofit2.Callback;

public class ExportData {


    public void exportData(Context context){
        generateJson(context);

    }


    private void generateJson(Context context) {
        @SuppressLint("StaticFieldLeak")
        class GenerateJson extends AsyncTask<Void, Void, JSONObject> {

            @Override
            protected JSONObject doInBackground(Void... voids) {
                JSONObject json = new JSONObject();
                try {
                    List<Trip> trips = DatabaseClient.getInstance(context)
                            .getAppDatabase().TripDao().getAll();
                    List<Expense> expenses = DatabaseClient.getInstance(context)
                            .getAppDatabase().ExpenseDao().getAll();

                    JSONArray tripArray = new JSONArray();
                    for (Trip trip : trips) {
                        JSONObject tripObject = new JSONObject();
                        tripObject.put("id", trip.id);
                        tripObject.put("name", trip.name);
                        tripObject.put("destination", trip.destination);
                        tripObject.put("date", trip.date);
                        tripObject.put("risk_assessment", trip.risk_assessment);
                        tripObject.put("description", trip.description);
                        tripObject.put("dateCreated", trip.dateCreated);

                        // Find expenses for this trip
                        JSONArray expenseArray = new JSONArray();
                        for (Expense expense : expenses) {
                            if (expense.trip_id == trip.id) {
                                JSONObject expenseObject = new JSONObject();
                                expenseObject.put("id", expense.id);
                                expenseObject.put("expense_type", expense.expense_type);
                                expenseObject.put("amount", expense.amount);
                                expenseObject.put("expense_time", expense.expense_time);
                                expenseObject.put("additional_comments", expense.additional_comments);
                                expenseArray.put(expenseObject);
                            }
                        }
                        tripObject.put("expenses", expenseArray);
                        tripArray.put(tripObject);
                    }
                    json.put("trips", tripArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }

            @Override
            protected void onPostExecute(JSONObject json) {
                super.onPostExecute(json);
                //upload the json
                uploadJson(context, json);

            }
        }
        GenerateJson generateJson = new GenerateJson();
        generateJson.execute();
    }

    private void uploadJson(Context context, JSONObject json) {

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        retrofit2.Call<ServerResponse> call = getResponse.uploadJson(json);
        call.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(retrofit2.Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(context, serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }

            }

            @Override
            public void onFailure(retrofit2.Call<ServerResponse> call, Throwable t) {
                Toast.makeText(context, "Could not upload Json", Toast.LENGTH_SHORT).show();

            }


        });
    }


}
