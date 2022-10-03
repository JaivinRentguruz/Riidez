package com.riidez.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.riidez.app.adapters.CustomToast;
import com.riidez.app.flexiicar_app.currentbookinglist.Current_Booking_Activity;
import com.riidez.app.flexiicar_app.login.OnBoarding;
import com.riidez.app.apicall.ApiService;
import com.riidez.app.apicall.OnResponseListener;
import com.riidez.app.apicall.RequestType;
import com.riidez.app.flexiicar_app.booking.Booking_Activity;
import com.riidez.app.flexiicar_app.ochs.OCHS_Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.riidez.app.apicall.ApiEndPoint.APP_INTIALIZATION;
import static com.riidez.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.riidez.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.riidez.app.apicall.ApiEndPoint.CURRENTBOOKING;

public class SplashScreen extends AppCompatActivity
{
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    public static Context context;
    public String id="";
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sp = getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");

        JSONObject bodyParam = new JSONObject();

        AndroidNetworking.initialize(getApplicationContext());
        SplashScreen.context = getApplicationContext();

        ApiService ApiService = new ApiService(appInitialize, RequestType.GET,
                APP_INTIALIZATION, BASE_URL_LOGIN,  new HashMap<String, String>(), bodyParam);
        //Loading = ProgressDialog.show(getApplicationContext(), "Welcome to FlexiiCar", "Please Wait...", true, true);

    }

    OnResponseListener appInitialize = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("status");

                        if (status)
                        {

                            JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                            JSONObject obj = resultSet.getJSONObject("appInitialization");
                            System.out.println(obj);

                            int cmp_ID=obj.getInt("cmp_ID");
                            String cmp_Name=obj.getString("cmp_Name");
                            String cmp_Street=obj.getString("cmp_Street");
                            String cmp_UnitNo=obj.getString("cmp_UnitNo");
                            String cmp_City=obj.getString("cmp_City");
                            int cmp_State=obj.getInt("cmp_State");
                            String state_Name=obj.getString("state_Name");
                            int cmp_Country=obj.getInt("cmp_Country");
                            String country_Name=obj.getString("country_Name");
                            String cmp_ZipCode=obj.getString("cmp_ZipCode");
                            String cmp_Domain=obj.getString("cmp_Domain");
                            String cmp_Logo=obj.getString("cmp_Logo");
                            String currency_Symbol=obj.getString("currency_Symbol");
                            String defaultSMSNo=obj.getString("defaultSMSNo");
                            String langCode=obj.getString("langCode");
                            int environment=obj.getInt("environment");
                            String cmP_PCONTNAME=obj.getString("cmP_PCONTNAME");
                            String cmP_PDESIG=obj.getString("cmP_PDESIG");
                            String cmP_PCONTNO=obj.getString("cmP_PCONTNO");
                            String cmP_PMOBILENO=obj.getString("cmP_PMOBILENO");
                            String cmP_PEMAIL=obj.getString("cmP_PEMAIL");
                            String cmP_SCONTNAME=obj.getString("cmP_SCONTNAME");
                            String cmP_SDESIG=obj.getString("cmP_SDESIG");
                           // String cmP_SCONTNO=obj.getString("cmP_SCONTNO");
                         //   String cmP_SMOBILENO=obj.getString("cmP_SMOBILENO");
                            String cmP_SEMAIL=obj.getString("cmP_SEMAIL");
                            String cmP_INCNO=obj.getString("cmP_INCNO");
                            String cmP_TAX_ID_NO=obj.getString("cmP_TAX_ID_NO");
                            String cmP_FYEAR=obj.getString("cmP_FYEAR");
                            String cmP_BNKNAME=obj.getString("cmP_BNKNAME");
                            String cmP_ACCOUNT_NO=obj.getString("cmP_ACCOUNT_NO");
                            String account_Type_Name=obj.getString("account_Type_Name");
                            Double cmP_CURRENCY_ID=obj.getDouble("cmP_CURRENCY_ID");
                            String cmP_CURRENCY_Name=obj.getString("cmP_CURRENCY_Name");
                            String cmP_LOCATION=obj.getString("cmP_LOCATION");
                            String cmP_WEBSITE=obj.getString("cmP_WEBSITE");
                            int cmP_DISTANCE=obj.getInt("cmP_DISTANCE");
                            int cmP_FUEL_MEASUREMENT=obj.getInt("cmP_FUEL_MEASUREMENT");
                            String serverPath=obj.getString("serverPath");
                            String colorPlatNo=obj.getString("colorPlatNo");
                            String cmP_DATE_FORMAT=obj.getString("cmP_DATE_FORMAT");
                            String domainName=obj.getString("domainName");
                            int vehicleClassWise=obj.getInt("vehicleClassWise");

                            SharedPreferences sp = getApplicationContext().getSharedPreferences("FlexiiCar",MODE_PRIVATE);
                            SharedPreferences.Editor editor= sp.edit();

                            editor.putInt("cmp_ID" ,cmp_ID);
                            editor.putString("cmp_Name" ,cmp_Name);
                            editor.putString("cmp_Street" ,cmp_Street);
                            editor.putString("cmp_UnitNo" ,cmp_UnitNo);
                            editor.putString("cmp_City" ,cmp_City);
                            editor.putInt("cmp_State" ,cmp_State);
                            editor.putString("state_Name" ,state_Name);
                            editor.putInt("cmp_Country" ,cmp_Country);
                            editor.putString("country_Name" ,country_Name);
                            editor.putString("cmp_ZipCode" ,cmp_ZipCode);
                            editor.putString("cmp_Domain" ,cmp_Domain);
                            editor.putString("cmp_Logo" ,cmp_Logo);
                            editor.putString("currency_Symbol" ,currency_Symbol);
                            editor.putString("defaultSMSNo" ,defaultSMSNo);
                            editor.putString("langCode" ,langCode);
                            editor.putInt("environment" ,environment);
                            editor.putString("cmP_PCONTNAME" ,cmP_PCONTNAME);
                            editor.putString("cmP_PDESIG" ,cmP_PDESIG);
                            editor.putString("cmP_PCONTNO" ,cmP_PCONTNO);
                            editor.putString("cmP_PMOBILENO" ,cmP_PMOBILENO);
                            editor.putString("cmP_PEMAIL" ,cmP_PEMAIL);
                            editor.putString("cmP_SCONTNAME" ,cmP_SCONTNAME);
                            editor.putString("cmP_SDESIG" ,cmP_SDESIG);
                       //     editor.putString("cmP_SCONTNO" ,cmP_SCONTNO);
                      //   editor.putString("cmP_SMOBILENO" ,cmP_SMOBILENO);
                            editor.putString("cmP_SEMAIL" ,cmP_SEMAIL);
                            editor.putString("cmP_INCNO" ,cmP_INCNO);
                            editor.putString("cmP_TAX_ID_NO" ,cmP_TAX_ID_NO);
                            editor.putString("cmP_FYEAR" ,cmP_FYEAR);
                            editor.putString("cmP_BNKNAME" ,cmP_BNKNAME);
                            editor.putString("cmP_ACCOUNT_NO" ,cmP_ACCOUNT_NO);
                            editor.putString("account_Type_Name" ,account_Type_Name);
                            editor.putString("cmP_CURRENCY_Name" ,cmP_CURRENCY_Name);
                            editor.putString("cmP_LOCATION" ,cmP_LOCATION);
                            editor.putString("cmP_WEBSITE" ,cmP_WEBSITE);
                            editor.putInt("cmP_DISTANCE" ,cmP_DISTANCE);
                            editor.putInt("cmP_FUEL_MEASUREMENT" ,cmP_FUEL_MEASUREMENT);
                            editor.putString("domainName" ,domainName);
                            editor.putString("serverPath" ,serverPath);
                            editor.putString("colorPlatNo" ,colorPlatNo);
                            editor.putString("cmP_DATE_FORMAT",cmP_DATE_FORMAT);

                            editor.commit();

                            if(id.equals(""))
                            {
                                Intent i = new Intent(SplashScreen.this, OnBoarding.class);
                                startActivity(i);
                            }
                            else
                            {
                                System.out.println("Error"+id);
                               /* JSONObject bodyParam = new JSONObject();
                                bodyParam.accumulate("customerId=", id);*/
                                String bodyParam = "customerId="+ id;
                                ApiService apiService = new ApiService(onCurrentBooking, RequestType.GET,CURRENTBOOKING,BASE_URL_CUSTOMER, new HashMap<String, String>(),bodyParam);

                               /* Intent i = new Intent(SplashScreen.this, Booking_Activity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                              *//*  Bundle bndlanimation1 =  ActivityOptions.makeCustomAnimation(getApplicationContext(),
                                        R.anim.rotate, R.anim.rotate).toBundle();
                                startActivity(i, bndlanimation1);*//*
                                startActivity(i);*/
                            }

                        }
                        else
                        {
                            String errorString = responseJSON.getString("resultSet");
                            CustomToast.showToast(SplashScreen.this,errorString,1);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        @Override
        public void onError(String error)
        {
            System.out.println("Error"+error);
        }
    };

    OnResponseListener onCurrentBooking = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("Success");
                        System.out.println(response);
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("status");

                        if (status){
                            JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                            JSONArray array = resultSet.getJSONArray("v0200_Agreement_Details");

                            Intent i = new Intent(SplashScreen.this, Current_Booking_Activity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.putExtra("currentbooking", array.length());
                            startActivity(i);
                        } else {
                            Intent i = new Intent(SplashScreen.this, Booking_Activity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }



                    }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                }
            });
        }

        @Override
        public void onError(String error) {
            System.out.println("Error"+error);
        }
    };

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
    }
}



