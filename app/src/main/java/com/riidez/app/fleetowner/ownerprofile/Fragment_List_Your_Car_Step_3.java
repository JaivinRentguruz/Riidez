package com.riidez.app.fleetowner.ownerprofile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.riidez.app.R;

public class Fragment_List_Your_Car_Step_3 extends Fragment
{
    LinearLayout lblNext;
    ImageView Back;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_your_car_step_3, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        lblNext=view.findViewById(R.id.lbl_next3);
        Back=view.findViewById(R.id.back_to_Step2);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(Fragment_List_Your_Car_Step_3.this)
                        .navigate(R.id.action_ListYourCar_3_to_ListYourCar_2);
            }
        });
        lblNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle Location = new Bundle();
                Location.putBoolean("FromMap",false);
                System.out.println(Location);
                NavHostFragment.findNavController(Fragment_List_Your_Car_Step_3.this)
                        .navigate(R.id.action_ListYourCar_3_to_ListYourCar_4,Location);
            }
        });

    }
}
