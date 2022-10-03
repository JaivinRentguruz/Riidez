package com.riidez.app.fleetowner.registration;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.riidez.app.R;

import java.util.ArrayList;

public class Owner_Registration extends AppCompatActivity
{
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    ArrayList<String> scanData = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_fleetowner_registration);
            //0scanData = getIntent().getStringArrayListExtra("scanData");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
