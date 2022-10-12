package com.ralphiloutatoy.fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements
        HeadlineFragment.OnHeadlineSelectedListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onArticleSelected(int position) {
        NewsFragment newsFragment = (NewsFragment)
                getSupportFragmentManager().findFragmentById(R.id.news_fragment)
                ;
        newsFragment.updateArticleView(position);
    }
}