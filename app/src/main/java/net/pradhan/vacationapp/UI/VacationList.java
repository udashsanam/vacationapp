package net.pradhan.vacationapp.UI;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.pradhan.vacationapp.R;
import net.pradhan.vacationapp.entities.Excursion;
import net.pradhan.vacationapp.entities.Vacation;
import net.pradhan.vacationapp.repository.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VacationList extends AppCompatActivity {

    LinearLayout vacationListContainer;
    Repository repository;

    List<Vacation> vacations;
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
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.action_search){
                openSearchDialog();
            }
            if(item.getItemId() == R.id.report){
                generatePdf(this, vacations);
            }
            return false;
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });


        // Handle back button click
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        vacationListContainer = findViewById(R.id.vacationListContainer);
        repository = new Repository(getApplication());
        // Example dynamic vacation list
        String search = getIntent().getStringExtra("search");

        vacations = repository.getVacationList();
        displayVacations(vacations);

        System.out.println(getIntent().getStringExtra("test"));
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
//        return true;
//    }
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

    private void openSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search Excursion");

        final EditText input = new EditText(this);
        input.setHint("Type to search...");
        builder.setView(input);

        builder.setPositiveButton("Search", (dialog, which) -> {
            String query = input.getText().toString();
            this.vacations =this.repository.searchVacation(query);
            this.displayVacations(this.vacations);

        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    private void displayVacations(List<Vacation> list) {
        vacationListContainer.removeAllViews(); // 🔥 clear old list

        for (Vacation place : list) {
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
    }


    public void generatePdf(Context context, List<Vacation> vacations) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        titlePaint.setTextSize(18f);
        titlePaint.setFakeBoldText(true);
        paint.setTextSize(14f);

        int pageWidth = 595; // A4 width in points
        int pageHeight = 842; // A4 height in points
        int margin = 40;
        int y = margin;

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Title
        canvas.drawText("Vacation & Excursion Report", margin, y, titlePaint);
        y += 30;
        canvas.drawText("Date: " + new Date().toString(), margin, y, paint);
        y += 40;

        // Table Header
        paint.setFakeBoldText(true);
        canvas.drawText("No.", margin, y, paint);
        canvas.drawText("Title", margin + 50, y, paint);
        canvas.drawText("Start Date", margin + 200, y, paint);
        canvas.drawText("End Date", margin + 300, y, paint);
        canvas.drawText("Hotel", margin + 400, y, paint);
        y += 20;
        paint.setFakeBoldText(false);

        int vacRowNum = 1;
        for (Vacation vac : vacations) {
            // Draw vacation row with row number
            canvas.drawText(String.valueOf(vacRowNum), margin, y, paint);
            canvas.drawText(vac.getTitle(), margin + 50, y, paint);
            canvas.drawText(vac.getStartDate(), margin + 200, y, paint);
            canvas.drawText(vac.getEndDate(), margin + 300, y, paint);
            canvas.drawText(vac.getHotel(), margin + 400, y, paint);
            y += 20;

            // Draw excursions for this vacation
            List<Excursion> excursions = this.repository.getExcursionListByVacationId(vac.getVacationId());
            if (excursions != null && !excursions.isEmpty()) {
                // Excursion header
                paint.setFakeBoldText(true);
                canvas.drawText("  No.", margin + 20, y, paint);
                canvas.drawText("Title", margin + 70, y, paint);
                canvas.drawText("Start Date", margin + 200, y, paint);
                canvas.drawText("Done", margin + 300, y, paint);
                paint.setFakeBoldText(false);
                y += 20;

                int exRowNum = 1;
                for (Excursion ex : excursions) {
                    canvas.drawText("  " + exRowNum, margin + 20, y, paint);
                    canvas.drawText(ex.getTitle(), margin + 70, y, paint);
                    canvas.drawText(ex.getStartDate(), margin + 200, y, paint);
                    canvas.drawText(ex.getDone() == 1 ? "Yes" : "No", margin + 300, y, paint);
                    y += 20;
                    exRowNum++;
                }
            }

            // Add spacing between vacations
            y += 10;
            vacRowNum++;

            // New page if overflow
            if (y > pageHeight - 50) {
                pdfDocument.finishPage(page);
                pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pdfDocument.getPages().size() + 1).create();
                page = pdfDocument.startPage(pageInfo);
                canvas = page.getCanvas();
                y = margin;
            }
        }

        pdfDocument.finishPage(page);

        // Save file
        File file = new File(context.getExternalFilesDir(null), "Vacation_Report_" + new Date().getTime() + ".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "PDF saved: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error generating PDF", Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();

        openPdf(context, file);
    }



    public void openPdf(Context context, File file) {
        Uri uri = FileProvider.getUriForFile(
                context,
                context.getPackageName() + ".provider",
                file
        );

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No PDF viewer app found", Toast.LENGTH_SHORT).show();
        }
    }




}