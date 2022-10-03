package com.riidez.app.flexiicar_app.currentbookinglist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.riidez.app.R;
import com.riidez.app.flexiicar_app.booking.Booking_Activity;
import com.riidez.app.flexiicar_app.user.User_Profile;

public class Fragment_Location_And_Key extends Fragment {

    TextView Done,txtVehiclaName;
    ImageView imgback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_and_key, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        imgback=view.findViewById(R.id.Back);
        txtVehiclaName=view.findViewById(R.id.txtVehiclaName);

        /*txtVehiclaName.setText("Test");*/

        imgback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (getArguments().getInt("test")==1){
                    NavHostFragment.findNavController(Fragment_Location_And_Key.this).popBackStack();
                } else {
                    Intent i = new Intent(getContext(), Booking_Activity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });

        Done=view.findViewById(R.id.lblDone);
        Done.setText("Home");
        Done.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getContext(), Booking_Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });

    }

}
