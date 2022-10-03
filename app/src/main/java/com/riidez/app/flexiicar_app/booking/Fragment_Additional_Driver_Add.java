package com.riidez.app.flexiicar_app.booking;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.riidez.app.R;
import com.riidez.app.ScanDrivingLicense;
import com.riidez.app.adapters.CustomToast;
import com.riidez.app.apicall.ApiService;
import com.riidez.app.apicall.OnResponseListener;
import com.riidez.app.apicall.RequestType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.riidez.app.apicall.ApiEndPoint.ADD_DRIVER;
import static com.riidez.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;

public class Fragment_Additional_Driver_Add extends Fragment
{
    EditText edt_DriverName,edt_LicenseNo,edt_IssueBy,edt_RelationShip,edt_Email,edt_Telephone;
    TextView lbl_Save,lblexDate,lblissuedate;
    Handler handler = new Handler();
    public String id="";
    ImageView Image_Back;
    DatePickerDialog datePickerDialog;
    ImageView img_DLFronside,img_DLBackside;
    private static int RESULT_LOAD_IMAGE = 1;
    String imagestr;
    String imgId = "";
    JSONArray ImageList = new JSONArray();
    Bundle BookingBundle, VehicleBundle;
    Bundle returnLocationBundle, locationBundle;
    Boolean locationType, initialSelect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driving_license_add_update, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        try {
            super.onViewCreated(view, savedInstanceState);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            id = sp.getString(getString(R.string.id), "");

            BookingBundle = getArguments().getBundle("BookingBundle");
            VehicleBundle = getArguments().getBundle("VehicleBundle");
            returnLocationBundle = getArguments().getBundle("returnLocation");
            locationBundle = getArguments().getBundle("location");
            locationType = getArguments().getBoolean("locationType");
            initialSelect = getArguments().getBoolean("initialSelect");

            edt_DriverName = view.findViewById(R.id.edt_driverName);
            edt_LicenseNo = view.findViewById(R.id.edt_LicenseNo);
            lblexDate = view.findViewById(R.id.lblexDate);
            edt_IssueBy = view.findViewById(R.id.edt_issueBy);
            lblissuedate = view.findViewById(R.id.lblissuedate);
            edt_RelationShip = view.findViewById(R.id.edt_Relationship);
            edt_Email = view.findViewById(R.id.edt_DriverEmail);
            edt_Telephone = view.findViewById(R.id.edt_driverTelephone);
            lbl_Save = view.findViewById(R.id.lbl_Save);
            Image_Back= view.findViewById(R.id.Image_BackDL);

            img_DLBackside = view.findViewById(R.id.img_DLBackside);
            img_DLFronside = view.findViewById(R.id.img_DLFronside);

            img_DLFronside.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    imgId = "2";
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                }
            });

            img_DLBackside.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    imgId = "3";
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                }
            });

            Image_Back.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle Booking = new Bundle();
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);
                    System.out.println(Booking);
                    NavHostFragment.findNavController(Fragment_Additional_Driver_Add.this)
                            .navigate(R.id.action_Additional_Driver_Add_to_Additional_Driver_List,Booking);
                }
            });

            ImageView imgScanDrivingLicense = view.findViewById(R.id.imgScanDrivingLicense);
            imgScanDrivingLicense.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    Intent i = new Intent(getActivity(), ScanDrivingLicense.class);
                    i.putExtra("afterScanBackTo", 3);
                    startActivity(i);

                }
            });

            lblexDate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try {
                        // calender class's instance and get current date , month and year from calender
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR); // current year
                        int mMonth = c.get(Calendar.MONTH); // current month
                        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                        // date picker dialog
                        datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme,
                                new DatePickerDialog.OnDateSetListener()
                                {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                    {
                                        String month, day;
                                        if (monthOfYear < 9)
                                            month = "0" + (monthOfYear + 1);
                                        else
                                            month = (monthOfYear + 1) + "";

                                        if (dayOfMonth < 10)
                                            day = "0" + dayOfMonth;
                                        else
                                            day = dayOfMonth + "";

                                        lblexDate.setText(year + "-" + month + "-" + day);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            lblissuedate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try {
                        // calender class's instance and get current date , month and year from calender
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR); // current year
                        int mMonth = c.get(Calendar.MONTH); // current month
                        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                        // date picker dialog
                        datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                    {
                                        String month, day;
                                        if (monthOfYear < 9)
                                            month = "0" + (monthOfYear + 1);
                                        else
                                            month = (monthOfYear + 1) + "";

                                        if (dayOfMonth < 10)
                                            day = "0" + dayOfMonth;
                                        else
                                            day = dayOfMonth + "";

                                        lblissuedate.setText(year + "-" + month + "-" + day);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()+ (1000 * 60 * 60 * 24 * 0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            lbl_Save.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (edt_DriverName.getText().toString().equals("")) {
                        CustomToast.showToast(getActivity(), "Please Enter Your Name", 1);
                    }
                    else if (edt_LicenseNo.getText().toString().equals("")) {
                        CustomToast.showToast(getActivity(), "Please Enter License No", 1);
                    }
                    else if (lblexDate.getText().toString().equals("")) {
                        CustomToast.showToast(getActivity(), "Please Enter Expiry Date", 1);
                    }
                    else if (lblissuedate.getText().toString().equals("")) {
                        CustomToast.showToast(getActivity(), "Please Enter Issue Date", 1);
                    }
                    else if (edt_Email.getText().toString().equals("")) {
                        CustomToast.showToast(getActivity(), "Please Enter Email", 1);
                    }
                    else if (!Patterns.EMAIL_ADDRESS.matcher(edt_Email.getText().toString()).matches())
                    {
                        edt_Email.setError("Enter valid Email address");
                        edt_Email.requestFocus();
                    }
                    else if (edt_Telephone.getText().toString().equals("")) {
                        CustomToast.showToast(getActivity(), "Please Enter Your Telephone No.", 1);
                    }
                    else {
                        JSONObject bodyParam = new JSONObject();
                        try {
                            bodyParam.accumulate("CustomerID", Integer.parseInt(id));
                            bodyParam.accumulate("LoginID", Integer.parseInt(id));
                            bodyParam.accumulate("Driver_Name", edt_DriverName.getText().toString());
                            bodyParam.accumulate("License_Num", edt_LicenseNo.getText().toString());
                            bodyParam.accumulate("License_Expiry", lblexDate.getText().toString());
                            bodyParam.accumulate("DriverEmail", edt_Email.getText().toString());
                            bodyParam.accumulate("Phone_Num", edt_Telephone.getText().toString());

                            // bodyParam.accumulate("LIssue_Date", lblissuedate.getText().toString());
                           // bodyParam.accumulate("LIssue_By", edt_IssueBy.getText().toString());
                           // bodyParam.accumulate("DriverRelationship", edt_RelationShip.getText().toString());
                            if(ImageList!=null)
                            {
                                bodyParam.accumulate("ImageList", ImageList);
                            }
                            System.out.println(bodyParam);
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        ApiService ApiService = new ApiService(AddDriver, RequestType.POST,
                                ADD_DRIVER, BASE_URL_CUSTOMER, new HashMap<String, String>(), bodyParam);
                    }
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data)
        {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap bitmap = null;
                Uri targetUri = data.getData();
                try
                {
                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                    imagestr = ConvertBitmapToString(resizedBitmap);
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }

                try {
                    bitmap = getScaledBitmap(selectedImage,500,500);
                    Bitmap temp = bitmap;
                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
                    temp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] image=stream.toByteArray();
                    String img_str_base64 = Base64.encodeToString(image, Base64.NO_WRAP);

                    if (imgId.equals("2"))
                    {
                        JSONObject frontImgObj = new JSONObject();
                        frontImgObj.put("VhlPictureSide", 2);
                        frontImgObj.put("Doc_For", 17);
                        frontImgObj.put("fileBase64", img_str_base64);
                        ImageList.put(frontImgObj);
                        img_DLFronside.setImageBitmap(bitmap);
                    } else if (imgId.equals("3"))
                    {
                        JSONObject backImgObj = new JSONObject();
                        backImgObj.put("VhlPictureSide", 3);
                        backImgObj.put("Doc_For", 17);
                        backImgObj.put("fileBase64", img_str_base64);
                        ImageList.put(backImgObj);
                        img_DLBackside.setImageBitmap(bitmap);
                    }

                } catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getScaledBitmap(Uri selectedImage, int width, int height) throws FileNotFoundException
    {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, sizeOptions);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {
            // Calculate ratios of height and width to requested one
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    public static String ConvertBitmapToString(Bitmap bitmap)
    {
        String encodedImage = "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        try {
            encodedImage= URLEncoder.encode(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return encodedImage;
    }

    OnResponseListener AddDriver = new OnResponseListener()
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
                            String msg = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),msg,0);

                            Bundle Booking = new Bundle();
                            Booking.putBundle("BookingBundle", BookingBundle);
                            Booking.putBundle("VehicleBundle", VehicleBundle);
                            Booking.putBundle("returnLocation", returnLocationBundle);
                            Booking.putBundle("location", locationBundle);
                            Booking.putBoolean("locationType", locationType);
                            Booking.putBoolean("initialSelect", initialSelect);
                            System.out.println(Booking);
                            NavHostFragment.findNavController(Fragment_Additional_Driver_Add.this)
                                    .navigate(R.id.action_Additional_Driver_Add_to_Additional_Driver_List,Booking);
                        }

                        else
                        {
                            String msg = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),msg,1);
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
            System.out.println("Error-" + error);
        }
    };
}
