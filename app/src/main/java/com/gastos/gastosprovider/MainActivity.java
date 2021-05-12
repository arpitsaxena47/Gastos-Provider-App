package com.gastos.gastosprovider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout dotsLL;
    SliderAdapter adapter;
    private ArrayList<SliderModal> sliderModalArrayList;
    private TextView[] dots;
    int size;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        viewPager = findViewById(R.id.idViewPager);
        dotsLL = findViewById(R.id.idLLDots);
        sliderModalArrayList = new ArrayList<>();
        sliderModalArrayList.add(new SliderModal(R.drawable.ic_app_new_logo, "Slide 1 heading", "Description 1"));
        sliderModalArrayList.add(new SliderModal(R.drawable.ic_launcher_background, "Slide 2 heading", "Description 2 "));
        sliderModalArrayList.add(new SliderModal(R.drawable.ic_launcher_background, "Slide 3 heading", "Description 3 "));
        // below line is use to add our array list to adapter class.
        adapter = new SliderAdapter(MainActivity.this, sliderModalArrayList);
        viewPager.setAdapter(adapter);
        size = sliderModalArrayList.size();
        addDots(size, 0);
        viewPager.addOnPageChangeListener(viewListner);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent i = new Intent(MainActivity.this, Enterpin_Activity.class);
            startActivity(i);
        }
    }

    private void addDots(int size, int pos) {
        dots = new TextView[size];

        dotsLL.removeAllViews();
        for (int i = 0; i < size; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("•"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.dots_unselected_color));
            dotsLL.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[pos].setTextColor(getResources().getColor(R.color.dots_selected_color));
        }
    }

    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(size, position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void OpenLogin(View view) {
        Intent i = new Intent(MainActivity.this, PhoneNumberActivity.class);
        startActivity(i);

    }
}