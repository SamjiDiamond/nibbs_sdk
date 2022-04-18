package com.nibbssdk.fingerprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dermalog.afis.imagecontainer.DICException;
import com.dermalog.afis.imagecontainer.Decoder;
import com.dermalog.afis.imagecontainer.EncoderPng;
import com.dermalog.afis.imagecontainer.EncoderWsq;
import com.dermalog.biometricpassportsdk.BiometricPassportException;
import com.dermalog.biometricpassportsdk.BiometricPassportSdkAndroid;
import com.dermalog.biometricpassportsdk.IUsbDeviceChangeListener;
import com.dermalog.biometricpassportsdk.usb.permission.IUsbPermissionListener;
import com.dermalog.biometricpassportsdk.usb.permission.PermissionResult;
import com.integratedbiometrics.ibscanultimate.IBScan;
import com.integratedbiometrics.ibscanultimate.IBScanDevice;
import com.integratedbiometrics.ibscanultimate.IBScanException;
import com.integratedbiometrics.ibscanultimate.IBScanListener;
import com.nibbssdk.R;

import java.util.HashMap;
import java.util.Iterator;

public class BeginfingerprintActivity extends AppCompatActivity implements IBScanListener{

    private Decoder imageDecoder;
    private EncoderWsq encoderWsq;
    private EncoderPng encoderPng;

    private BiometricPassportSdkAndroid sdk;
    boolean kojak = false, dermalog = false;

    /*
     * A handle to the single instance of the IBScan class that will be the primary interface to
     * the library, for operations like getting the number of scanners (getDeviceCount()) and
     * opening scanners (openDeviceAsync()).
     */
    private IBScan       m_ibScan;

    private void requestUsbPermissionsAndStart(final BiometricPassportSdkAndroid sdk) {
        sdk.requestUSBPermissionsAsync(new IUsbPermissionListener() {
            @Override
            public void onUsbPermissions(PermissionResult result) {
                String errorMessage = null;
                switch (result.getResult()) {
                    case NoDevice:
                        errorMessage = "No USB devices!";
                        Log.e("tolulope", errorMessage);
                        dermalog = false;
                        break;
                    case NoPermission:
                        errorMessage = "No USB permission!";
                        dermalog = false;
                        break;
                    case PartialPermission:
                        errorMessage = "Not all USB permissions!";
                        dermalog = false;
                        break;
                    case Success:
                        dermalog = true;
                        break;
                    case UsbNotSupported:
                        errorMessage = "No USB support!";
                        dermalog = false;
                        break;
                }
                if (errorMessage != null) {
//                    Log.e(TAG, errorMessage);
//                    showError(errorMessage);
                }
            }
        });
    }

    private void startdermalog(){
        // Initializing the SDK may take a while. Do it on a background thread and show a loading
        // spinner / progressBar to avoid that the app seems unresponsive.
        new Thread(new Runnable(){
            public void run() {
                    try {
                        System.setProperty("DERMALOG_SDK_LOG", "1");

                        sdk = new BiometricPassportSdkAndroid(getApplicationContext());
                    sdk.registerUsbDeviceChangeListener(new IUsbDeviceChangeListener() {
                        @Override
                        public void onUsbDeviceChange(DeviceState deviceState, UsbDevice usbDevice) {
                            if(deviceState == DeviceState.Connected) {
                                requestUsbPermissionsAndStart(sdk);
                            }
                        }
                    });
                        requestUsbPermissionsAndStart(sdk);
                    } catch (BiometricPassportException e) {
//                            Log.e(TAG, null, e);
//                            showError("Initialization error: " + e.getMessage());
                    }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginfingerprint);
        Button button = findViewById(R.id.formdata);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tolulope", "onClick: kojak "+kojak);
                Log.d("tolulope", "onClick: dermalog "+dermalog);
                Intent in = null;
                if (kojak || dermalog) {
                    if (kojak) {
                        in = new Intent(getApplicationContext(), SimpleScanActivity.class);
                    }
                    if (dermalog) {
                        in = new Intent(getApplicationContext(), CapturefingerprintActivity.class);
                    }
                    in.putExtra("data", "Left Index");
                    startActivity(in);
                }else{
                    Toast.makeText(getApplicationContext(), "Kindly connect a finger print scanner", Toast.LENGTH_SHORT).show();
                }
//                finish();
            }
        });

        startdermalog();
        startkojak();
    }

    private void startkojak(){
        /*
         * Make sure there are no USB devices attached that are IB scanners for which permission has
         * not been granted.  For any that are found, request permission; we should receive a
         * callback when permission is granted or denied and then when IBScan recognizes that new
         * devices are connected, which will result in another refresh.
         */
        m_ibScan = IBScan.getInstance(this.getApplicationContext());
        m_ibScan.setScanListener(this);
        final UsbManager manager        = (UsbManager)this.getApplicationContext().getSystemService(Context.USB_SERVICE);
        final HashMap<String, UsbDevice> deviceList     = manager.getDeviceList();
        final Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext())
        {
            final UsbDevice device       = deviceIterator.next();
            final boolean   isScanDevice = IBScan.isScanDevice(device);
            if (isScanDevice)
            {
                kojak = true;
                final boolean hasPermission = manager.hasPermission(device);
                if (!hasPermission)
                {
                    this.m_ibScan.requestPermission(device.getDeviceId());
                }

            }else{
                kojak = false;
            }
        }
    }


    // //////////////////////////////////////////////////////////////////////////////////////////////
    // PUBLIC INTERFACE: IBScanListener METHODS
    // //////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void scanDeviceAttached(final int deviceId)
    {
        showToastOnUiThread("Device " + deviceId + " attached", Toast.LENGTH_SHORT);

        Log.e("tolulope", "scanDeviceAttached: device");
        kojak = true;
        /*
         * Check whether we have permission to access this device.  Request permission so it will
         * appear as an IB scanner.
         */
        final boolean hasPermission = m_ibScan.hasPermission(deviceId);
        if (!hasPermission)
        {
            m_ibScan.requestPermission(deviceId);
        }
    }

    @Override
    public void scanDeviceDetached(final int deviceId)
    {
        /*
         * A device has been detached.  We should also receive a scanDeviceCountChanged() callback,
         * whereupon we can refresh the display.  If our device has detached while scanning, we
         * should receive a deviceCommunicationBreak() callback as well.
         */
        kojak = false;
        showToastOnUiThread("Device " + deviceId + " detached", Toast.LENGTH_SHORT);
    }

    @Override
    public void scanDevicePermissionGranted(final int deviceId, final boolean granted)
    {
        if (granted)
        {
            /*
             * This device should appear as an IB scanner.  We can wait for the scanDeviceCountChanged()
             * callback to refresh the display.
             */
            showToastOnUiThread("Permission granted to device " + deviceId, Toast.LENGTH_SHORT);
        }
        else
        {
            showToastOnUiThread("Permission denied to device " + deviceId, Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void scanDeviceCountChanged(final int deviceCount)
    {
//        OnMsg_UpdateDeviceList(false);
    }

    @Override
    public void scanDeviceInitProgress(final int deviceIndex, final int progressValue)
    {
//        OnMsg_SetStatusBarMessage("Initializing device..."+ progressValue + "%");
    }

    @Override
    public void scanDeviceOpenComplete(final int deviceIndex, final IBScanDevice device,
                                       final IBScanException exception)
    {
    }

    private void showToastOnUiThread(final String message, final int duration)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast toast = Toast.makeText(getApplicationContext(), message, duration);
                toast.show();
            }
        });
    }
}