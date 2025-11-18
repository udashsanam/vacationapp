package net.pradhan.vacationapp.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.pradhan.vacationapp.R;
import net.pradhan.vacationapp.entities.Excursion;
import net.pradhan.vacationapp.notification.Schedular;
import net.pradhan.vacationapp.repository.Repository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    TextView startDateText;
    EditText titleText;

    Repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        repository = new Repository(getApplication());

        String currentDate = new SimpleDateFormat("MM/dd/yy", Locale.getDefault())
                .format(Calendar.getInstance().getTime());
        startDateText = findViewById(R.id.startDateText);
        startDateText.setOnClickListener(v -> showDatePicker(startDateText));
        titleText = findViewById(R.id.titleText);
        startDateText.setText(currentDate);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        int excursionId = getIntent().getIntExtra("excursionId", 0);
        int vacationId = getIntent().getIntExtra("vacationId", 0);

        if(excursionId !=0){
            Excursion excursion = repository.getExcursionById(excursionId);
            startDateText.setText(excursion.getStartDate());
            titleText.setText(excursion.getTitle());
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.saveExcursion){
                Excursion excursion = new Excursion();
                excursion.setExcursionId(excursionId);
                excursion.setTitle(titleText.getText().toString().trim());
                excursion.setStartDate(startDateText.getText().toString().trim());
                excursion.setVacationId(vacationId);
                if(excursionId ==0){
                    repository.insertExcursion(excursion);
                }else {
                    repository.updateExcursion(excursion);
                }
                Intent intent = new Intent(this, VacationDetails.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("vacationId", vacationId);
                startActivity(intent);
                finish(); // removes current screen from back stack
                return true;
            }
            if(item.getItemId() == R.id.deleteExcursion){
                if(excursionId !=0){
                    Excursion excursion = new Excursion();
                    excursion.setExcursionId(excursionId);
                    repository.delete(excursion);
                }
                Intent intent = new Intent(this, VacationDetails.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("vacationId", vacationId);
                startActivity(intent);
                finish();
                return true;
            }
            if(item.getItemId() == R.id.alertExcursion){
                if(excursionId ==0){
                    Toast.makeText(ExcursionDetails.this, "Please save excursion details first.", Toast.LENGTH_SHORT).show();
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
                LocalDate date = LocalDate.parse(startDateText.getText().toString(), formatter);

                int month = date.getMonthValue();
                int day = date.getDayOfMonth();
                int year = date.getYear();

                Schedular schedular = new Schedular();
                schedular.scheduleToast(getApplicationContext(),month,year,day, titleText.getText().toString() + " excursion started!!");
            }
            return false;
        });

    }
    private void showDatePicker(TextView targetView) {
        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = String.format("%02d/%02d/%02d",
                            selectedMonth + 1, selectedDay, selectedYear % 100);
                    targetView.setText(formattedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}