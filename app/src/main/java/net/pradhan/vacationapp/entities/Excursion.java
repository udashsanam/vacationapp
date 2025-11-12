package net.pradhan.vacationapp.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName ="excursions")
public class Excursion {

    @PrimaryKey(autoGenerate = true)
    private int excursionId;
    private String title;

    private int vacationId;

    private String startDate;

    private int done;

    public Excursion(int excursionId, String title, int vacationId, String startDate, int done) {
        this.excursionId = excursionId;
        this.title = title;
        this.vacationId = vacationId;
        this.startDate = startDate;
        this.done = done;
    }

    public int getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(int excursionId) {
        this.excursionId = excursionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }
}
