package com.riidez.app.flexiicar_app.ochs;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.AndroidNetworking;
import com.riidez.app.R;
import com.riidez.app.adapters.CustomToast;
import com.riidez.app.apicall.ApiService;
import com.riidez.app.apicall.OnResponseListener;
import com.riidez.app.apicall.RequestType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hufsm.keyholder.mobile.KeyholderInterface;
import com.hufsm.secureaccess.ble.log.LoggingInterface;
import com.hufsm.tacs.mobile.TacsInterface;
import com.hufsm.tacs.mobile.TacsKeyring;
import com.hufsm.telematics.mobile.TelematicsInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

import static com.riidez.app.apicall.ApiEndPoint.BASE_URL_HUFKEY;
import static com.riidez.app.apicall.ApiEndPoint.GET_MOBILEKEY_DETAILS;

public class Fragment_Vehicle_LocationAndKey extends Fragment implements TacsInterface.Callback, LoggingInterface.EventLogCallback
{
    Button btnLock,btnUnlock,btnBtConnect,btnBtDisConnect,btnTrunk;
    TacsInterface tacsInterface;
    TacsInterface.VehicleStatus currentStatus;
    public static final Collection<TelematicsInterface.TelematicsDataType> DEFAULT_TELEMATICS_REQUEST = TelematicsInterface.TelematicsDataType.buildRequest();
    static final String NOT_AVAILABLE_STATUS = "Not available";
    static final String NOT_CONNECTED_STATUS = "Not connected";
    final Object mutex = new Object();
    static final String TAG = Fragment_VehicleAvailable.class.getSimpleName();
    TacsKeyring keyring = null;
    String MY_MOCK_ACCESS_GRANT_ID;
    Handler handler = new Handler();
    Bundle VehicleModelBundle;
    int vehiclE_ID;
    String make_Name,model_Name;
    ImageView Back;
    private boolean useMockMode = true;

    BluetoothAdapter mBluetoothAdapter;

    String tacsKeyring;
    TextView txtVehiclaName,lblDone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_and_key, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        try {
            super.onViewCreated(view, savedInstanceState);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            VehicleModelBundle = getArguments().getBundle("VehicleModelBundle");
            vehiclE_ID = VehicleModelBundle.getInt("vehiclE_ID");
            make_Name = VehicleModelBundle.getString("make_Name");
            model_Name = VehicleModelBundle.getString("model_Name");

            vehiclE_ID = 3779;

            btnLock = view.findViewById(R.id.btnLock);
            btnUnlock = view.findViewById(R.id.btnUnlock);
            btnBtConnect = view.findViewById(R.id.btnBtConnect);
            btnBtDisConnect= view.findViewById(R.id.btnBtDisConnect);
            btnTrunk = view.findViewById(R.id.btnTrunk);

            lblDone= view.findViewById(R.id.lblDone);

            txtVehiclaName= view.findViewById(R.id.txtVehiclaName);
            txtVehiclaName.setText(make_Name);

            Back= view.findViewById(R.id.Back);
            Back.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Vehicle_LocationAndKey.this)
                            .navigate(R.id.action_LocationAndKey_to_Vehicles_Available);
                }
            });

            lblDone= view.findViewById(R.id.lblDone);
            lblDone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Vehicle_LocationAndKey.this)
                            .navigate(R.id.action_LocationAndKey_to_Vehicles_Available);
                }
            });

            btnBtConnect.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    turnOnBluetoothDevice();
                }
            });

            btnBtDisConnect.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    turnOffBluetoothDevice();
                }
            });

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            btnLock.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(!mBluetoothAdapter.isEnabled())
                    {
                        CustomToast.showToast(getActivity(), "Please Connect Bluetooth", 0);
                    }
                    else {
                        tacsInterface.vehicleAccess().lockDoors();
                    }
                }
            });

            btnUnlock.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(!mBluetoothAdapter.isEnabled())
                    {
                        CustomToast.showToast(getActivity(), "Please Connect Bluetooth", 0);
                    }
                    else {
                        tacsInterface.vehicleAccess().unlockDoors();
                    }
                }
            });
            AndroidNetworking.initialize(getActivity());

            String bodyParam = "";
            try {
                bodyParam += "vehicleId=" + vehiclE_ID;
                System.out.println(bodyParam);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            ApiService ApiService = new ApiService(getMobileKeyDetails, RequestType.GET,
                    GET_MOBILEKEY_DETAILS, BASE_URL_HUFKEY, new HashMap<String, String>(), bodyParam);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    OnResponseListener getMobileKeyDetails = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        System.out.println("Success");
                        System.out.println(response);
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("status");

                        if (status)
                        {
                            try {
                                JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                                JSONObject hufKeyDetailModelList = resultSet.getJSONObject("hufKeyDetailModel");

                                JSONObject keyringJson = hufKeyDetailModelList.getJSONObject("keyring");
                                {
                                    String userLeaseTokenTableVersion = keyringJson.getString("userLeaseTokenTableVersion");
                                    JSONArray userLeaseTokenTable = keyringJson.getJSONArray("userLeaseTokenTable");
                                    JSONArray userSorcBlobTable = keyringJson.getJSONArray("userSorcBlobTable");


                                    int len;
                                    len = userLeaseTokenTable.length();
                                    for (int j = 0; j < len; j++) {
                                        final JSONObject test = (JSONObject) userLeaseTokenTable.get(j);
                                        final String leaseId = test.getString("leaseId");
                                        final String userId = test.getString("userId");
                                        final String sorcId = test.getString("sorcId");

                                        final String leaseTokenDocumentVersion = test.getString("leaseTokenDocumentVersion");
                                        final String leaseTokenId = test.getString("leaseTokenId");
                                        final String sorcAccessKey = test.getString("sorcAccessKey");
                                        final String startTime = test.getString("startTime");
                                        final String endTime = test.getString("endTime");

                                        JSONArray serviceGrantList = test.getJSONArray("serviceGrantList");

                                        int len1;
                                        len1 = serviceGrantList.length();

                                        for (int k = 0; k < len1; k++) {
                                            final JSONObject test1 = (JSONObject) serviceGrantList.get(k);
                                            final String serviceGrantId = test1.getString("serviceGrantId");
                                            final JSONObject validators = test1.getJSONObject("validators");
                                            final String ValistartTime = validators.getString("startTime");
                                            final String ValiendTime = validators.getString("endTime");
                                        }
                                    }

                                    int len3;
                                    len3 = userSorcBlobTable.length();
                                    for (int i = 0; i < len3; i++) {
                                        final JSONObject test = (JSONObject) userSorcBlobTable.get(i);
                                        //final String sorcId = test.getString("sorcId");
                                        final String blob = test.getString("blob");
                                        final String blobMessageCounter = test.getString("blobMessageCounter");

                                    }

                                }

                                JSONObject tacsKeyringJson = hufKeyDetailModelList.getJSONObject("tacsKeyring");
                                tacsKeyring=tacsKeyringJson.toString();
                                {
                                    String tacsLeaseTokenTableVersion = tacsKeyringJson.getString("tacsLeaseTokenTableVersion");
                                    String tacsSorcBlobTableVersion = tacsKeyringJson.getString("tacsSorcBlobTableVersion");
                                    JSONArray tacsLeaseTokenTable = tacsKeyringJson.getJSONArray("tacsLeaseTokenTable");
                                    JSONArray tacsSorcBlobTable = tacsKeyringJson.getJSONArray("tacsSorcBlobTable");

                                    int len;
                                    len = tacsLeaseTokenTable.length();
                                    for (int j = 0; j < len; j++)
                                    {
                                        final JSONObject test = (JSONObject) tacsLeaseTokenTable.get(j);
                                        final String vehicleAccessGrantId = test.getString("vehicleAccessGrantId");
                                        JSONObject leaseToken = test.getJSONObject("leaseToken");
                                        final String leaseId = leaseToken.getString("leaseId");
                                        final String userId = leaseToken.getString("userId");
                                        final String sorcId = leaseToken.getString("sorcId");

                                        final String leaseTokenDocumentVersion = leaseToken.getString("leaseTokenDocumentVersion");
                                        final String leaseTokenId = leaseToken.getString("leaseTokenId");
                                        final String sorcAccessKey = leaseToken.getString("sorcAccessKey");
                                        final String startTime = leaseToken.getString("startTime");
                                        final String endTime = leaseToken.getString("endTime");

                                        MY_MOCK_ACCESS_GRANT_ID=vehicleAccessGrantId;

                                        final JSONArray serviceGrantList = leaseToken.getJSONArray("serviceGrantList");

                                        int len1;
                                        len1 = serviceGrantList.length();

                                        for (int k = 0; k < len1; k++) {
                                            final JSONObject test1 = (JSONObject) serviceGrantList.get(k);
                                            final String serviceGrantId = test1.getString("serviceGrantId");
                                            final JSONObject validators = test1.getJSONObject("validators");
                                            final String ValistartTime = validators.getString("startTime");
                                            final String ValiendTime = validators.getString("endTime");
                                        }
                                    }
                                    int len3;
                                    len3 = tacsSorcBlobTable.length();
                                    for (int i = 0; i < len3; i++)
                                    {
                                        final JSONObject test = (JSONObject) tacsSorcBlobTable.get(i);
                                        //final String sorcId = test.getString("sorcId");
                                        //final String blob = test.getString("blob");
                                        //final String blobMessageCounter = test.getString("blobMessageCounter");

                                    }
                                }
                                keyring = getMockedKeyring();
                                initializeBLESetUp();
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
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

    private void initializeBLESetUp()
    {

        if (tacsInterface != null)
        {
            tacsInterface.closeInterface();
        }
        tacsInterface = new TacsInterface.Builder(this, getActivity())
                .enableMockMode(useMockMode)
                .build();

        tacsInterface.logging().registerEventCallback(this);
        tacsInterface.logging().setLogLevel(LoggingInterface.LogLevel.DEBUG);

        if (tacsInterface.useAccessGrant(MY_MOCK_ACCESS_GRANT_ID, keyring))
        {
            //The key ring is loaded successfully
            Log.i(TAG, "Keyring loaded");

            if(mBluetoothAdapter.isEnabled())
            {
                btnBtDisConnect.setVisibility(View.GONE);
                btnBtConnect.setVisibility(View.VISIBLE);
                tacsInterface.searchAndConnect();
            }
        } else {
            //Invalid keyring
            Log.e(TAG, "Keyring invalid");
        }

        System.out.println("TacsInterface.ConnectionStatus:-" + TacsInterface.ConnectionStatus.UNAVAILABLE.name());
        System.out.println("KeyholderInterface.StatusCode:-" +KeyholderInterface.StatusCode.KEYHOLDER_NOT_DETECTED.name());
        System.out.println("No data");
    }

    private TacsKeyring getMockedKeyring()
    {
        Gson gson = new Gson();
        Type configType = new TypeToken<TacsKeyring>(){}.getType();
        try{
            System.out.println(tacsKeyring.toString());
            return gson.fromJson(tacsKeyring.toString(), configType);
        }catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
            return new TacsKeyring();
        }
    }

    void turnOnBluetoothDevice()
    {
        try {
            BluetoothAdapter BluetoothAdapter1 = BluetoothAdapter.getDefaultAdapter();
            if (!BluetoothAdapter1.isEnabled())
            {
                BluetoothAdapter1.enable();
                btnBtDisConnect.setVisibility(View.VISIBLE);
                btnBtConnect.setVisibility(View.GONE);
                tacsInterface.searchAndConnect();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void turnOffBluetoothDevice()
    {
        BluetoothAdapter BluetoothAdapter2 = BluetoothAdapter.getDefaultAdapter();
        if (BluetoothAdapter2.isEnabled())
        {
            BluetoothAdapter2.disable();
            btnBtConnect.setVisibility(View.VISIBLE);
            btnBtDisConnect.setVisibility(View.GONE);
            tacsInterface.cancelConnection();
        }
    }

    @Override
    public void updateDeviceStatus(final TacsInterface.BluetoothDeviceStatus deviceStatus, final String errorMessage)
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    synchronized (mutex)
                    {
                        System.out.println("1"+deviceStatus.name());
                        switch (deviceStatus)
                        {
                            case DEVICE_AVAILABLE:
                                //
                                break;
                            case DEVICE_QUEUE_LIMIT_REACHED:
                                CustomToast.showToast(getActivity(), "Slow down, please!", 0);
                                //fallthrough
                                break;
                            case DEVICE_NOT_SUPPORTED:
                                CustomToast.showToast(getActivity(), "Device Not Supported!",0);
                                break;
                            case DEVICE_DEACTIVATED:
                                //
                                break;
                            case DEVICE_PERMISSION_ERROR:
                                // fallthrough
                                break;
                            case DEVICE_ERROR:
                                // fallthrough
                                break;
                            default:
                             //  System.out.println("3"+VehicleAccessInterface.DoorStatus.DOORS_UNKNOWN.name());
                                //  CustomToast.showToast(getActivity(), VehicleAccessInterface.DoorStatus.DOORS_UNKNOWN.name(), 0);
                                break;
                        }
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void updateVehicleStatus(final TacsInterface.VehicleStatus newStatus)
    {
        try {
            if (currentStatus != null && newStatus.getConnectionStatus().isConnected() && !currentStatus.getConnectionStatus().isConnected())
            {
                tacsInterface.vehicleAccess().requestCurrentVehicleStatus(); // Initial door and immobilizer status are highly useful to have
                tacsInterface.telematics().queryTelematicsData(DEFAULT_TELEMATICS_REQUEST); // Telematics may also be useful, depending on use case
            }
            currentStatus = newStatus;

            final Collection<TelematicsInterface.TelematicsData> td = newStatus.getTelematicsData();
            //   final String odometerStatus = formatTelematicsData(td, TelematicsInterface.TelematicsDataType.ODOMETER);

            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        if (newStatus.getConnectionStatus().isError())
                        {
                            String message = newStatus.getErrorMessage();
                            message = message == null || message.isEmpty() ? "No details" : message;
                            CustomToast.showToast(getActivity(),newStatus.getConnectionStatus().name() + " : " + message, 0);
                            System.out.println(newStatus.getConnectionStatus().name() + " : " + message);
                        }
                        else {
                            switch (newStatus.getConnectionStatus())
                            {
                                case UNAVAILABLE:
                                     break;
                                case CONNECTED:
                                    CustomToast.showToast(getActivity(),newStatus.getConnectionStatus().name(), 0);
                                    System.out.println("4"+newStatus.getConnectionStatus().name());
                                    break;
                                case REMOVED:
                                    CustomToast.showToast(getActivity(),"DISCONNECTED", 0);
                                    System.out.println("5"+newStatus.getConnectionStatus().name());
                                    break;
                                default:
                                    break;
                            }
                            if(newStatus.getConnectionStatus().isConnected())
                            {
                                switch (newStatus.getDoorStatus())
                                {
                                    case DOORS_LOCKED:
                                        CustomToast.showToast(getActivity(), newStatus.getDoorStatus().name(), 0);
                                        System.out.println("6" + newStatus.getDoorStatus().name());
                                        break;
                                    case DOORS_UNLOCKED:
                                        CustomToast.showToast(getActivity(), newStatus.getDoorStatus().name(), 0);
                                        System.out.println("7" + newStatus.getDoorStatus().name());
                                        break;
                                    case DOORS_UNKNOWN:
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String formatTelematicsData(Collection<TelematicsInterface.TelematicsData> responses, TelematicsInterface.TelematicsDataType type) {

        for (TelematicsInterface.TelematicsData data : responses)
        {
            if (data.type() == type)
            {
                if (data.statusCode() == TelematicsInterface.TelematicsStatusCode.SUCCESS)
                {
                    return String.format("%s :\n%s (%s)", data.timestamp(), data.value(), data.unit());
                } else {
                    return String.format("%s :\n%s", data.timestamp(), data.statusCode().name());
                }
            }
        }
        return NOT_AVAILABLE_STATUS;
    }

    private String formatKeyholderPayload(KeyholderInterface.KeyholderStatus status)
    {
        return String.format(Locale.US, "vBatt=%f, actCnt=%d, battChg=%d, card=%s",
                status.getBatteryVoltage(),
                status.getActivationCounter(),
                status.getBatteryChanges(),
                status.isCardPresent() ? "YES" : "NO"
        );
    }

    @Override
    public void logEvent(@NonNull LoggingInterface.LogEvent logEvent)
    {

    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        tacsInterface.closeInterface();
    }
}