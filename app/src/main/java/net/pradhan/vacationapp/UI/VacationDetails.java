package net.pradhan.vacationapp.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.pradhan.vacationapp.R;
import net.pradhan.vacationapp.entities.Excursion;
import net.pradhan.vacationapp.entities.Vacation;
import net.pradhan.vacationapp.repository.Repository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {

    TextView startDateText, endDateText;

    EditText editTextTitle,editTextHotel;
    LinearLayout excursionListContainer;

    Repository repository;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        repository = new Repository(getApplication());
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        // handle buttn click
        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.endDateText);
        editTextTitle = findViewById(R.id.titletext);
        editTextHotel = findViewById(R.id.hotelText);
        // 🗓️ Set current date by default
        String currentDate = new SimpleDateFormat("MM/dd/yy", Locale.getDefault())
                .format(Calendar.getInstance().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1); // add 1 day

        String tomorrowDate = new SimpleDateFormat("MM/dd/yy", Locale.getDefault())
                .format(calendar.getTime());

        startDateText.setText(currentDate);
        endDateText.setText(tomorrowDate);

        startDateText.setOnClickListener(v -> showDatePicker(startDateText));
        endDateText.setOnClickListener(v -> showDatePicker(endDateText));

        String holidayName = getIntent().getStringExtra("title");
        int vacationId = getIntent().getIntExtra("vacationId", 0);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vacationId==0 ) {
                    Toast.makeText(VacationDetails.this, "Please save vacation details first.", Toast.LENGTH_SHORT).show();
                    return; // Stop here
                }
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacationId", vacationId);
                startActivity(intent);
            }
        });
        if(vacationId !=0){
            String hotel = getIntent().getStringExtra("hotel");
            String startDate = getIntent().getStringExtra("startDate");
            String endDate = getIntent().getStringExtra("endDate");
            editTextTitle.setText(holidayName);
            editTextHotel.setText(hotel);
            startDateText.setText(startDate);
            endDateText.setText(endDate);
        }
        List<Excursion> excursions = repository.getExcursionListByVacationId(vacationId);

        excursionListContainer = findViewById(R.id.excursionListContainer);


        for (Excursion excursion : excursions) {
            // Create a horizontal LinearLayout
            LinearLayout horizontalLayout = new LinearLayout(this);
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLayout.setPadding(0, 8, 0, 8);
            horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            // First TextView (place)
            TextView placeText = new TextView(this);
            placeText.setText(excursion.getTitle());
            placeText.setTextSize(16);
            placeText.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f // take remaining space
            ));

            // Second TextView (extra text)
            TextView extraText = new TextView(this);
            extraText.setText(String.valueOf(excursion.getDone()));
            extraText.setTextSize(14);
            extraText.setTextColor(Color.GRAY);
            extraText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            // Add click listener on the whole layout
            horizontalLayout.setOnClickListener(v -> {
                Intent intent = new Intent(this, ExcursionDetails.class);
                intent.putExtra("excursionId", excursion.getExcursionId());
                intent.putExtra("vacationId", vacationId);
                startActivity(intent);
            });

            // Add both TextViews to the horizontal layout
            horizontalLayout.addView(placeText);
            horizontalLayout.addView(extraText);

            // Add the horizontal layout to the container
            excursionListContainer.addView(horizontalLayout);
        }

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        // Handle back button click
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setOnMenuItemClickListener(item -> {
            if(R.id.saveVacation == item.getItemId()){

                    Vacation vacation = new Vacation();
                    vacation.setVacationId(vacationId);
                    vacation.setTitle(editTextTitle.getText().toString().trim());
                    vacation.setHotel(editTextHotel.getText().toString().trim());
                    vacation.setStartDate(startDateText.getText().toString().trim());
                    vacation.setEndDate(endDateText.getText().toString().trim());
                    if(vacationId ==0){
                        repository.insert(vacation);
                    }else {
                        repository.updateVacation(vacation);
                    }
                    Intent intent = new Intent(this, VacationList.class);
                    startActivity(intent);
                    finish(); // removes current screen from back stack


                return  true;
            }
            if(R.id.deleteVacation == item.getItemId()){
                if(vacationId!=0){
                    Vacation vacation = new Vacation();
                    vacation.setVacationId(vacationId);
                    repository.deleteVacation(vacation);
                    Intent intent = new Intent(this, VacationList.class);
                    startActivity(intent);
                    finish(); // removes current screen from back stack

                }
                return true;
            }
            return  false;

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        System.out.println(item);
        return super.onOptionsItemSelected(item);
    }
}