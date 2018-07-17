package com.staticscreen.shishir.staticscreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * This class is responsible to handle the button clicks for drop down and populate horizontal and vertical scroll of
 * Best Seller component. We also introduced an auto scroll for banner.
 * The coding style used is normal android default as most of the component are static and in xml only
 */

public class MainActivity extends Activity implements View.OnClickListener {

    private final static int VISIBLE_COUNT_BANNER = 2;
    private final static int TIME_DELAY_SEC = 1000;

    private ImageView dropDownBestSeller;
    private ImageView dropDownLips;
    private ImageView dropDownNails;
    private ImageView dropDownFace;

    private LinearLayout layoutBest;
    private LinearLayout layoutLips;
    private LinearLayout layoutNails;
    private LinearLayout layoutFace;

    private LinearLayout mainHorizontalChildLayout;
    private LinearLayout mainHorizontalChildLayoutOne;
    private LinearLayout mainHorizontalChildLayoutTwo;

    private HorizontalScrollView bestSellerHorizontalScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        bestSellerHorizontalScroll = (HorizontalScrollView) findViewById(R.id.scroll_view_banner);
        NestedScrollView bestSellerVerticalScroll = (NestedScrollView) findViewById(R.id.scrollview_bestseller);
        bestSellerVerticalScroll.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, getScreenHeight() / 4));

        dropDownBestSeller = (ImageView) findViewById(R.id.image_one);
        dropDownLips = (ImageView) findViewById(R.id.image_two);
        dropDownNails = (ImageView) findViewById(R.id.image_three);
        dropDownFace = (ImageView) findViewById(R.id.image_four);

        dropDownFace.setOnClickListener(this);
        dropDownLips.setOnClickListener(this);
        dropDownNails.setOnClickListener(this);
        dropDownBestSeller.setOnClickListener(this);

        dropDownBestSeller.setTag(android.R.drawable.arrow_down_float);
        dropDownLips.setTag(android.R.drawable.arrow_down_float);
        dropDownNails.setTag(android.R.drawable.arrow_down_float);
        dropDownFace.setTag(android.R.drawable.arrow_down_float);

        layoutBest = (LinearLayout) findViewById(R.id.layout_best);
        layoutLips = (LinearLayout) findViewById(R.id.layout_lips);
        layoutNails = (LinearLayout) findViewById(R.id.layout_nails);
        layoutFace = (LinearLayout) findViewById(R.id.layout_face);

        mainHorizontalChildLayout = (LinearLayout) findViewById(R.id.layout_horizontal_scroll_child);
        mainHorizontalChildLayoutOne = (LinearLayout) findViewById(R.id.layout_horizontal_scroll_child_one);
        mainHorizontalChildLayoutTwo = (LinearLayout) findViewById(R.id.layout_horizontal_scroll_child_two);

        // To fill horizontal and vertical scroll view for bestseller
        populateBestSellers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoScroll();
    }

    // Animation for banner
    private void autoScroll() {
        final Handler handler = new Handler();

        final int[] factor = {mainHorizontalChildLayout.getChildAt(0).getMeasuredWidth()};
        final int[] maxScroll = {(mainHorizontalChildLayout.getChildCount() - VISIBLE_COUNT_BANNER)
                * mainHorizontalChildLayout.getChildAt(0).getMeasuredWidth()};

        final int singleChildSize = mainHorizontalChildLayout.getChildAt(0).getMeasuredWidth();
        final int totalChildCount = mainHorizontalChildLayout.getChildCount();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // To scroll the banner
                bestSellerHorizontalScroll.smoothScrollTo(
                        bestSellerHorizontalScroll.getScrollX() + factor[0],
                        bestSellerHorizontalScroll.getScrollY());

                int currentPosition = bestSellerHorizontalScroll.getScrollX();

                if (currentPosition > maxScroll[0]) {

                    // To scroll banner in left direction
                    factor[0] = -singleChildSize;

                    // Starting position of banner
                    maxScroll[0] = 0;
                } else {

                    // To scroll banner in right direction
                    factor[0] = singleChildSize;
                    maxScroll[0] = (totalChildCount - VISIBLE_COUNT_BANNER) * singleChildSize;
                }
                handler.postDelayed(this, TIME_DELAY_SEC);
            }
        }, TIME_DELAY_SEC);

    }

    private void populateBestSellers() {
        final int maxPosition = 3;

        for (int k = 0; k < maxPosition; k++) {
            LinearLayout mainBestSellerLayout;
            if (k == 0) {
                mainBestSellerLayout = mainHorizontalChildLayout;
            } else if (k == 1) {
                mainBestSellerLayout = mainHorizontalChildLayoutOne;
            } else {
                mainBestSellerLayout = mainHorizontalChildLayoutTwo;
            }

            for (int i = 0; i < maxPosition; i++) {
                @SuppressLint("InflateParams")
                View view = LayoutInflater.from(this).inflate(R.layout.layout_child_bestseller, null);
                RelativeLayout bestSellersChildLayout = (RelativeLayout)
                        view.findViewById(R.id.layout_bestsell);

                // Calculate width to occupy full space of parent layout
                int screenWidth = (getScreenWidth() * 7) / 10;

                bestSellersChildLayout.setLayoutParams(
                        new RelativeLayout.LayoutParams(screenWidth, RelativeLayout.LayoutParams.MATCH_PARENT));
                mainBestSellerLayout.addView(view);
            }
        }
    }

    private int getScreenWidth() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    private int getScreenHeight() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    @Override
    public void onClick(View v) {

        // To handle open and close of drop drown
        int tag = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.image_one:
                if (tag == android.R.drawable.arrow_down_float) {
                    dropDownBestSeller.setImageResource(android.R.drawable.arrow_up_float);
                    dropDownBestSeller.setTag(android.R.drawable.arrow_up_float);
                    layoutBest.setVisibility(View.GONE);
                } else {
                    dropDownBestSeller.setImageResource(android.R.drawable.arrow_down_float);
                    dropDownBestSeller.setTag(android.R.drawable.arrow_down_float);
                    layoutBest.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.image_two:
                if (tag == android.R.drawable.arrow_down_float) {
                    dropDownLips.setImageResource(android.R.drawable.arrow_up_float);
                    dropDownLips.setTag(android.R.drawable.arrow_up_float);
                    layoutLips.setVisibility(View.GONE);
                } else {
                    dropDownLips.setImageResource(android.R.drawable.arrow_down_float);
                    dropDownLips.setTag(android.R.drawable.arrow_down_float);
                    layoutLips.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.image_three:
                if (tag == android.R.drawable.arrow_down_float) {
                    dropDownNails.setImageResource(android.R.drawable.arrow_up_float);
                    dropDownNails.setTag(android.R.drawable.arrow_up_float);
                    layoutNails.setVisibility(View.GONE);
                } else {
                    dropDownNails.setImageResource(android.R.drawable.arrow_down_float);
                    dropDownNails.setTag(android.R.drawable.arrow_down_float);
                    layoutNails.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.image_four:
                if (tag == android.R.drawable.arrow_down_float) {
                    dropDownFace.setImageResource(android.R.drawable.arrow_up_float);
                    dropDownFace.setTag(android.R.drawable.arrow_up_float);
                    layoutFace.setVisibility(View.GONE);
                } else {
                    dropDownFace.setImageResource(android.R.drawable.arrow_down_float);
                    dropDownFace.setTag(android.R.drawable.arrow_down_float);
                    layoutFace.setVisibility(View.VISIBLE);
                }

                break;
        }
    }
}
