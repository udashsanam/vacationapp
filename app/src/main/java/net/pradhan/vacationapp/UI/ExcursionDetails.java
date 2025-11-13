package net.pradhan.vacationapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.pradhan.vacationapp.R;
import net.pradhan.vacationapp.entities.Excursion;
import net.pradhan.vacationapp.repository.Repository;

import java.text.SimpleDateFormat;
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
                return true;
            }
            return false;
        });

    }
}