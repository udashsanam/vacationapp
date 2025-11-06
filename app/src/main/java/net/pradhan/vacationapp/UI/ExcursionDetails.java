package net.pradhan.vacationapp.UI;

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

public class ExcursionDetails extends AppCompatActivity {

    TextView startDateText;
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
        String currentDate = new SimpleDateFormat("MM/dd/yy", Locale.getDefault())
                .format(Calendar.getInstance().getTime());
        startDateText = findViewById(R.id.startDateText);
        startDateText.setText(currentDate);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        // Handle back button click
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

    }
}