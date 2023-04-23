package com.example.tripsmanagement.data;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {
    private static DatabaseClient myInstance;
    private final AppDatabase appDatabase;

    private DatabaseClient(Context context){
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "trips")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized DatabaseClient getInstance(Context context){
        if (myInstance == null){
            myInstance = new DatabaseClient(context);
        }
        return myInstance;
    }

    public AppDatabase getAppDatabase(){
        return appDatabase;
    }
}
