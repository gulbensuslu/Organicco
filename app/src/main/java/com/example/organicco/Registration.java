package com.example.organicco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Registration extends AppCompatActivity {
    public static FragmentManager FM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        FM = getSupportFragmentManager();
        if(findViewById(R.id.mainFragment_id)!=null){
            if(savedInstanceState!=null){
                return;
            }
            FragmentTransaction FT= FM.beginTransaction();
            FT.add(R.id.mainFragment_id,new Login_fragment(),null);
            FT.commit();

        }
    }
}