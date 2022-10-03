package com.riidez.app.flexiicar_app.selfcheckout;

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
import com.riidez.app.flexiicar_app.user.User_Profile;

public class Fragment_Location_And_Key extends Fragment
{
    TextView lblDone;
    ImageView imgback;
    Bundle AgreementsBundle;

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
        ((User_Profile) getActivity()).BottomnavInVisible();

        imgback=view.findViewById(R.id.Back);
        AgreementsBundle= getArguments().getBundle("AgreementsBundle");

        imgback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle Agreements = new Bundle();
                Agreements.putBundle("AgreementsBundle", AgreementsBundle);
                NavHostFragment.findNavController(Fragment_Location_And_Key.this)
                        .navigate(R.id.action_Location_And_Key_to_Waiver_Signature,Agreements);
            }
        });

        lblDone=view.findViewById(R.id.lblDone);

        lblDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle Agreements = new Bundle();
                Agreements.putBundle("AgreementsBundle", AgreementsBundle);
                NavHostFragment.findNavController(Fragment_Location_And_Key.this)
                        .navigate(R.id.action_Location_And_Key_to_SummaryOfChargesForAgreements,Agreements);
            }
        });

    }
}

