package net.pradhan.vacationapp.repository;

import android.app.Application;

import net.pradhan.vacationapp.dao.ExcursionDAO;
import net.pradhan.vacationapp.dao.VacationDAO;
import net.pradhan.vacationapp.database.VacationDatabaseBuilder;
import net.pradhan.vacationapp.entities.Excursion;
import net.pradhan.vacationapp.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {

    private ExcursionDAO excursionDAO;
    private VacationDAO vacationDAO;
    private List<Vacation> vacationList;
    private List<Excursion> excursionList;

    private Excursion excursion;

    private Vacation vacation;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public Repository(Application application) {
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        excursionDAO = db.excursionDAO();
        vacationDAO = db.vacationDAO();
    }

    public List<Vacation> getVacationList() {
        databaseExecutor.execute(() -> {
            vacationList = vacationDAO.getAllVacations();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return vacationList;
    }

    public void insert(Vacation product) {
        databaseExecutor.execute(() -> {
            vacationDAO.insert(product);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateVacation(Vacation vacation) {
        databaseExecutor.execute(() -> {
            vacationDAO.update(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteVacation(Vacation vacation) {
        databaseExecutor.execute(() -> {
            vacationDAO.delete(vacation);
        });
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public void insertExcursion(Excursion excursion){
        databaseExecutor.execute(()-> {
            excursionDAO.insert(excursion);
        });
        try {
            Thread.sleep(1000);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public List<Excursion> getExcursionListByVacationId(int vacationId){
        databaseExecutor.execute(()->{
           excursionList = excursionDAO.getAllByVacationId(vacationId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return excursionList;
    }

    public void updateExcursion(Excursion excursion){
        databaseExecutor.execute(()-> {
            excursionDAO.update(excursion);
        });
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void delete(Excursion excursion){
        databaseExecutor.execute(()-> {
            excursionDAO.delete(excursion);
        });
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public Excursion getExcursionById(int excursionId){
        databaseExecutor.execute(()->{
            excursion = excursionDAO.getExcursionById(excursionId);
        });
        try {
            Thread.sleep(1000);
        }catch (InterruptedException ex){

        }
        return excursion;
    }

}
