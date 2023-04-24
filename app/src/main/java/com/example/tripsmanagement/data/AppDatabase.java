package com.example.tripsmanagement.data;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;



@Database(
        entities = {Trip.class , Expense.class},
        version = 3
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TripDao TripDao();
    public  abstract  ExpenseDao ExpenseDao();


    @Override
    public void clearAllTables() {

    }
    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(@NonNull DatabaseConfiguration databaseConfiguration) {
        return null;
    }
}
