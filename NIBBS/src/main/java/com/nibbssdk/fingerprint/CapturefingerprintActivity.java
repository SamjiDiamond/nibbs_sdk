package com.nibbssdk.fingerprint;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.usb.UsbDevice;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dermalog.afis.imagecontainer.DICException;
import com.dermalog.afis.imagecontainer.Decoder;
import com.dermalog.afis.imagecontainer.EncoderPng;
import com.dermalog.afis.imagecontainer.EncoderWsq;
import com.dermalog.afis.imagecontainer.RawImage;
import com.dermalog.biometricpassportsdk.BiometricPassportException;
import com.dermalog.biometricpassportsdk.BiometricPassportSdkAndroid;
import com.dermalog.biometricpassportsdk.Device;
import com.dermalog.biometricpassportsdk.DeviceCallback;
import com.dermalog.biometricpassportsdk.IUsbDeviceChangeListener;
import com.dermalog.biometricpassportsdk.enums.CallbackEventId;
import com.dermalog.biometricpassportsdk.enums.CaptureMode;
import com.dermalog.biometricpassportsdk.enums.DeviceId;
import com.dermalog.biometricpassportsdk.enums.FeatureId;
import com.dermalog.biometricpassportsdk.enums.NistFingerPosition;
import com.dermalog.biometricpassportsdk.usb.permission.IUsbPermissionListener;
import com.dermalog.biometricpassportsdk.usb.permission.PermissionResult;
import com.dermalog.biometricpassportsdk.utils.BitmapUtil;
import com.dermalog.biometricpassportsdk.wrapped.DeviceCallbackEventArgument;
import com.dermalog.biometricpassportsdk.wrapped.DeviceFeature;
import com.dermalog.biometricpassportsdk.wrapped.DeviceInfo;
import com.dermalog.biometricpassportsdk.wrapped.arguments.ErrorArgument;
import com.dermalog.biometricpassportsdk.wrapped.arguments.EventArgument;
import com.dermalog.biometricpassportsdk.wrapped.arguments.FingerprintSegment;
import com.dermalog.biometricpassportsdk.wrapped.arguments.FingerprintSegmentationArgument;
import com.dermalog.biometricpassportsdk.wrapped.arguments.ImageArgument;
import com.google.android.material.snackbar.Snackbar;
import com.nibbssdk.Constant;
import com.nibbssdk.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CapturefingerprintActivity extends AppCompatActivity {

    private final static String TAG = "Capturefingerprint";
    private ProgressBar progressBar;
    private ImageView captureImage;
    private ImageView fingerprintpreview;
    private ImageView fingerprintsegment;

    private BiometricPassportSdkAndroid sdk;
    private Device device;
    private Decoder imageDecoder;
    private EncoderWsq encoderWsq;
    private EncoderPng encoderPng;
    Bitmap segmentsBitmap;
    SharedPreferences sharedpreferences;
    Bundle extras;
    String fingerprintname = "";
    String fingerprintimage = "";
    //region definitions
    private enum ImageType {
        Detection,
        Preview,
        Segments
    }

    private static class SegmentInfo {
        NistFingerPosition nistFingerPosition;
        Bitmap bitmap;
        byte[] bihBytes;
    }

    private static class ImageHolder {
        final byte[] bihBytes;

        ImageHolder(byte[] bihBytes) {
            this.bihBytes = bihBytes;
        }
    }

    private final DeviceCallback deviceCallback = new DeviceCallback() {
        @Override
        public void onCall(Device device, DeviceCallbackEventArgument deviceCallbackEventArgument) {
            Log.d(TAG, "deviceCallback: EventId = " + deviceCallbackEventArgument.getEventId());
            for (EventArgument e : deviceCallbackEventArgument.getArguments()) {
                Log.d(TAG, " - " + e.toString());
            }
            // Once a FINGER_DETECT is received there will be no more FINGER_IMAGE messages until a
            // FINGER_REMOVE occurs - only detectionImageView and segmentsImageView are updated!
            switch (deviceCallbackEventArgument.getEventId()) {
                case ERROR:
                    showError(deviceCallbackEventArgument);
                    break;
                case FINGER_IMAGE:

                    for (EventArgument ea : deviceCallbackEventArgument.getArguments()) {
                        if (ea instanceof ImageArgument) {
                            ImageArgument img = (ImageArgument) ea;

                            Bitmap bitmap;
                            try {
                                bitmap = BitmapUtil.fromImageArgument(img);
                                showImage(bitmap, ImageType.Preview);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    break;

                case FINGER_DETECT:

                    Map<String, ImageHolder> images = new HashMap<>();

                    FingerprintSegmentationArgument fingerprintSegmentationArgument = null;
                    ImageArgument imageArgument = null;
                    List<EventArgument> eventArguments = deviceCallbackEventArgument.getArguments();
                    // All scanners will deliver an ImageArgument with the whole image.
                    // Scanners supporting fingerprints from multiple fingers will additionally
                    // deliver a FingerprintSegmentationArgument with information about the
                    // individual fingerprints (segments) like their position and extracted image.
                    for (EventArgument ea : eventArguments) {
                        if (ea instanceof FingerprintSegmentationArgument) {
                            fingerprintSegmentationArgument = (FingerprintSegmentationArgument) ea;
                        }
                        if (ea instanceof ImageArgument) {
                            imageArgument = (ImageArgument) ea;
                        }
                    }

                    try {
                        assert imageArgument != null;
                        Bitmap bitmap = BitmapUtil.fromImageArgument(imageArgument);
                        boolean isDetection = CallbackEventId.FINGER_DETECT.equals(
                                deviceCallbackEventArgument.getEventId());
                        if (isDetection) {
                            // Create an image for each segment
                            List<SegmentInfo> segmentInfos = extractSegmentInfos(
                                    bitmap, fingerprintSegmentationArgument);
                            for (int i = 0; i < segmentInfos.size(); i++) {
                                SegmentInfo si = segmentInfos.get(i);
                                ImageHolder ih = new ImageHolder(si.bihBytes);
                                String filename = device.getDeviceId() + "_" + i + " " + si.nistFingerPosition;
                                images.put(filename, ih);
                            }
                            // Create an image showing all segments
                           segmentsBitmap = createFingerprintSegmentsBitmap(segmentInfos,
                                    fingerprintsegment.getWidth(), fingerprintsegment.getHeight());
                            if (segmentsBitmap != null) {
                                showImage(segmentsBitmap, ImageType.Segments);
                            }

                        }
//                        // Create and show main image, even for previews
//                        showImage(bitmap, ImageType.Detection);

                        //Save image;
                        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "fingerprint" + timeStamp + "_";
                        if (fingerprintname.isEmpty()){
                            fingerprintname= imageFileName;
                        }else{
                            fingerprintname += ";"+imageFileName;
                        }
                        String path = Constant.saveToInternalStorage(bitmap, getApplicationContext(), imageFileName);
                        if (fingerprintimage.isEmpty()){
                            fingerprintimage= path;
                        }else{
                            fingerprintimage += ";"+path;
                        }

                        Log.d("TAG", "onClick: "+fingerprintimage);
                        Log.d("TAG", "onClick: "+fingerprintname);

                        ImageHolder ih = new ImageHolder(imageArgument.bitmapInfoHeaderData().getRawData());
                        images.put(device.getDeviceId() + "_Detect", ih);

                        // deviceCallback doesn't run on the UI thread - cannot access views directly!
                        final boolean enableButton = !images.isEmpty();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                               captureImage.setEnabled(enableButton);
                            }
                        });

                    } catch (IOException e1) {
                        Log.e(TAG, null, e1);
                    }
                    break;
            }
        }
    };

    //endregion definitions

    private Bitmap createFingerprintSegmentsBitmap(
            List<SegmentInfo> segmentInfos, int targetWidth, int targetHeight) {
        // If multiple fingers were detected create a bitmap containing all segments stacked
        // vertically with a red box around each and the NIST finger position in it.
        Bitmap result = null;
        if (segmentInfos.size() > 0) {
            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            result = Bitmap.createBitmap(targetWidth, targetHeight, conf);
            Canvas canvas = new Canvas(result);
            // Rects
            Rect textBounds = new Rect();
            Rect targetRect = new Rect();
            Rect sourceRect = new Rect();
            int targetSegmentHeight = targetHeight / segmentInfos.size();
            for (int i = 0; i < segmentInfos.size(); i++) {
                // Draw fingerprint segment bitmap
                SegmentInfo si = segmentInfos.get(i);
                targetRect.left = 0;
                sourceRect.left = 0;
                sourceRect.top = 0;
                sourceRect.bottom = si.bitmap.getHeight();
                sourceRect.right = si.bitmap.getWidth();
                float sourceAspectRatio = (float) sourceRect.width() / (float) sourceRect.height();
                targetRect.right = Math.round(targetSegmentHeight * sourceAspectRatio);
                targetRect.top = i * targetSegmentHeight;
                targetRect.bottom = targetRect.top + targetSegmentHeight;
                canvas.drawBitmap(si.bitmap, sourceRect, targetRect, null);

            }
        }
        return result;
    }




    private List<SegmentInfo> extractSegmentInfos(
            Bitmap sourceBitmap,
            FingerprintSegmentationArgument fingerprintSegmentationArgument) {
        List<SegmentInfo> result = new ArrayList<>();
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Rect targetRect = new Rect();
        targetRect.left = 0;
        targetRect.top = 0;
        if (fingerprintSegmentationArgument != null) {
            FingerprintSegment[] fingerprintSegments
                    = fingerprintSegmentationArgument.getFingerprintSegments();
            for (FingerprintSegment f : fingerprintSegments) {
                Rect sourceRect = new Rect();
                sourceRect.left = f.getPositionTopLeft().x;
                sourceRect.top = f.getPositionTopLeft().y;
                sourceRect.bottom = f.getPositionBottomRight().y;
                sourceRect.right = f.getPositionBottomRight().x;
                targetRect.bottom = sourceRect.bottom - sourceRect.top;
                targetRect.right = sourceRect.right - sourceRect.left;
                SegmentInfo si = new SegmentInfo();
                result.add(si);
                si.bitmap = Bitmap.createBitmap(targetRect.width(), targetRect.height(), conf);
                Canvas canvas = new Canvas(si.bitmap);
                canvas.drawBitmap(sourceBitmap, sourceRect, targetRect, null);
                si.nistFingerPosition = f.getNistFingerPosition();
                si.bihBytes = f.getBitmapInfoHeaderData().getRawData();

            }
        }
        return result;
    }

//    private byte[] getWsqBytes(byte[] rawData) throws DICException {
//        RawImage rawImage = imageDecoder.Decode(rawData);
//        try {
//            return encoderWsq.Encode(rawImage);
//        } finally {
//            rawImage.close();
//        }
//    }
//
    private byte[] getPngBytes(byte[] rawData) throws DICException {
        RawImage rawImage = imageDecoder.Decode(rawData);
        try {

            return encoderPng.Encode(rawImage);
        } finally {
            rawImage.close();
        }
    }

    private void initializeImageContainer() throws DICException {
        // com.dermalog.afis.imagecontainer.Decoder is used to create well defined raw images used
        // by the Encoder
        imageDecoder = new Decoder();
        // com.dermalog.afis.imagecontainer.Encoder is used to convert images to other formats,
        // like png or wsq
        encoderWsq = new EncoderWsq();
        encoderPng = new EncoderPng();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capturefingerprint);
        ImageView backbutton = findViewById(R.id.backbutton);
        sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        Constant.table_id = sharedpreferences.getString("table_id","");
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView fingertext =  findViewById(R.id.fingername);
        extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("key");
            fingertext.setText(extras.getString("data")+" Finger.");
            //The key argument here must match that used in the other activity
        }else{
            fingertext.setText("Right Thumb Finger.");
        }
        fingerprintpreview = findViewById(R.id.fingerprintpreview);
        fingerprintsegment = findViewById(R.id.fingerprintsegment);
        captureImage = findViewById(R.id.captureImage);
//        captureImage.setEnabled(false);
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopScanning();
                uninitializeSdk();
                uninitializeImageContainer();
                @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "fingerprint" + timeStamp + "_";
                if (fingerprintname.isEmpty()){
                    fingerprintname= imageFileName;
                }else{
                    fingerprintname += ";"+imageFileName;
                }
                String path = Constant.saveToInternalStorage(segmentsBitmap, getApplicationContext(), imageFileName);
                if (fingerprintimage.isEmpty()){
                    fingerprintimage= path;
                }else{
                    fingerprintimage += ";"+path;
                }

                Log.d("TAG", "onClick: "+fingerprintimage);
                Log.d("TAG", "onClick: "+fingerprintname);
                uninitializeImageContainer();
                Intent in;
                if (extras != null) {
                    in = new Intent(getApplicationContext(), CapturefingerprintActivity.class);
                    switch (extras.getString("data")) {
                        case "Left Index":
                            in.putExtra("data", "Left Middle");
                            break;
                        case "Left Middle":
                            in.putExtra("data", "Left Ring");
                            break;
                        case "Left Ring":
                            in.putExtra("data", "Left Little");
                            break;
                        case "Left Little":
                            in.putExtra("data", "Right Index");
                            break;
                        case "Right Index":
                            in.putExtra("data", "Right Middle");
                            break;
                        case "Right Middle":
                            in.putExtra("data", "Right Ring");
                            break;
                        case "Right Ring":
                            in.putExtra("data", "Right Little");
                            break;
                        case "Right Little":
                            in.putExtra("data", "Left Thumb");
                            break;
                    }
                }else {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("fingerprintimage",fingerprintimage);
                    editor.putString("fingerprintname",fingerprintname);
                    editor.apply();
                    in = new Intent(getApplicationContext(), FingerprintActivity.class);
                }
                startActivity(in);
            }
        });

        // Initializing the SDK may take a while. Do it on a background thread and show a loading
        // spinner / progressBar to avoid that the app seems unresponsive.
        new Thread(new Runnable(){
            public void run() {
                try {
                    System.setProperty("DERMALOG_SDK_LOG", "1");
                    initializeImageContainer();
                    sdk = new BiometricPassportSdkAndroid(getApplicationContext());
                    sdk.registerUsbDeviceChangeListener(new IUsbDeviceChangeListener() {
                        @Override
                        public void onUsbDeviceChange(DeviceState deviceState, UsbDevice usbDevice) {
                            if(deviceState == IUsbDeviceChangeListener.DeviceState.Connected) {
                                requestUsbPermissionsAndStart(sdk);
                            }
                        }
                    });
                    requestUsbPermissionsAndStart(sdk);
                } catch (BiometricPassportException | DICException e) {
                    Log.e(TAG, null, e);
                    showError("Initialization error: " + e.getMessage());
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
    protected void onDestroy() {
        super.onDestroy();
        stopScanning();
        uninitializeSdk();
        uninitializeImageContainer();

    }

    private void requestUsbPermissionsAndStart(final BiometricPassportSdkAndroid sdk) {
        sdk.requestUSBPermissionsAsync(new IUsbPermissionListener() {
            @Override
            public void onUsbPermissions(PermissionResult result) {
                String errorMessage = null;
                switch (result.getResult()) {
                    case NoDevice:
                        errorMessage = "No USB devices!";
                        break;
                    case NoPermission:
                        errorMessage = "No USB permission!";
                        break;
                    case PartialPermission:
                        errorMessage = "Not all USB permissions!";
                        break;
                    case Success:
                        Log.d(TAG, "USB permission granted.");
                        startScanning();
                        break;
                    case UsbNotSupported:
                        errorMessage = "No USB support!";
                        break;
                }
                if (errorMessage != null) {
                    Log.e(TAG, errorMessage);
                    showError(errorMessage);
                }
            }
        });
    }

    /**
     * Shows a snackbar with errorMessage. Can be called from non-UI threads.
     */
    private void showError(final String errorMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View snackbarContainerView = findViewById(android.R.id.content);
                final Snackbar snackbar = Snackbar.make(
                        snackbarContainerView,
                        errorMessage,
                        Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        });
    }

    /**
     * Shows a snackbar with the error contained in deviceCallbackEventArgument or the text "ERROR".
     * Can be called from non-UI threads.
     */
    private void showError(
            DeviceCallbackEventArgument deviceCallbackEventArgument) {
        String msg = "ERROR";
        for (EventArgument arg : deviceCallbackEventArgument.getArguments()) {
            if (arg instanceof ErrorArgument) {
                ErrorArgument error = (ErrorArgument) arg;
                msg = error.getReturnCode() + " - " + error.getMessage();
                break;
            }
        }
        showError(msg);
    }

    private void showImage(final Bitmap bmp, final ImageType imageType) {
        // showImage() is called from deviceCallback which doesn't run on the UI thread - cannot access views directly!
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (imageType) {
                    case Detection:
                        fingerprintpreview.setImageBitmap(bmp);
                        break;
                    case Preview:
                        fingerprintpreview.setImageBitmap(bmp);
                        break;
                    case Segments:
                        fingerprintpreview.setVisibility(View.GONE);
                        fingerprintsegment.setImageBitmap(bmp);
                        break;
                    default:
                        Log.e(TAG, "Unexpected imageType = " + imageType);
                }
            }
        });
    }

    private void showMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast t = Toast.makeText(
                        getApplicationContext(), message, Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                t.show();
            }
        });
    }

    private void startScanning() {
        try {
            DeviceInfo[] deviceInfos = sdk.enumerateDevices(DeviceId.ALL);
            if (deviceInfos.length > 0) {
                Log.d(TAG, "Found " + deviceInfos.length + " devices.");
                device = sdk.createDevice(deviceInfos[0]);
                Log.d(TAG, "Defaulting to scanner " + device.toString());
                boolean isLivenessCheckSupported = false;
                // Liveness check support depends on both scanner capabilities and on compiling
                // the app with DermalogFakeFingerDetectionZF1Plugin
                for (DeviceFeature df : device.getSupportedFeatures()) {
                    if (df.getId() == FeatureId.FINGER_LIVENESS) {
                        isLivenessCheckSupported = true;
                        break;
                    }
                }
                if (isLivenessCheckSupported) {
                    device.setFeature(FeatureId.FINGER_LIVENESS, 1);
                }
                Log.d(TAG, "isLivenessCheckSupported = " + isLivenessCheckSupported);
                CaptureMode captureMode = device.getCaptureMode();
                Log.d(TAG, "Default capture mode = " + captureMode);
                device.registerCallback(deviceCallback);
                final String message = "Ready to scan - place finger.";
                Log.d(TAG, message);
                showMessage(message);
                device.startCapture();
            } else {
                Log.e(TAG, "No scanner attached");
                showError("No scanner attached");
            }
        } catch (BiometricPassportException e) {
            showError("Error: "+e.getMessage());
        }
    }

    private void stopScanning() {

        if (device != null) {
            try {
                device.stopCapture();
            } catch (BiometricPassportException e) {
                Log.e(TAG, null, e);
            }
            device.dispose();
            device = null;
        }

    }


    private void uninitializeSdk() {
        stopScanning();

        if (sdk != null) {
            sdk.dispose();
            sdk = null;
        }
    }

    private void uninitializeImageContainer() {
        if (encoderPng != null) {
            try {
                encoderPng.close();
            } catch (DICException e) {
                Log.e(TAG, null, e);
            }
            encoderPng = null;
        }
        if (encoderWsq != null) {
            try {
                encoderWsq.close();
            } catch (DICException e) {
                Log.e(TAG, null, e);
            }
            encoderWsq = null;
        }
        if (imageDecoder != null) {
            try {
                imageDecoder.close();
            } catch (DICException e) {
                Log.e(TAG, null, e);
            }
            imageDecoder = null;
        }
    }
}