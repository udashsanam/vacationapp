package net.pradhan.vacationapp.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VacationDetails extends AppCompatActivity {

    TextView startDateText, endDateText;
    LinearLayout excursionListContainer;


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

        String holidayName = getIntent().getStringExtra("Detail");
        System.out.println("fromintext" + holidayName);
        Map<String, String> excursions = Map.of("Swimming","1", "Cycling", "0");

        excursionListContainer = findViewById(R.id.excursionListContainer);
        // Example dynamic vacation list
        List<String> vacations = Arrays.asList("Miami", "Denver", "Las Vegas", "New York", "Hawaii");

        for (String excursion : excursions.keySet()) {
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
            placeText.setText(excursion);
            placeText.setTextSize(16);
            placeText.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f // take remaining space
            ));

            // Second TextView (extra text)
            TextView extraText = new TextView(this);
            extraText.setText(excursions.get(excursion));
            extraText.setTextSize(14);
            extraText.setTextColor(Color.GRAY);
            extraText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            // Add click listener on the whole layout
            horizontalLayout.setOnClickListener(v -> {
                Intent intent = new Intent(this, ExcursionDetails.class);
                intent.putExtra("excursion", excursion);
                startActivity(intent);
            });

            // Add both TextViews to the horizontal layout
            horizontalLayout.addView(placeText);
            horizontalLayout.addView(extraText);

            // Add the horizontal layout to the container
            excursionListContainer.addView(horizontalLayout);
        }

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