package com.example.tripsmanagement.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Expense", foreignKeys = @ForeignKey(entity = Trip.class,
        parentColumns = "id",
        childColumns = "trip_id",
        onDelete = ForeignKey.CASCADE))

public class Expense {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "trip_id")
    public int trip_id;

    @ColumnInfo(name = "expense_type")
    public String expense_type;
    @ColumnInfo(name = "amount")
    public String amount;
    @ColumnInfo(name = "expense_time")
    public String expense_time;
    @ColumnInfo(name = "additional_comments")
    public String additional_comments;


    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpense_type() {
        return expense_type;
    }

    public void setExpense_type(String expense_type) {
        this.expense_type = expense_type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpense_time() {
        return expense_time;
    }

    public void setExpense_time(String expense_time) {
        this.expense_time = expense_time;
    }

    public String getAdditional_comments() {
        return additional_comments;
    }

    public void setAdditional_comments(String additional_comments) {
        this.additional_comments = additional_comments;
    }
}
