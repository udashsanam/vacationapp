package net.pradhan.vacationapp.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.pradhan.vacationapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {

    TextView startDateText, endDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                startActivity(intent);
            }
        });
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        // Handle back button click
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.endDateText);
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