package com.example.tripsmanagement.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Query("SELECT * FROM Expense ORDER BY id DESC")
    List<Expense> getAll();

    @Query("DELETE FROM Expense WHERE id= :expenseId")
    void delete(int expenseId);

    @Query("DELETE FROM Expense")
    void deleteAll();

    @Insert
    void insertAll(Expense... expenses);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Expense expense);

}
