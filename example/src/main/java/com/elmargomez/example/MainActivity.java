package com.elmargomez.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.elmargomez.fablayout.widget.FabLayout;

public class MainActivity extends AppCompatActivity {

    private FabLayout fabLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fabLayout = (FabLayout) findViewById(R.id.fblyout_id);
    }

    public void click(View v) {
        fabLayout.showView();
    }

}
