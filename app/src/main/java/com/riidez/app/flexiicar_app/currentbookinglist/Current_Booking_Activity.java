package com.riidez.app.flexiicar_app.currentbookinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.util.Log;

import com.riidez.app.R;

public class Current_Booking_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_booking);
        Bundle extras = getIntent().getExtras();
        getIntent().getExtras().getInt("currentbooking");
        Log.e("TAG", "onCreate: "+ getIntent().getExtras().getInt("currentbooking") );

        if (extras.getInt("currentbooking")>1){
            NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = hostFragment.getNavController();
            NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph_currentbooking);
            navGraph.setStartDestination(R.id.BookingList);
            navController.setGraph(navGraph);
        }
    }
}