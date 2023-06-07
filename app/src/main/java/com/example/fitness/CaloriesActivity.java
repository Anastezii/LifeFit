package com.example.fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class CaloriesActivity extends AppCompatActivity {

    private Button food,sport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories);

        food=findViewById(R.id.buttonBuyFood);
        sport=findViewById(R.id.buttonBuySport);

        TextView textView = findViewById(R.id.ChloeTing);
        TextView txtBrad=findViewById(R.id.textViewBrad);
        TextView txtMatt=findViewById(R.id.textViewMatt);
        TextView txtEmma=findViewById(R.id.textViewEmma);
        TextView txtGymHelp=findViewById(R.id.textViewgymHelp);

        // Set the URL and label
        String url = "https://chloeting.com/?_x_tr_hist=true";
        String label = "Visit Chloe Ting`s website";

        String urlBrad = "https://www.lookgreatnaked.com/index.php";
        String labelBrad="Visit Brad`s website";

        String urlMatt="https://mattroberts.co.uk/";
        String labelMatt="Visit Matt`s website";

        String urlEmma="https://www.coastalfitnesshk.com/coastal-fitness/meet-the-team/emma-chan/";
        String labelEmma="Visit Emma`s website";

        String gymHelp="https://quiz.betterme.world/first-page-generated?flow=1671";
        String labelGymHelp="Visit website for help in gym";

        // Create a SpannableString with the label as a clickable link
        SpannableString spannableString = new SpannableString(label);
        SpannableString spannableStringBrad = new SpannableString(labelBrad);
        SpannableString spannableStringMatt = new SpannableString(labelMatt);
        SpannableString spannableStringEmma = new SpannableString(labelEmma);
        SpannableString spannableStringGym = new SpannableString(labelGymHelp);


        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Open the URL when the link is clicked
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

                Intent intentBrad = new Intent(Intent.ACTION_VIEW, Uri.parse(urlBrad));
                startActivity(intentBrad);

                Intent intentMatt = new Intent(Intent.ACTION_VIEW, Uri.parse(urlMatt));
                startActivity(intentMatt);

                Intent intentEmma = new Intent(Intent.ACTION_VIEW, Uri.parse(urlEmma));
                startActivity(intentEmma);

                Intent intentGym = new Intent(Intent.ACTION_VIEW, Uri.parse(gymHelp));
                startActivity(intentGym);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                // Set the link color and style
                ds.setColor(ContextCompat.getColor(getApplicationContext(), R.color.aqua));
                ds.setUnderlineText(true);
            }
        };
        spannableString.setSpan(clickableSpan, 0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBrad.setSpan(clickableSpan, 0, labelBrad.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringMatt.setSpan(clickableSpan, 0, labelMatt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringEmma.setSpan(clickableSpan, 0, labelEmma.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringGym.setSpan(clickableSpan, 0, labelGymHelp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the SpannableString in the TextView
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        txtBrad.setText(spannableStringBrad);
        txtBrad.setMovementMethod(LinkMovementMethod.getInstance());

        txtMatt.setText(spannableStringMatt);
        txtMatt.setMovementMethod(LinkMovementMethod.getInstance());

        txtEmma.setText(spannableStringEmma);
        txtEmma.setMovementMethod(LinkMovementMethod.getInstance());

        txtGymHelp.setText(spannableStringGym);
        txtGymHelp.setMovementMethod(LinkMovementMethod.getInstance());


        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_home:
                        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        return;
                    case R.id.bottom_activity:
                        startActivity(new Intent(getApplicationContext(), SportActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        return;
                    case R.id.bottom_eat:
                        startActivity(new Intent(getApplicationContext(), FoodActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        return;
                    case R.id.bottom_calories:
                        return;
                }
                return;
            }
        });

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CaloriesActivity.this, BuyFoodActivity.class);
                startActivity(intent);
            }
        });

        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CaloriesActivity.this, BuySportActivity.class);
                startActivity(intent);
            }
        });



    }
}