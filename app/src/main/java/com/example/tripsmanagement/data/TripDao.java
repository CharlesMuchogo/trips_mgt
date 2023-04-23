package com.example.tripsmanagement.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface TripDao {
    @Query("SELECT * FROM Trips ORDER BY id DESC")
    List<Trip> getAll();



    @Query("DELETE FROM Trips WHERE id= :tripId")
    void delete(int tripId);

    @Query("DELETE FROM Trips")
    void deleteAll();

    @Insert
    void insertAll(Trip... trips);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Trip patient);



}
