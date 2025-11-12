package net.pradhan.vacationapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import net.pradhan.vacationapp.entities.Excursion;
import net.pradhan.vacationapp.entities.Vacation;

import java.util.List;

@Dao
public interface ExcursionDAO {

    @Insert(onConflict= OnConflictStrategy.IGNORE)
    void insert(Excursion excursion);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);

    @Query("SELECT * FROM excursions ORDER BY excursionId ASC")
    List<Excursion> getAllExcursions();

    @Query("SELECT * FROM excursions where vacationId = :vacationId")
    List<Excursion> getAllByVacationId(int vacationId);
}
