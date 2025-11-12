package net.pradhan.vacationapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import net.pradhan.vacationapp.entities.Vacation;
import net.pradhan.vacationapp.repository.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VacationList extends AppCompatActivity {

    LinearLayout vacationListContainer;
    Repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        // Handle back button click
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        vacationListContainer = findViewById(R.id.vacationListContainer);
        repository = new Repository(getApplication());
        // Example dynamic vacation list
//        List<String> vacations = Arrays.asList("Miami", "Denver", "Las Vegas", "New York", "Hawaii", "Denver", "Las Vegas", "New York", "Hawaii", "Denver", "Las Vegas", "New York", "Hawaii"
//                , "Denver", "Las Vegas", "New York", "Hawaii", "Denver", "Las Vegas", "New York", "Hawaii", "Denver", "Las Vegas", "New York", "Hawaii", "Denver", "Las Vegas", "New York", "Hawaii", "Denver", "Las Vegas", "New York", "Hawaii", "Denver", "Las Vegas", "New York", "Hawaii");
       List<Vacation> vacations = repository.getVacationList();

        for (Vacation place : vacations) {
            TextView textView = new TextView(this);
            textView.setText(place.getTitle());
            textView.setTextSize(16);
            textView.setPadding(0, 8, 0, 8);
            textView.setOnClickListener(v -> {
                Intent intent = new Intent(this, VacationDetails.class);
                intent.putExtra("vacationId", place.getVacationId());
                intent.putExtra("title", place.getTitle());
                intent.putExtra("hotel", place.getHotel());
                intent.putExtra("startDate", place.getStartDate());
                intent.putExtra("endDate", place.getEndDate());
                startActivity(intent);
            });
            vacationListContainer.addView(textView);
        }
        System.out.println(getIntent().getStringExtra("test"));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.saveExcursion) {
            Toast.makeText(VacationList.this, "put in sample data", Toast.LENGTH_LONG).show();
            return true;
        }
        if(item.getItemId()==android.R.id.home) {
//            this.finish();
            Intent intent = new Intent(VacationList.this, VacationDetails.class); //takes your across pages
            startActivity(intent);
            return true;
        }
        return true;
    }
}