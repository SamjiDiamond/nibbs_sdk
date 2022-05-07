/* *************************************************************************************************
 * SimpleScanActivity.java
 *
 * DESCRIPTION:
 *     SimpleScan demo app for IBScanUltimate
 *     http://www.integratedbiometrics.com
 *
 * NOTES:
 *     Copyright (c) Integrated Biometrics, 2012-2017
 *     
 * HISTORY:
 *     2013/03/01  First version.
 *     2013/03/06  Added automatic population of capture type spinner based on initialized scanner.
 *                 A refresh from INITIALIZED now checks whether the initialized scanner is present;
 *                 if not, the scanner is closed and the list refreshed.
 *     2013/03/22  Added NFIQ calculation following completion of image capture.
 *     2013/04/06  Made minor changes, including allowing user to supply file name of e-mail image.
 *     2013/10/18  Add support for new finger quality values that indicate presence of finger in 
 *                 invalid area along scanner edge.
 *                 Implement new extended result method for IBScanDeviceListener.
 *     2013/06/19  Updated for IBScanUltimate v1.7.3.
 *     2015/04/07  Updated for IBScanUltimate v1.8.4.
 *     2015/12/11  Updated for IBScanUltimate v1.9.0.
 *     2016/01/21  Updated for IBScanUltimate v1.9.2.
 *     2016/09/22  Updated for IBScanUltimate v1.9.4.
 *     2018/03/06  Re-formatted code for IBScanUltimate v2.0.0
************************************************************************************************ */

package com.nibbssdk.fingerprint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.integratedbiometrics.ibscanultimate.IBScan;
import com.integratedbiometrics.ibscanultimate.IBScan.HashType;
import com.integratedbiometrics.ibscanultimate.IBScan.SdkVersion;
import com.integratedbiometrics.ibscanultimate.IBScanDevice;
import com.integratedbiometrics.ibscanultimate.IBScanDevice.FingerCountState;
import com.integratedbiometrics.ibscanultimate.IBScanDevice.FingerQualityState;
import com.integratedbiometrics.ibscanultimate.IBScanDevice.ImageData;
import com.integratedbiometrics.ibscanultimate.IBScanDevice.ImageType;
import com.integratedbiometrics.ibscanultimate.IBScanDevice.LedState;
import com.integratedbiometrics.ibscanultimate.IBScanDevice.PlatenState;
import com.integratedbiometrics.ibscanultimate.IBScanDevice.RollingData;
import com.integratedbiometrics.ibscanultimate.IBScanDevice.SegmentPosition;
import com.integratedbiometrics.ibscanultimate.IBScanDeviceListener;
import com.integratedbiometrics.ibscanultimate.IBScanException;
import com.integratedbiometrics.ibscanultimate.IBScanListener;
import com.nibbssdk.Constant;
import com.nibbssdk.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;


/**
 * Main activity for SimpleScan application.  Capture on a single connected scanner can be started
 * and stopped.  After an acquisition is complete, long-clicking on the small preview window will
 * allow the image to be e-mailed or show a larger view of the image.
 */
public class SimpleScanActivity extends Activity implements IBScanListener, IBScanDeviceListener
{
	/* *********************************************************************************************
	 * PRIVATE CONSTANTS
	 ******************************************************************************************** */


	/* The tag used for Android log messages from this app. */
	private static final String TAG                             = "Simple Scan";

	protected static final int 	__INVALID_POS__				  	= -1;

	/* The default value of the status TextView. */
	protected static final String __NFIQ_DEFAULT__                = "0-0-0-0";

	/* The default value of the frame time TextView. */
	protected static final String __NA_DEFAULT__              	= "n/a";

	/* The default file name for images and templates for e-mail. */
	protected static final String FILE_NAME_DEFAULT               = "output";


	/* The background color of the preview image ImageView. */
	protected static final int    PREVIEW_IMAGE_BACKGROUND         = Color.LTGRAY;

	/* The background color of a finger quality TextView when the finger is not present. */
	protected static final int    FINGER_QUALITY_NOT_PRESENT_COLOR = Color.LTGRAY;


	protected final int __TIMER_STATUS_DELAY__				= 500;

	// Capture sequence definitions
	protected final String CAPTURE_SEQ_FLAT_SINGLE_FINGER 				= "Single flat finger";

	// Beep definitions
	protected final int __BEEP_FAIL__						= 0;
	protected final int __BEEP_SUCCESS__					= 1;
	protected final int __BEEP_OK__							= 2;
	protected final int __BEEP_DEVICE_COMMUNICATION_BREAK__	= 3;

	// LED color definitions
	protected final int __LED_COLOR_NONE__		= 0;
	protected final int __LED_COLOR_GREEN__		= 1;
	protected final int __LED_COLOR_RED__		= 2;
	protected final int __LED_COLOR_YELLOW__	= 3;

	// Key button definitions
	protected final int __LEFT_KEY_BUTTON__		= 1;
	protected final int __RIGHT_KEY_BUTTON__	= 2;

	// Device Lock definitions
	protected final int DEVICE_LOCKED		= 1;
	protected final int DEVICE_UNLOCKED		= 2;
	protected final int DEVICE_KEY_INVALID	= 4;

	/* The number of finger segments set in the result image. */
	protected static final int    FINGER_SEGMENT_COUNT           = 4;

	/* *********************************************************************************************
	 * PRIVATE CLASSES
	 ******************************************************************************************** */

	/*
	 * This class wraps the data saved by the app for configuration changes.
	 */
	protected class AppData
	{
		/* The usb device currently selected. */
		public int                usbDevices                = __INVALID_POS__;

		/* The sequence of capture currently selected. */
		public int                captureSeq                = __INVALID_POS__;

		/* The current contents of the nfiq TextView. */
		public String             nfiq                      = __NFIQ_DEFAULT__;

		/* The current contents of the frame time TextView. */
		public String             frameTime                 = __NA_DEFAULT__;

		/* The current image displayed in the image preview ImageView. */
		public Bitmap             imageBitmap               = null;

		/* The current background colors of the finger quality TextViews. */
		public int[]              fingerQualityColors       = new int[]
				{FINGER_QUALITY_NOT_PRESENT_COLOR, FINGER_QUALITY_NOT_PRESENT_COLOR,
						FINGER_QUALITY_NOT_PRESENT_COLOR, FINGER_QUALITY_NOT_PRESENT_COLOR};

		/* Indicates whether the image preview ImageView can be long-clicked. */
		public boolean            imagePreviewImageClickable = false;

		/* The current contents of the overlayText TextView. */
		public String             overlayText				= "";

		/* The current contents of the overlay color for overlayText TextView. */
		public int             	  overlayColor					= PREVIEW_IMAGE_BACKGROUND;

		/* The current contents of the status message TextView. */
		public String             statusMessage             = __NA_DEFAULT__;
	}

	protected class CaptureInfo
	{
		String		PreCaptureMessage;		// to display on fingerprint window
		String		PostCaptuerMessage;		// to display on fingerprint window
		ImageType	ImageType;				// capture mode
		int			NumberOfFinger;			// number of finger count
		String		fingerName;				// finger name (e.g left thumbs, left index ... )
	};

	/* *********************************************************************************************
	 * PRIVATE FIELDS (UI COMPONENTS)
	 ******************************************************************************************** */

	private ImageView  m_imgPreview;
	private Bitmap	   m_BitmapImage;
	Bundle extras;
	TextView fingertext;
	private ImageView captureImage;
	SharedPreferences sharedpreferences;
	String fingerprintname = "";
	String fingerprintimage = "";
	/* *********************************************************************************************
	 * PRIVATE FIELDS
	 ******************************************************************************************** */

	/*
	 * A handle to the single instance of the IBScan class that will be the primary interface to
	 * the library, for operations like getting the number of scanners (getDeviceCount()) and
	 * opening scanners (openDeviceAsync()).
	 */
	private IBScan       m_ibScan;

	/*
	 * A handle to the open IBScanDevice (if any) that will be the interface for getting data from
	 * the open scanner, including capturing the image (beginCaptureImage(), cancelCaptureImage()),
	 * and the type of image being captured.
	 */
	private IBScanDevice m_ibScanDevice;


	/*
	 * Information retained to show view.
	 */
	private ImageData    m_lastResultImage;
	private ImageData[]    m_lastSegmentImages = new ImageData[FINGER_SEGMENT_COUNT];

	/*
	 * Information retained for orientation changes.
	 */
	private AppData      m_savedData = new AppData();

	protected int		m_nSelectedDevIndex = -1;				///< Index of selected device
	protected boolean	m_bInitializing = false;				///< Device initialization is in progress
	protected String 	m_ImgSaveFolderName = "";
	String				m_ImgSaveFolder = "";					///< Base folder for image saving
	String				m_ImgSubFolder = "";					///< Sub Folder for image sequence
	protected String 	m_strImageMessage = "";
	protected boolean 	m_bNeedClearPlaten = false;
	protected boolean 	m_bBlank = false;
	protected boolean	m_bSaveWarningOfClearPlaten;
	protected String    m_strCustomerKey = "";
	protected boolean   m_bisInputCustomerKey = false;

	protected Vector<CaptureInfo> m_vecCaptureSeq = new Vector<CaptureInfo>();		///< Sequence of capture steps
	protected int m_nCurrentCaptureStep = -1;					///< Current capture step

	protected LedState m_LedState;
	protected FingerQualityState[] m_FingerQuality = {FingerQualityState.FINGER_NOT_PRESENT, FingerQualityState.FINGER_NOT_PRESENT, FingerQualityState.FINGER_NOT_PRESENT, FingerQualityState.FINGER_NOT_PRESENT};
	protected ImageType	m_ImageType;
	protected int m_nSegmentImageArrayCount=0;
	protected SegmentPosition[] m_SegmentPositionArray;

//	protected ArrayList<String> m_arrCaptureSeq;

	protected byte[] m_drawBuffer;
	protected double m_scaleFactor;
	protected int m_leftMargin;
	protected int m_topMargin;

	protected String m_minSDKVersion = "";
	protected int m_nDeviceLockState = DEVICE_UNLOCKED;

	protected String m_strResSpoof = "";

	/* *********************************************************************************************
	 * INHERITED INTERFACE (Activity OVERRIDES)
	 ******************************************************************************************** */

	/*
	 * Called when the activity is started.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		m_ibScan = IBScan.getInstance(this.getApplicationContext());
		m_ibScan.setScanListener(this);

			setContentView(R.layout.activity_capturefingerprint);

		sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
		Constant.table_id = sharedpreferences.getString("table_id","");


//		/* Initialize UI fields. */

		m_imgPreview   = (ImageView) findViewById(R.id.fingerprintpreview);
		m_imgPreview.setBackgroundColor(PREVIEW_IMAGE_BACKGROUND);
		fingertext =  findViewById(R.id.fingername);
		extras = getIntent().getExtras();

			fingertext.setText(extras.getString("data")+" Finger.");
			//The key argument here must match that used in the other activity

		captureImage = findViewById(R.id.captureImage);
//        captureImage.setEnabled(false);
		captureImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!fingertext.getText().toString().equals("Right Thumb Finger.")) {
					startscanning();
//					in = new Intent(getApplicationContext(), SimpleScanActivity.class);
					switch (fingertext.getText().toString()) {
						case "Left Index Finger.":
							fingertext.setText("Left Middle Finger.");
							break;
						case "Left Middle Finger.":
							fingertext.setText("Left Ring Finger.");
							break;
						case "Left Ring Finger.":
							fingertext.setText("Left Little Finger.");
							break;
						case "Left Little Finger.":
							fingertext.setText("Right Index Finger.");
							break;
						case "Right Index Finger.":
							fingertext.setText("Right Middle Finger.");
							break;
						case "Right Middle Finger.":
							fingertext.setText("Right Ring Finger.");
							break;
						case "Right Ring Finger.":
							fingertext.setText("Right Little Finger.");
							break;
						case "Right Little Finger.":
							fingertext.setText("Left Thumb Finger.");
							break;
						case "Left Thumb Finger.":
							fingertext.setText("Right Thumb Finger.");
							break;
						case "Right Thumb Finger.":
							Intent in = new Intent(getApplicationContext(), FingerprintActivity.class);
							startActivity(in);
							break;

					}
				}else {
					SharedPreferences.Editor editor = sharedpreferences.edit();
					editor.putString("fingerprintimage",fingerprintimage);
					editor.putString("fingerprintname",fingerprintname);
					editor.apply();
					Intent in = new Intent(getApplicationContext(), FingerprintActivity.class);
					startActivity(in);
				}
			}
		});


		/*
		 * Make sure there are no USB devices attached that are IB scanners for which permission has
		 * not been granted.  For any that are found, request permission; we should receive a
		 * callback when permission is granted or denied and then when IBScan recognizes that new
		 * devices are connected, which will result in another refresh.
		 */
		final UsbManager manager        = (UsbManager)this.getApplicationContext().getSystemService(Context.USB_SERVICE);
		final HashMap<String, UsbDevice> deviceList     = manager.getDeviceList();
		final Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
		while (deviceIterator.hasNext())
		{
			final UsbDevice device       = deviceIterator.next();
			final boolean   isScanDevice = IBScan.isScanDevice(device);
			if (isScanDevice)
			{
				final boolean hasPermission = manager.hasPermission(device);
				if (!hasPermission)
				{
					this.m_ibScan.requestPermission(device.getDeviceId());
				}
			}
		}

		OnMsg_UpdateDeviceList(false);

		/* Initialize UI with data. */
		setSDKVersionInfo();




		if (m_savedData.imageBitmap != null)
		{
			m_imgPreview.setImageBitmap(m_savedData.imageBitmap);
		}

		m_imgPreview.setLongClickable(m_savedData.imagePreviewImageClickable);

		_TimerTaskThreadCallback thread = new _TimerTaskThreadCallback(__TIMER_STATUS_DELAY__);
		thread.start();
	}


	/*
	 * Release driver resources.
	 */
	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		for (int i = 0; i < 10; i++)
		{
			try
			{
				_ReleaseDevice();
				break;
			}
			catch (IBScanException ibse)
			{
				if (ibse.getType().equals(IBScanException.Type.RESOURCE_LOCKED))
				{
				}
				else
				{
					break;
				}
			}
		}
	}

//	@Override
//	public void onBackPressed()
//	{
//		exitApp(this);
//	}

	@Override
	public Object onRetainNonConfigurationInstance()
	{
		return null;
	}

	/* *********************************************************************************************
	 * PRIVATE METHODS
	 ******************************************************************************************** */

	// Get IBScan.
	protected IBScan getIBScan()
	{
		return (this.m_ibScan);
	}

	// Get opened or null IBScanDevice.
	protected IBScanDevice getIBScanDevice()
	{
		return (this.m_ibScanDevice);
	}

	// Set IBScanDevice.
	protected void setIBScanDevice(IBScanDevice ibScanDevice)
	{
		m_ibScanDevice = ibScanDevice;
		if (ibScanDevice != null)
		{
			ibScanDevice.setScanDeviceListener(this);
		}
	}

	/*
	 * Print log via Android Logcat
	 */
	protected void _Log(final String s)
	{
		Log.i("IBScan-Simple", s);
	}

	/*
	 * Set image overlay message text box.
	 */
	protected void _SetOverlayText(final String s, final int txtColor)
	{
		m_savedData.overlayText = s;
		m_savedData.overlayColor = txtColor;

		/* Make sure this occurs on the UI thread. */
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
			}
		});
	}

	/*
	 * Timer task with using Thread
	 */
	class _TimerTaskThreadCallback extends Thread
	{
		private int timeInterval;

		_TimerTaskThreadCallback(int timeInterval)
		{
			this.timeInterval = timeInterval;
		}

		@Override
		public void run()
		{
			while (!Thread.currentThread().isInterrupted()) {
				if (getIBScanDevice() != null) {
					OnMsg_DrawFingerQuality();

					if (m_bNeedClearPlaten)
						m_bBlank = !m_bBlank;
				}

				_Sleep(timeInterval);

				try {
					Thread.sleep(timeInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * Initialize Device with using Thread
	 */
	class _InitializeDeviceThreadCallback extends Thread
	{
		private int devIndex;

		_InitializeDeviceThreadCallback(int devIndex)
		{
			this.devIndex = devIndex;
		}

		@Override
		public void run()
		{
			try
			{
				m_bInitializing = true;

				if (m_nDeviceLockState == DEVICE_LOCKED || m_nDeviceLockState == DEVICE_KEY_INVALID)
				{
					if (m_strCustomerKey == "") // Value is null
					{
						m_bInitializing = false;
						return;
					}
					else // m_strCustomerKey has not null, Try to unlock
					{
						try {
							getIBScan().setCustomerKey(this.devIndex, HashType.SHA256, m_strCustomerKey);
						} catch (IBScanException ibse) {
						}
					}

				}

				IBScanDevice ibScanDeviceNew = getIBScan().openDevice(this.devIndex);
				setIBScanDevice(ibScanDeviceNew);
				m_bInitializing = false;

				if (ibScanDeviceNew != null)
				{
					//getProperty device Width,Height
	/*					String imageW = getIBScanDevice().getProperty(PropertyId.IMAGE_WIDTH);
						String imageH = getIBScanDevice().getProperty(PropertyId.IMAGE_HEIGHT);
						int	imageWidth = Integer.parseInt(imageW);
						int	imageHeight = Integer.parseInt(imageH);
	//					m_BitmapImage = _CreateBitmap(imageWidth, imageHeight);
	*/
					int outWidth = m_imgPreview.getWidth()-20;
					int outHeight = m_imgPreview.getHeight() - 20;
					m_BitmapImage = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
					m_drawBuffer = new byte[outWidth*outHeight*4];
					m_LedState = getIBScanDevice().getOperableLEDs();

					OnMsg_CaptureSeqStart();
				}
			}
			catch (IBScanException ibse)
			{
				m_bInitializing = false;
				 if (ibse.getType().equals(IBScanException.Type.DEVICE_HIGHER_SDK_REQUIRED))
				{
					try
					{
						m_minSDKVersion = getIBScan().getRequiredSDKVersion(this.devIndex);
					}
					catch (IBScanException ibse1)
					{
					}
				}

					/*
					else if (ibse.getType().equals(IBScanException.Type.DEVICE_LOCK_INVALID_BUFF))
					{
						_SetStatusBarMessage("[Error Code =-215] The Lock-info Buff is not valid.");
					}
					else if (ibse.getType().equals(IBScanException.Type.DEVICE_LOCK_INFO_EMPTY))
					{
						_SetStatusBarMessage("[Error Code =-216] The Lock-info Buff is empty.");
					}
					else if (ibse.getType().equals(IBScanException.Type.DEVICE_LOCK_INFO_NOT_MATCHED))
					{
						_SetStatusBarMessage("[Error Code =-217] The Customer Key to the devices is not registered.");
					}
					else if (ibse.getType().equals(IBScanException.Type.DEVICE_LOCK_INVALID_CHECKSUM))
					{
						_SetStatusBarMessage("[Error Code =-218] Checksums between buffer and calculated are different.");
					}
					else if (ibse.getType().equals(IBScanException.Type.DEVICE_LOCK_INVALID_KEY))
					{
						_SetStatusBarMessage("[Error Code =-219] The Customer key is not valid.");
						try {
							_ReleaseDevice();
						} catch (IBScanException e) {
							e.printStackTrace();
						}
					}
					else if (ibse.getType().equals(IBScanException.Type.DEVICE_LOCK_LOCKED))
					{
						_SetStatusBarMessage("[Error Code =-220] The device is locked. Customer Key is required");
					}
					else
					{
						_SetStatusBarMessage("Device initialization failed.");
					}
					*/

			}
		}
	}

	protected void _CalculateScaleFactors(ImageData image, int outWidth, int outHeight)
	{
		int left = 0, top = 0;
		int tmp_width = outWidth;
		int tmp_height = outHeight;
		int imgWidth = image.width;
		int imgHeight = image.height;
		int dispWidth, dispHeight, dispImgX, dispImgY;

		if (outWidth > imgWidth)
		{
			tmp_width = imgWidth;
			left = (outWidth-imgWidth)/2;
		}
		if (outHeight > imgHeight)
		{
			tmp_height = imgHeight;
			top = (outHeight-imgHeight)/2;
		}

		float ratio_width = (float)tmp_width / (float)imgWidth;
		float ratio_height = (float)tmp_height / (float)imgHeight;

		dispWidth = outWidth;
		dispHeight = outHeight;

		if (ratio_width >= ratio_height)
		{
			dispWidth = tmp_height * imgWidth / imgHeight;
			dispWidth -= (dispWidth % 4);
			dispHeight = tmp_height;
			dispImgX = (tmp_width - dispWidth) / 2 + left;
			dispImgY = top;
		}
		else
		{
			dispWidth = tmp_width;
			dispWidth -= (dispWidth % 4);
			dispHeight = tmp_width * imgHeight / imgWidth;
			dispImgX = left;
			dispImgY = (tmp_height - dispHeight) / 2 + top;
		}

		if (dispImgX < 0)
		{
			dispImgX = 0;
		}
		if (dispImgY < 0)
		{
			dispImgY = 0;
		}

		///////////////////////////////////////////////////////////////////////////////////
		m_scaleFactor = (double)dispWidth / image.width;
		m_leftMargin = dispImgX;
		m_topMargin = dispImgY;
		///////////////////////////////////////////////////////////////////////////////////
	}

	protected void _DrawOverlay_ImageText(Canvas canvas)
	{
/*
 * Draw text over bitmap image
 		Paint g = new Paint();
		g.setAntiAlias(true);
		if (m_bNeedClearPlaten)
			g.setColor(Color.RED);
		else
			g.setColor(Color.BLUE);
		g.setTypeface(Typeface.DEFAULT);
		g.setTextSize(20);
//		canvas.drawText(m_strImageMessage, 10, 20, g);
		canvas.drawText(m_strImageMessage, 20, 40, g);
*/

		/*
		 * Draw textview over imageview
		 */
		if (m_bNeedClearPlaten)
			_SetOverlayText(m_strImageMessage, Color.RED);
		else
			_SetOverlayText(m_strImageMessage, Color.BLUE);
	}

	protected void _DrawOverlay_WarningOfClearPlaten(Canvas canvas, int left, int top, int width, int height)
	{
		if (getIBScanDevice() == null)
			return;

		boolean idle = !m_bInitializing && ( m_nCurrentCaptureStep == -1 );

		if (!idle && m_bNeedClearPlaten && m_bBlank)
		{
			Paint g = new Paint();
			g.setStyle(Paint.Style.STROKE);
			g.setColor(Color.RED);
//			g.setStrokeWidth(10);
			g.setStrokeWidth(20);
			g.setAntiAlias(true);
			canvas.drawRect(left, top, width-1, height-1, g);
		}
	}

	protected void _DrawOverlay_ResultSegmentImage(Canvas canvas, ImageData image, int outWidth, int outHeight)
	{
		if (image.isFinal)
		{
//			if (m_chkDrawSegmentImage.isSelected())
			{
				// Draw quadrangle for the segment image

				_CalculateScaleFactors(image, outWidth, outHeight);
				Paint g = new Paint();
				g.setColor(Color.rgb(0, 128 ,0));
//				g.setStrokeWidth(1);
				g.setStrokeWidth(4);
				g.setAntiAlias(true);
				for (int i=0; i<m_nSegmentImageArrayCount; i++)
				{
					int x1, x2, x3, x4, y1, y2, y3, y4;
					x1 = m_leftMargin + (int)(m_SegmentPositionArray[i].x1*m_scaleFactor);
					x2 = m_leftMargin + (int)(m_SegmentPositionArray[i].x2*m_scaleFactor);
					x3 = m_leftMargin + (int)(m_SegmentPositionArray[i].x3*m_scaleFactor);
					x4 = m_leftMargin + (int)(m_SegmentPositionArray[i].x4*m_scaleFactor);
					y1 = m_topMargin +  (int)(m_SegmentPositionArray[i].y1*m_scaleFactor);
					y2 = m_topMargin +  (int)(m_SegmentPositionArray[i].y2*m_scaleFactor);
					y3 = m_topMargin +  (int)(m_SegmentPositionArray[i].y3*m_scaleFactor);
					y4 = m_topMargin +  (int)(m_SegmentPositionArray[i].y4*m_scaleFactor);

					canvas.drawLine(x1, y1, x2, y2, g);
					canvas.drawLine(x2, y2, x3, y3, g);
					canvas.drawLine(x3, y3, x4, y4, g);
					canvas.drawLine(x4, y4, x1, y1, g);
				}
			}
		}
	}

	protected void _DrawOverlay_RollGuideLine(Canvas canvas, ImageData image, int width, int height)
	{
		if (getIBScanDevice() == null || m_nCurrentCaptureStep == -1)
			return;

		if (m_ImageType == ImageType.ROLL_SINGLE_FINGER)
		{
			Paint g = new Paint();
			RollingData rollingdata;
			g.setAntiAlias(true);
			try
			{
				rollingdata = getIBScanDevice().getRollingInfo();

			}
			catch (IBScanException e)
			{
				rollingdata = null;
			}

			if ( (rollingdata != null) && rollingdata.rollingLineX > 0 &&
					(rollingdata.rollingState.equals(IBScanDevice.RollingState.TAKE_ACQUISITION) ||
							rollingdata.rollingState.equals(IBScanDevice.RollingState.COMPLETE_ACQUISITION)) )
			{
				_CalculateScaleFactors(image, width, height);
				int LineX = m_leftMargin + (int)(rollingdata.rollingLineX*m_scaleFactor);

				// Guide line for rolling
				if (rollingdata.rollingState.equals(IBScanDevice.RollingState.TAKE_ACQUISITION))
					g.setColor(Color.RED);
				else if (rollingdata.rollingState.equals(IBScanDevice.RollingState.COMPLETE_ACQUISITION))
					g.setColor(Color.GREEN);

				if (rollingdata.rollingLineX > -1)
				{
//					g.setStrokeWidth(2);
					g.setStrokeWidth(4);
					canvas.drawLine(LineX, 0, LineX, height, g);
				}
			}
		}
	}


	protected void _BeepFail()
	{
		try
		{
			IBScanDevice.BeeperType beeperType = getIBScanDevice().getOperableBeeper();
			if (beeperType != IBScanDevice.BeeperType.BEEPER_TYPE_NONE)
			{
				getIBScanDevice().setBeeper(IBScanDevice.BeepPattern.BEEP_PATTERN_GENERIC, 2/*Sol*/, 12/*300ms = 12*25ms*/, 0, 0);
				_Sleep(150);
				getIBScanDevice().setBeeper(IBScanDevice.BeepPattern.BEEP_PATTERN_GENERIC, 2/*Sol*/, 6/*150ms = 6*25ms*/, 0, 0);
				_Sleep(150);
				getIBScanDevice().setBeeper(IBScanDevice.BeepPattern.BEEP_PATTERN_GENERIC, 2/*Sol*/, 6/*150ms = 6*25ms*/, 0, 0);
				_Sleep(150);
				getIBScanDevice().setBeeper(IBScanDevice.BeepPattern.BEEP_PATTERN_GENERIC, 2/*Sol*/, 6/*150ms = 6*25ms*/, 0, 0);
			}
		}
		catch (IBScanException ibse)
		{
			// devices for without beep chip
			// send the tone to the "alarm" stream (classic beeps go there) with 30% volume
			ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 30);
			toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 300); // 300 is duration in ms
			_Sleep(300+150);
			toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 150); // 150 is duration in ms
			_Sleep(150+150);
			toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 150); // 150 is duration in ms
			_Sleep(150+150);
			toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 150); // 150 is duration in ms
		}
	}

	protected void _BeepSuccess()
	{
		try
		{
			IBScanDevice.BeeperType beeperType = getIBScanDevice().getOperableBeeper();
			if (beeperType != IBScanDevice.BeeperType.BEEPER_TYPE_NONE)
			{
				getIBScanDevice().setBeeper(IBScanDevice.BeepPattern.BEEP_PATTERN_GENERIC, 2/*Sol*/, 4/*100ms = 4*25ms*/, 0, 0);
				_Sleep(50);
				getIBScanDevice().setBeeper(IBScanDevice.BeepPattern.BEEP_PATTERN_GENERIC, 2/*Sol*/, 4/*100ms = 4*25ms*/, 0, 0);
			}
		}
		catch (IBScanException ibse)
		{
			// devices for without beep chip
			// send the tone to the "alarm" stream (classic beeps go there) with 30% volume
			ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 30);
			toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100); // 100 is duration in ms
			_Sleep(100+50);
			toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100); // 100 is duration in ms
		}
	}

	protected void _BeepOk()
	{
		try
		{
			IBScanDevice.BeeperType beeperType = getIBScanDevice().getOperableBeeper();
			if (beeperType != IBScanDevice.BeeperType.BEEPER_TYPE_NONE)
			{
				getIBScanDevice().setBeeper(IBScanDevice.BeepPattern.BEEP_PATTERN_GENERIC, 2/*Sol*/, 4/*100ms = 4*25ms*/, 0, 0);
			}
		}
		catch (IBScanException ibse)
		{
			// devices for without beep chip
			// send the tone to the "alarm" stream (classic beeps go there) with 30% volume
			ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 30);
			toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100); // 100 is duration in ms
		}
	}

	protected void _BeepDeviceCommunicationBreak()
	{
		for (int i=0; i<8; i++)
		{
			// send the tone to the "alarm" stream (classic beeps go there) with 30% volume
			ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 30);
			toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100); // 100 is duration in ms
			_Sleep(100+100);
		}
	}

	protected void _Sleep(int time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{
		}
	}

	protected void _SetTxtNFIQScore(final String s)
	{
		this.m_savedData.nfiq = s;

		/* Make sure this occurs on the UI thread. */
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
			}
		});
	}

	protected void _SetImageMessage(String s)
	{
		m_strImageMessage = s;
	}


	protected void _AddCaptureSeqVector(String PreCaptureMessage, String PostCaptuerMessage,
										ImageType imageType, int NumberOfFinger, String fingerName)
	{
		CaptureInfo info = new CaptureInfo();
		info.PreCaptureMessage = PreCaptureMessage;
		info.PostCaptuerMessage = PostCaptuerMessage;
		info.ImageType = imageType;
		info.NumberOfFinger = NumberOfFinger;
		info.fingerName = fingerName;
		m_vecCaptureSeq.addElement(info);
	}

	protected void _UpdateCaptureSequences()
	{
		try
		{


			final int devIndex = 0;
			IBScan.DeviceDesc devDesc = null;
			if (devIndex > -1)
			{
				devDesc = getIBScan().getDeviceDescription(devIndex);

			}

			if (devIndex >=0 )
			{
				if (devDesc.isLocked == true)
				{
					m_nDeviceLockState = DEVICE_LOCKED;
				}
				else // Unlocked
				{
					m_nDeviceLockState = DEVICE_UNLOCKED;
				}
			}
			else
			{
				m_nDeviceLockState = DEVICE_UNLOCKED;
			}



			new Handler().postDelayed(new Runnable() {
				public void run() {
					Log.e(TAG, "run: i'm running oooooooo" );

					m_savedData.captureSeq = 1;
					startscanning();

				}
			}, 100);



		}
		catch (IBScanException e)
		{
			e.printStackTrace();
		}
	}

	protected void _ReleaseDevice() throws IBScanException
	{
		if (getIBScanDevice() != null)
		{
			if (getIBScanDevice().isOpened() == true)
			{
				getIBScanDevice().close();
				setIBScanDevice(null);
			}
		}

		m_nCurrentCaptureStep = -1;
		m_bInitializing = false;
	}

	public void _SetLEDs(CaptureInfo info, int ledColor, boolean bBlink)
	{
		try
		{
			LedState ledState = getIBScanDevice().getOperableLEDs();
			if (ledState.ledCount == 0)
			{
				return;
			}
		}
		catch (IBScanException ibse)
		{
			ibse.printStackTrace();
		}

		int setLEDs = 0;

		if (ledColor == __LED_COLOR_NONE__)
		{
			try
			{
				getIBScanDevice().setLEDs(setLEDs);
			}
			catch (IBScanException ibse)
			{
				ibse.printStackTrace();
			}

			return;
		}

		if (m_LedState.ledType == IBScanDevice.LedType.FSCAN)
		{
			if (bBlink)
			{
				if (ledColor == __LED_COLOR_GREEN__)
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_BLINK_GREEN;
				}
				else if(ledColor == __LED_COLOR_RED__)
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_BLINK_RED;
				}
				else if(ledColor == __LED_COLOR_YELLOW__)
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_BLINK_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_BLINK_RED;
				}
			}

			if (info.ImageType == ImageType.ROLL_SINGLE_FINGER)
			{
				setLEDs |= IBScanDevice.IBSU_LED_F_PROGRESS_ROLL;
			}

			if( (info.fingerName.equals("SFF_Right_Thumb")) ||
					(info.fingerName.equals("SRF_Right_Thumb")) )
			{
				setLEDs |= IBScanDevice.IBSU_LED_F_PROGRESS_TWO_THUMB;
				if( ledColor == __LED_COLOR_GREEN__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_THUMB_GREEN;
				}
				else if( ledColor == __LED_COLOR_RED__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_THUMB_RED;
				}
				else if( ledColor == __LED_COLOR_YELLOW__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_THUMB_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_THUMB_RED;
				}
			}
			else if( (info.fingerName.equals("SFF_Left_Thumb")) ||
					(info.fingerName.equals("SRF_Left_Thumb")) )
			{
				setLEDs |= IBScanDevice.IBSU_LED_F_PROGRESS_TWO_THUMB;
				if( ledColor == __LED_COLOR_GREEN__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_THUMB_GREEN;
				}
				else if( ledColor == __LED_COLOR_RED__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_THUMB_RED;
				}
				else if( ledColor == __LED_COLOR_YELLOW__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_THUMB_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_THUMB_RED;
				}
			}
			else if( (info.fingerName.equals("TFF_2_Thumbs")) )
			{
				setLEDs |= IBScanDevice.IBSU_LED_F_PROGRESS_TWO_THUMB;
				if( ledColor == __LED_COLOR_GREEN__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_THUMB_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_THUMB_GREEN;
				}
				else if( ledColor == __LED_COLOR_RED__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_THUMB_RED;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_THUMB_RED;
				}
				else if( ledColor == __LED_COLOR_YELLOW__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_THUMB_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_THUMB_RED;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_THUMB_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_THUMB_RED;
				}
			}
			///////////////////LEFT HAND////////////////////
			else if( (info.fingerName.equals("SFF_Left_Index")) ||
					(info.fingerName.equals("SRF_Left_Index")) )
			{
				setLEDs |= IBScanDevice.IBSU_LED_F_PROGRESS_LEFT_HAND;
				if( ledColor == __LED_COLOR_GREEN__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_INDEX_GREEN;
				}
				else if( ledColor == __LED_COLOR_RED__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_INDEX_RED;
				}
				else if( ledColor == __LED_COLOR_YELLOW__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_INDEX_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_INDEX_RED;
				}
			}
			else if( (info.fingerName.equals("SFF_Left_Middle")) ||
					(info.fingerName.equals("SRF_Left_Middle")) )
			{
				setLEDs |= IBScanDevice.IBSU_LED_F_PROGRESS_LEFT_HAND;
				if( ledColor == __LED_COLOR_GREEN__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_MIDDLE_GREEN;
				}
				else if( ledColor == __LED_COLOR_RED__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_MIDDLE_RED;
				}
				else if( ledColor == __LED_COLOR_YELLOW__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_MIDDLE_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_MIDDLE_RED;
				}
			}
			else if( (info.fingerName.equals("SFF_Left_Ring")) ||
					(info.fingerName.equals("SRF_Left_Ring")) )
			{
				setLEDs |= IBScanDevice.IBSU_LED_F_PROGRESS_LEFT_HAND;
				if( ledColor == __LED_COLOR_GREEN__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_RING_GREEN;
				}
				else if( ledColor == __LED_COLOR_RED__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_RING_RED;
				}
				else if( ledColor == __LED_COLOR_YELLOW__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_RING_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_RING_RED;
				}
			}
			else if( (info.fingerName.equals("SFF_Left_Little")) ||
					(info.fingerName.equals("SRF_Left_Little")) )
			{
				setLEDs |= IBScanDevice.IBSU_LED_F_PROGRESS_LEFT_HAND;
				if( ledColor == __LED_COLOR_GREEN__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_LITTLE_GREEN;
				}
				else if( ledColor == __LED_COLOR_RED__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_LITTLE_RED;
				}
				else if( ledColor == __LED_COLOR_YELLOW__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_LITTLE_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_LITTLE_RED;
				}
			}
			else if( (info.fingerName.equals("4FF_Left_4_Fingers")) )
			{
				setLEDs |= IBScanDevice.IBSU_LED_F_PROGRESS_LEFT_HAND;
				if( ledColor == __LED_COLOR_GREEN__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_INDEX_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_MIDDLE_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_RING_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_LITTLE_GREEN;
				}
				else if( ledColor == __LED_COLOR_RED__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_INDEX_RED;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_MIDDLE_RED;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_RING_RED;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_LITTLE_RED;
				}
				else if( ledColor == __LED_COLOR_YELLOW__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_INDEX_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_MIDDLE_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_RING_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_LITTLE_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_INDEX_RED;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_MIDDLE_RED;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_RING_RED;
					setLEDs |= IBScanDevice.IBSU_LED_F_LEFT_LITTLE_RED;
				}
			}
			///////////RIGHT HAND /////////////////////////
			else if( (info.fingerName.equals("SFF_Right_Index")) ||
					(info.fingerName.equals("SRF_Right_Index")) )
			{
				setLEDs |= IBScanDevice.IBSU_LED_F_PROGRESS_RIGHT_HAND;
				if( ledColor == __LED_COLOR_GREEN__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_INDEX_GREEN;
				}
				else if( ledColor == __LED_COLOR_RED__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_INDEX_RED;
				}
				else if( ledColor == __LED_COLOR_YELLOW__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_INDEX_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_INDEX_RED;
				}
			}
			else if( (info.fingerName.equals("SFF_Right_Middle")) ||
					(info.fingerName.equals("SRF_Right_Middle")) )
			{
				setLEDs |= IBScanDevice.IBSU_LED_F_PROGRESS_RIGHT_HAND;
				if( ledColor == __LED_COLOR_GREEN__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_MIDDLE_GREEN;
				}
				else if( ledColor == __LED_COLOR_RED__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_MIDDLE_RED;
				}
				else if( ledColor == __LED_COLOR_YELLOW__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_MIDDLE_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_MIDDLE_RED;
				}
			}
			else if( (info.fingerName.equals("SFF_Right_Ring")) ||
					(info.fingerName.equals("SRF_Right_Ring")) )
			{
				setLEDs |= IBScanDevice.IBSU_LED_F_PROGRESS_RIGHT_HAND;
				if( ledColor == __LED_COLOR_GREEN__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_RING_GREEN;
				}
				else if( ledColor == __LED_COLOR_RED__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_RING_RED;
				}
				else if( ledColor == __LED_COLOR_YELLOW__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_RING_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_RING_RED;
				}
			}
			else if( (info.fingerName.equals("SFF_Right_Little")) ||
					(info.fingerName.equals("SRF_Right_Little")) )
			{
				setLEDs |= IBScanDevice.IBSU_LED_F_PROGRESS_RIGHT_HAND;
				if( ledColor == __LED_COLOR_GREEN__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_LITTLE_GREEN;
				}
				else if( ledColor == __LED_COLOR_RED__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_LITTLE_RED;
				}
				else if( ledColor == __LED_COLOR_YELLOW__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_LITTLE_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_LITTLE_RED;
				}
			}
			else if( (info.fingerName.equals("4FF_Right_4_Fingers")) )
			{
				setLEDs |= IBScanDevice.IBSU_LED_F_PROGRESS_RIGHT_HAND;
				if( ledColor == __LED_COLOR_GREEN__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_INDEX_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_MIDDLE_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_RING_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_LITTLE_GREEN;
				}
				else if( ledColor == __LED_COLOR_RED__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_INDEX_RED;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_MIDDLE_RED;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_RING_RED;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_LITTLE_RED;
				}
				else if( ledColor == __LED_COLOR_YELLOW__ )
				{
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_INDEX_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_MIDDLE_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_RING_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_LITTLE_GREEN;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_INDEX_RED;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_MIDDLE_RED;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_RING_RED;
					setLEDs |= IBScanDevice.IBSU_LED_F_RIGHT_LITTLE_RED;
				}
			}

			if (ledColor == __LED_COLOR_NONE__)
			{
				setLEDs = 0;
			}

			try
			{
				getIBScanDevice().setLEDs(setLEDs);
			}
			catch (IBScanException ibse)
			{
				ibse.printStackTrace();
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	// Event-dispatch threads
	private void OnMsg_SetStatusBarMessage(final String s)
	{
		runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
//				_SetStatusBarMessage(s);
			}
		});
	}


	private void OnMsg_SetTxtNFIQScore(final String s)
	{
		runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				_SetTxtNFIQScore(s);
			}
		});
	}


	private void OnMsg_Beep(final int beepType)
	{
		runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				if( beepType == __BEEP_FAIL__ )
					_BeepFail();
				else if( beepType == __BEEP_SUCCESS__ )
					_BeepSuccess();
				else if( beepType == __BEEP_OK__ )
					_BeepOk();
				else if( beepType == __BEEP_DEVICE_COMMUNICATION_BREAK__ )
					_BeepDeviceCommunicationBreak();
			}
		});
	}

	private void OnMsg_CaptureSeqStart()
	{
		runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				if (getIBScanDevice() == null)
				{
					return;
				}


				m_vecCaptureSeq.clear();

/** Please refer to definition below
 protected final String CAPTURE_SEQ_FLAT_SINGLE_FINGER 				= "Single flat finger";
 protected final String CAPTURE_SEQ_ROLL_SINGLE_FINGER 				= "Single rolled finger";
 protected final String CAPTURE_SEQ_2_FLAT_FINGERS 					= "2 flat fingers";
 protected final String CAPTURE_SEQ_10_SINGLE_FLAT_FINGERS 			= "10 single flat fingers";
 protected final String CAPTURE_SEQ_10_SINGLE_ROLLED_FINGERS 		= "10 single rolled fingers";
 protected final String CAPTURE_SEQ_4_FLAT_FINGERS 					= "4 flat fingers";
 protected final String CAPTURE_SEQ_10_FLAT_WITH_4_FINGER_SCANNER 	= "10 flat fingers with 4-finger scanner";
 */
			_AddCaptureSeqVector("Please put a single finger on the sensor!",
							"Keep finger on the sensor!",
							ImageType.FLAT_SINGLE_FINGER,
							1,
							"SFF_Unknown");





				OnMsg_CaptureSeqNext();
			}
		});
	}

	private void OnMsg_CaptureSeqNext()
	{
		runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				if (getIBScanDevice() == null)
					return;

				m_bBlank = false;
				for (int i=0; i<4; i++)
					m_FingerQuality[i] = FingerQualityState.FINGER_NOT_PRESENT;

				m_nCurrentCaptureStep++;
				if (m_nCurrentCaptureStep >= m_vecCaptureSeq.size())
				{
					// All of capture sequence completely
					CaptureInfo tmpInfo = new CaptureInfo();
					_SetLEDs(tmpInfo, __LED_COLOR_NONE__, false);
					m_nCurrentCaptureStep = -1;

					return;
				}

				try
				{
/*					if (m_chkDetectSmear.isSelected())
					{
						getIBScanDevice().setProperty(IBScanDevice.PropertyId.ROLL_MODE, "1");
						String strValue = String.valueOf(m_cboSmearLevel.getSelectedIndex());
						getIBScanDevice().setProperty(IBScanDevice.PropertyId.ROLL_LEVEL, strValue);
			}
			else
			{
						getIBScanDevice().setProperty(IBScanDevice.PropertyId.ROLL_MODE, "0");
					}
*/

					// Make capture delay for display result image on multi capture mode (500 ms)
					if (m_nCurrentCaptureStep > 0)
					{
						_Sleep(500);
						m_strImageMessage = "";
					}

					CaptureInfo info = m_vecCaptureSeq.elementAt(m_nCurrentCaptureStep);

					IBScanDevice.ImageResolution imgRes = IBScanDevice.ImageResolution.RESOLUTION_500;
					boolean bAvailable = getIBScanDevice().isCaptureAvailable(info.ImageType, imgRes);
					if (!bAvailable)
					{
//						_SetStatusBarMessage("The capture mode (" + info.ImageType + ") is not available");
						m_nCurrentCaptureStep = -1;
						return;
					}

					// Start capture
					int captureOptions = 0;
//					if (m_chkAutoContrast.isSelected())
					captureOptions |= IBScanDevice.OPTION_AUTO_CONTRAST;
//					if (m_chkAutoCapture.isSelected())
					captureOptions |= IBScanDevice.OPTION_AUTO_CAPTURE;
//					if (m_chkIgnoreFingerCount.isSelected())
					captureOptions |= IBScanDevice.OPTION_IGNORE_FINGER_COUNT;

					getIBScanDevice().beginCaptureImage(info.ImageType, imgRes, captureOptions);

					String strMessage = info.PreCaptureMessage;
//					_SetStatusBarMessage(strMessage);
//					if (!m_chkAutoCapture.isSelected())
//						strMessage += "\r\nPress button 'Take Result Image' when image is good!";

					_SetImageMessage(strMessage);
					m_strImageMessage = strMessage;

					m_ImageType = info.ImageType;

					_SetLEDs(info, __LED_COLOR_RED__, true);

				}
				catch (IBScanException ibse)
				{
					ibse.printStackTrace();
					m_nCurrentCaptureStep = -1;
				}
			}
		});
	}

	private void OnMsg_cboUsbDevice_Changed()
	{
		runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				if (m_nSelectedDevIndex == 1)
					return;

				m_nSelectedDevIndex = 1;
				if (getIBScanDevice() != null)
				{
					try
					{
						_ReleaseDevice();
					}
					catch (IBScanException ibse)
					{
						ibse.printStackTrace();
					}
				}

				_UpdateCaptureSequences();
			}
		});
	}

	private void OnMsg_UpdateDeviceList(final boolean bConfigurationChanged)
	{
		runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				try
				{
					boolean idle = (!m_bInitializing && (m_nCurrentCaptureStep == -1)) ||
							(bConfigurationChanged);


					//store currently selected device
					String strSelectedText = "";
					int selectedDev = 1;
					if (selectedDev > -1)
						strSelectedText = "tolu finger print";

					// populate combo box
					int devices = getIBScan().getDeviceCount();
					IBScan.DeviceDesc devDesc = getIBScan().getDeviceDescription(0);
					String strDevice;
					strDevice = devDesc.productName + "_v"+ devDesc.fwVersion + " (" + devDesc.serialNumber + ")";
					Log.e(TAG, "run: gfhjguytdrsxrty "+ strDevice );

					if (idle)
					{
						OnMsg_cboUsbDevice_Changed();
						_UpdateCaptureSequences();
					}

				} catch (IBScanException e)	{
					e.printStackTrace();
				}
			}
		});
	}



	private void OnMsg_AskRecapture(final IBScanException imageStatus)
	{
		runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				String askMsg;

				askMsg = "[Warning = " + imageStatus.getType().toString() + "] Do you want a recapture?";

				AlertDialog.Builder dlgAskRecapture = new AlertDialog.Builder(SimpleScanActivity.this);
				dlgAskRecapture.setMessage(askMsg);
				dlgAskRecapture.setPositiveButton("Yes",
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								// To recapture current finger position
								m_nCurrentCaptureStep--;
								OnMsg_CaptureSeqNext();
							}
						});
				dlgAskRecapture.setNegativeButton("No",
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								OnMsg_CaptureSeqNext();
							}
						});

				dlgAskRecapture.show();
			}
		});
	}


	private void OnMsg_DeviceCommunicationBreak()
	{
		runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				if (getIBScanDevice() == null)
					return;

				try
				{
					_ReleaseDevice();

					OnMsg_Beep(__BEEP_DEVICE_COMMUNICATION_BREAK__);
					OnMsg_UpdateDeviceList(false);
				}
				catch (IBScanException ibse)
				{
					if (ibse.getType().equals(IBScanException.Type.RESOURCE_LOCKED))
					{
						OnMsg_DeviceCommunicationBreak();
					}
				}
			}
		});
	}

	private void OnMsg_DrawImage(final IBScanDevice device, final ImageData image)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				int destWidth = m_imgPreview.getWidth()-20;
				int destHeight = m_imgPreview.getHeight()-20;
//				int outImageSize = destWidth * destHeight;

				try
				{
					if (destHeight <= 0 || destWidth <=0 )
						return;

					if (destWidth != m_BitmapImage.getWidth() || destHeight != m_BitmapImage.getHeight())
					{
						// if image size is changed (e.g changed capture type from flat to rolled finger)
						// Create bitmap again
						m_BitmapImage = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888);
						m_drawBuffer = new byte[destWidth*destHeight*4];
					}

					if (image.isFinal)
					{
						getIBScanDevice().generateDisplayImage(image.buffer, image.width, image.height,
								m_drawBuffer, destWidth, destHeight, (byte) 255, 2 /*IBSU_IMG_FORMAT_RGB32*/, 2 /*HIGH QUALITY*/, true);
/*						for (int i=0; i<destWidth*destHeight; i++)
					{
							if (m_drawBuffer[i] != -1) {
								OnMsg_Beep(__BEEP_OK__);
						break;
					}
						}
*/					}
					else
					{
						getIBScanDevice().generateDisplayImage(image.buffer, image.width, image.height,
								m_drawBuffer, destWidth, destHeight, (byte) 255, 2 /*IBSU_IMG_FORMAT_RGB32*/, 0 /*LOW QUALITY*/, true);
					}
				}
				catch (IBScanException e)
				{
					e.printStackTrace();
				}

				m_BitmapImage.copyPixelsFromBuffer(ByteBuffer.wrap(m_drawBuffer));
				// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				Canvas canvas = new Canvas(m_BitmapImage);

				_DrawOverlay_ImageText(canvas);
				_DrawOverlay_WarningOfClearPlaten(canvas, 0, 0, destWidth, destHeight);
				_DrawOverlay_ResultSegmentImage(canvas, image, destWidth, destHeight);
				_DrawOverlay_RollGuideLine(canvas, image, destWidth, destHeight);
/*				_DrawOverlay_WarningOfClearPlaten(canvas, 0, 0, image.width, image.height);
				_DrawOverlay_ResultSegmentImage(canvas, image, image.width, image.height);
				_DrawOverlay_RollGuideLine(canvas, image, image.width, image.height);
			 */
				m_savedData.imageBitmap = m_BitmapImage;
				m_imgPreview.setImageBitmap(m_BitmapImage);
			}
		});
	}

	private void OnMsg_DrawFingerQuality()
	{
		runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				// Update value in fingerQuality field and flash button.
				for (int i = 0; i < 4; i++)
				{
					int color;
					if (m_FingerQuality[i] == FingerQualityState.GOOD)
						color = Color.rgb(0, 128, 0);
					else if (m_FingerQuality[i] == FingerQualityState.FAIR)
						color = Color.rgb(255, 128, 0);
					else if (m_FingerQuality[i] == FingerQualityState.POOR ||
							m_FingerQuality[i] == FingerQualityState.INVALID_AREA_TOP ||
							m_FingerQuality[i] == FingerQualityState.INVALID_AREA_BOTTOM ||
							m_FingerQuality[i] == FingerQualityState.INVALID_AREA_LEFT ||
							m_FingerQuality[i] == FingerQualityState.INVALID_AREA_RIGHT
					)
						color = Color.rgb(255, 0, 0);
					else
						color = Color.LTGRAY;

					m_savedData.fingerQualityColors[i] = color;
				}
			}
		});
	}

	/*
	 * Show Toast message on UI thread.
	 */
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

	/*
	 * Set SDK version in SDK version text field.
	 */
	private void setSDKVersionInfo()
	{
		String txtValue;

		try
		{
			SdkVersion sdkVersion;

			sdkVersion = m_ibScan.getSdkVersion();
			txtValue   = "SDK version: " + sdkVersion.file;
		}
		catch (IBScanException ibse)
		{
			txtValue = "(failure)";
		}

		/* Make sure this occurs on the UI thread. */
		final String txtValueTemp = txtValue;
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
			}
		});
	}


	/*
	 * Set frame time in frame time field.  Save value for orientation change.
	 */
	private void setFrameTime(final String s)
	{
		m_savedData.frameTime = s;

		/* Make sure this occurs on the UI thread. */
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
			}
		});
	}

	/*
	 * Compress the image and attach it to an e-mail using an installed e-mail client.
	 */
	private void sendImageInEmail(final ImageData imageData, final String fileName)
	{
		boolean created = false;
		ArrayList ur = new ArrayList();
		try
		{
			String MainPNGfilename = Environment.getExternalStorageDirectory().getPath() + "/" + fileName + ".png";
			String MainBMPfilename = Environment.getExternalStorageDirectory().getPath() + "/" + fileName + ".bmp";
			String MainJP2filename = Environment.getExternalStorageDirectory().getPath() + "/" + fileName + ".jp2";
			String MainWSQfilename = Environment.getExternalStorageDirectory().getPath() + "/" + fileName + ".wsq";

			getIBScanDevice().SavePngImage(MainPNGfilename,imageData.buffer, imageData.width, imageData.height,imageData.pitch,imageData.resolutionX,imageData.resolutionY);
			getIBScanDevice().SaveBitmapImage(MainBMPfilename,imageData.buffer, imageData.width, imageData.height,imageData.pitch,imageData.resolutionX,imageData.resolutionY);
			getIBScanDevice().SaveJP2Image(MainJP2filename,imageData.buffer, imageData.width, imageData.height,imageData.pitch,imageData.resolutionX,imageData.resolutionY,80);
			getIBScanDevice().wsqEncodeToFile(MainWSQfilename,imageData.buffer, imageData.width, imageData.height, imageData.pitch, imageData.bitsPerPixel, 500, 0.75, "");

			ur.add(Uri.fromFile(new File(MainPNGfilename)));
			ur.add(Uri.fromFile(new File(MainBMPfilename)));
			ur.add(Uri.fromFile(new File(MainJP2filename)));
			ur.add(Uri.fromFile(new File(MainWSQfilename)));

			for (int i=0; i<m_nSegmentImageArrayCount; i++)
			{
				String SegmentPNGfilename = Environment.getExternalStorageDirectory().getPath() + "/segment_" +i + "_"+ fileName + ".png";
				String SegmentBMPfilename = Environment.getExternalStorageDirectory().getPath() + "/segment_" +i + "_"+ fileName + ".bmp";
				String SegmentJP2filename = Environment.getExternalStorageDirectory().getPath() + "/segment_" +i + "_"+ fileName + ".jp2";
				String SegmentWSQfilename = Environment.getExternalStorageDirectory().getPath() + "/segment_" +i + "_"+ fileName + ".wsq";

				try
				{
					getIBScanDevice().SavePngImage(SegmentPNGfilename,m_lastSegmentImages[i].buffer, m_lastSegmentImages[i].width, m_lastSegmentImages[i].height,m_lastSegmentImages[i].pitch,m_lastSegmentImages[i].resolutionX,m_lastSegmentImages[i].resolutionY);
					getIBScanDevice().SaveBitmapImage(SegmentBMPfilename,m_lastSegmentImages[i].buffer, m_lastSegmentImages[i].width, m_lastSegmentImages[i].height,m_lastSegmentImages[i].pitch,m_lastSegmentImages[i].resolutionX,m_lastSegmentImages[i].resolutionY);
					getIBScanDevice().SaveJP2Image(SegmentJP2filename,m_lastSegmentImages[i].buffer, m_lastSegmentImages[i].width, m_lastSegmentImages[i].height,m_lastSegmentImages[i].pitch,m_lastSegmentImages[i].resolutionX,m_lastSegmentImages[i].resolutionY,80);
					getIBScanDevice().wsqEncodeToFile(SegmentWSQfilename,m_lastSegmentImages[i].buffer, m_lastSegmentImages[i].width, m_lastSegmentImages[i].height, m_lastSegmentImages[i].pitch, m_lastSegmentImages[i].bitsPerPixel, 500, 0.75, "");

					ur.add(Uri.fromFile(new File(SegmentPNGfilename)));
					ur.add(Uri.fromFile(new File(SegmentBMPfilename)));
					ur.add(Uri.fromFile(new File(SegmentJP2filename)));
					ur.add(Uri.fromFile(new File(SegmentWSQfilename)));
				}
				catch (IBScanException e)
				{
					Toast.makeText(getApplicationContext(), "Could not create image for e-mail", Toast.LENGTH_LONG).show();
				}
			}

			created = true;
		}
		catch (IBScanException e)
		{
			Toast.makeText(getApplicationContext(), "Could not create image for e-mail", Toast.LENGTH_LONG).show();
		}


		/* If file was created, send the e-mail. */
		if (created)
		{
			attachAndSendEmail(ur, "Fingerprint Image", fileName);
		}
	}

	/*
	 * Attach file to e-mail and send.
	 */
	private void attachAndSendEmail(final ArrayList ur, final String subject, final String message)
	{
		final Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL,   new String[] { "" });
		i.putExtra(Intent.EXTRA_SUBJECT, subject);
//		i.putExtra(Intent.EXTRA_STREAM,  ur);
		i.putParcelableArrayListExtra(Intent.EXTRA_STREAM,  ur);
		i.putExtra(Intent.EXTRA_TEXT,    message);

		try
		{
			startActivity(Intent.createChooser(i, "Send mail..."));
		}
		catch (ActivityNotFoundException anfe)
		{
			showToastOnUiThread("There are no e-mail clients installed", Toast.LENGTH_LONG);
		}
	}

	/*
	 * Prompt to send e-mail with image.
	 */
//	private void promptForEmail(final ImageData imageData)
//	{
//		/* The dialog must be shown from the UI thread. */
//		runOnUiThread(new Runnable()
//		{
//			@Override
//			public void run()
//			{
//				final LayoutInflater      inflater     = getLayoutInflater();
//				final View                fileNameView = inflater.inflate(R.layout.file_name_dialog, null);
//				final AlertDialog.Builder builder      = new AlertDialog.Builder(SimpleScanActivity.this)
//						.setView(fileNameView)
//						.setTitle("Enter file name")
//						.setPositiveButton("OK", new DialogInterface.OnClickListener()
//						{
//							@Override
//							public void onClick(final DialogInterface dialog, final int which)
//							{
//								final EditText text     = (EditText) fileNameView.findViewById(R.id.file_name);
//								final String   fileName = text.getText().toString();
//
//								/* E-mail image in background thread. */
//								Thread threadEmail = new Thread()
//								{
//									@Override
//									public void run()
//									{
//										sendImageInEmail(imageData, fileName);
//									}
//								};
//								threadEmail.start();
//							}
//						})
//						.setNegativeButton("Cancel", null);
//				EditText text = (EditText) fileNameView.findViewById(R.id.file_name);
//				text.setText(FILE_NAME_DEFAULT);
//
//				builder.create().show();
//			}
//		});
//	}

	/*
	 * Show CustomerKey Input dialog
	 */
	private void showCustomerKeyDialog(Context c) {
		final EditText taskEditText = new EditText(c);
		AlertDialog dialog = new AlertDialog.Builder(c)
				.setTitle("Need to Customer key")
				.setMessage("Enter your Customer key")
				.setCancelable(false)
				.setView(taskEditText)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						m_strCustomerKey = String.valueOf(taskEditText.getText());
						// Go to Device initialize thread
						_deviceOpenStart();

					}
				})
				//.setNegativeButton("Cancel", null)
				.create();
		dialog.show();
	}


	/*
	 * Exit application.
	 */
	private static void exitApp(Activity ac)
	{
		ac.moveTaskToBack(true);
		ac.finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/*
	 * Resume from Customer Key input dialog
	 */
	private void _deviceOpenStart()
	{
		if (getIBScanDevice() == null)
		{
			m_bInitializing = true;

			_InitializeDeviceThreadCallback thread = new _InitializeDeviceThreadCallback(m_nSelectedDevIndex - 1);
			thread.start();
		}
		else
		{
			OnMsg_CaptureSeqStart();
		}


	}



	/* *********************************************************************************************
	 * EVENT HANDLERS
	 ******************************************************************************************** */

	void startscanning(){
		if (m_bInitializing)
			return;


		if (m_nCurrentCaptureStep != -1)
		{
			try
			{
				boolean IsActive = getIBScanDevice().isCaptureActive();
				if (IsActive)
				{
					// Capture image manually for active device
					getIBScanDevice().captureImageManually();
					return;
				}
			}
			catch (IBScanException ibse)
			{
//				_SetStatusBarMessage("IBScanDevice.takeResultImageManually() returned exception "
//						+ ibse.getType().toString() + ".");
			}
		}

		if (getIBScanDevice() == null)
		{
			m_bInitializing = true;

			if (m_nDeviceLockState == DEVICE_LOCKED || m_nDeviceLockState == DEVICE_KEY_INVALID)
			{
				showCustomerKeyDialog(SimpleScanActivity.this);
				return; // Exit this function for Input dialog problem
			}

			_InitializeDeviceThreadCallback thread = new _InitializeDeviceThreadCallback(m_nSelectedDevIndex - 1);
			thread.start();
		}
		else
		{
			OnMsg_CaptureSeqStart();
		}

	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC INTERFACE: IBScanListener METHODS
	// //////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void scanDeviceAttached(final int deviceId)
	{
		showToastOnUiThread("Device " + deviceId + " attached", Toast.LENGTH_SHORT);

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
		OnMsg_UpdateDeviceList(false);
	}

	@Override
	public void scanDeviceInitProgress(final int deviceIndex, final int progressValue)
	{
		OnMsg_SetStatusBarMessage("Initializing device..."+ progressValue + "%");
	}

	@Override
	public void scanDeviceOpenComplete(final int deviceIndex, final IBScanDevice device,
									   final IBScanException exception)
	{
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC INTERFACE: IBScanDeviceListener METHODS
	// //////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void deviceCommunicationBroken(final IBScanDevice device)
	{
		OnMsg_DeviceCommunicationBreak();
	}

	@Override
	public void deviceImagePreviewAvailable(final IBScanDevice device, final ImageData image)
	{
		setFrameTime(String.format("%1$.3f ms", image.frameTime*1000));
		OnMsg_DrawImage(device, image);
	}

	@Override
	public void deviceFingerCountChanged(final IBScanDevice device, final FingerCountState fingerState)
	{
		if (m_nCurrentCaptureStep >= 0)
		{
			CaptureInfo info = m_vecCaptureSeq.elementAt(m_nCurrentCaptureStep);
			if (fingerState == FingerCountState.NON_FINGER)
			{
				_SetLEDs(info, __LED_COLOR_RED__, true);
			}
			else
			{
				_SetLEDs(info, __LED_COLOR_YELLOW__, true);
			}
		}
	}

	@Override
	public void deviceFingerQualityChanged(final IBScanDevice device, final FingerQualityState[] fingerQualities)
	{
		for (int i=0; i<fingerQualities.length; i++)
		{
			m_FingerQuality[i] = fingerQualities[i];
		}

		OnMsg_DrawFingerQuality();
	}

	@Override
	public void deviceAcquisitionBegun(final IBScanDevice device, final ImageType imageType)
	{
		if (imageType.equals(ImageType.ROLL_SINGLE_FINGER))
		{
			OnMsg_Beep(__BEEP_OK__);
			m_strImageMessage = "When done remove finger from sensor";
			_SetImageMessage(m_strImageMessage);
		}
	}

	@Override
	public void deviceAcquisitionCompleted(final IBScanDevice device, final ImageType imageType)
	{
		if (imageType.equals(ImageType.ROLL_SINGLE_FINGER))
		{
			OnMsg_Beep(__BEEP_OK__);
		}
		else
		{
			OnMsg_Beep(__BEEP_SUCCESS__);
			_SetImageMessage("Remove fingers from sensor");
		}
	}

	@Override
	public void deviceImageResultAvailable(final IBScanDevice device, final ImageData image,
										   final ImageType imageType, final ImageData[] splitImageArray)
	{
		/* TODO: ALTERNATIVELY, USE RESULTS IN THIS FUNCTION */
	}

	@Override
	public void deviceImageResultExtendedAvailable(IBScanDevice device, IBScanException imageStatus,
												   final ImageData image, final ImageType imageType, final int detectedFingerCount,
												   final ImageData[] segmentImageArray, final SegmentPosition[] segmentPositionArray)
	{
		setFrameTime(String.format("%1$.3f ms", image.frameTime*1000));

		m_savedData.imagePreviewImageClickable = true;
		m_imgPreview.setLongClickable(true);
		m_lastResultImage = image;
		m_lastSegmentImages = segmentImageArray;

		// imageStatus value is greater than "STATUS_OK", Image acquisition successful.
		if (imageStatus == null /*STATUS_OK*/ ||
				imageStatus.getType().compareTo(IBScanException.Type.INVALID_PARAM_VALUE) > 0)
		{
			if (imageType.equals(ImageType.ROLL_SINGLE_FINGER))
			{
				OnMsg_Beep(__BEEP_SUCCESS__);
			}
		}

		if (m_bNeedClearPlaten)
		{
			m_bNeedClearPlaten = false;
			OnMsg_DrawFingerQuality();
		}

		// imageStatus value is greater than "STATUS_OK", Image acquisition successful.
		if (imageStatus == null /*STATUS_OK*/ ||
				imageStatus.getType().compareTo(IBScanException.Type.INVALID_PARAM_VALUE) > 0)
		{
			// Image acquisition successful
			CaptureInfo info = m_vecCaptureSeq.elementAt(m_nCurrentCaptureStep);
			_SetLEDs(info, __LED_COLOR_GREEN__, false);

			@SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String imageFileName = "fingerprint" + timeStamp + "_";
			if (fingerprintname.isEmpty()){
				fingerprintname= imageFileName;
			}else{
				fingerprintname += ";"+imageFileName;
			}
			String path = Constant.saveToInternalStorage(image.toBitmap(), getApplicationContext(), imageFileName);
			if (fingerprintimage.isEmpty()){
				fingerprintimage= path;
			}else{
				fingerprintimage += ";"+path;
			}


/* This code works well, but decided to exclude the code in v3.7.2 release (commented by Wade on 6/23/2021
			// [Start] WSQ to mem test
			Object[] wsqEncResult = new Object[2];
			Object[] wsqDecResult = new Object[6];

			try {
					//WSQ Encode Test
					wsqEncResult = getIBScanDevice().wsqEncodeToMem(image.buffer, image.width, image.height, image.pitch, image.bitsPerPixel,
						(int)image.resolutionX, 0.75, "");

					byte[] wsqCompByte = (byte[]) wsqEncResult[0];
					int wsqCompLength = Integer.parseInt(wsqEncResult[1].toString());
					getIBScanDevice().SaveRAWImage(wsqCompByte,(int)wsqCompLength, "/sdcard/WSQTest.wsq");

					//WSQ Decode Test
					wsqDecResult = getIBScanDevice().wsqDecodeToMem(wsqCompByte, wsqCompLength);
					byte[] wsqDecompByte = (byte[]) wsqDecResult[0];
					int outWidth =  Integer.parseInt(wsqDecResult[1].toString());
					int outHeight = Integer.parseInt(wsqDecResult[2].toString());
					int outPitch = Integer.parseInt(wsqDecResult[3].toString());
					int outBitsPerPixel = Integer.parseInt(wsqDecResult[4].toString());
					int outPixelPerInch = Integer.parseInt(wsqDecResult[5].toString());

					getIBScanDevice().SaveBitmapImage("/sdcard/WSQTest.bmp", wsqDecompByte, outWidth, outHeight, outPitch, outBitsPerPixel, outBitsPerPixel);

			} catch(IBScanException e) {
				e.printStackTrace();
			}
			// [End] WSQ to mem test
*/
			// SAVE IMAGE
/*			if (m_chkSaveImages.isSelected())
			{
				// Show chooser for output image.
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(imageFilter);
				int returnVal = chooser.showSaveDialog(IBScanUltimate_Sample.this);

				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					_SetStatusBarMessage("Saving image...");
					m_ImgSaveFolderName = chooser.getCurrentDirectory().toString() + File.separator + chooser.getSelectedFile().getName();
					_SaveBitmapImage(image, info.fingerName);
					_SaveWsqImage(image, info.fingerName);
					_SavePngImage(image, info.fingerName);
					_SaveJP2Image(image, info.fingerName);

					//save segmented fingers
					for (int i = 0; i < detectedFingerCount; i++)
				{
						String segmentName = info.fingerName + "_Segment_" + String.valueOf(i);
						_SaveBitmapImage(segmentImageArray[i], segmentName);
						_SaveWsqImage(segmentImageArray[i], segmentName);
						_SavePngImage(segmentImageArray[i], segmentName);
						_SaveJP2Image(segmentImageArray[i], segmentName);
				}
			}
			}
*/
//			if (m_chkDrawSegmentImage.isSelected())
			{
				m_nSegmentImageArrayCount = detectedFingerCount;
				m_SegmentPositionArray = segmentPositionArray;
			}

			// NFIQ
//			if (m_chkNFIQScore.isSelected())
			{
				byte[] nfiq_score = { 0, 0, 0, 0 };
				boolean isSpoof = false;
				m_strResSpoof = "";
				try
				{
					for (int i=0, segment_pos=0; i<4; i++)
					{
						if (m_FingerQuality[i].ordinal() != FingerQualityState.FINGER_NOT_PRESENT.ordinal())
						{
							nfiq_score[i] = (byte)getIBScanDevice().calculateNfiqScore(segmentImageArray[segment_pos++]);
						}
					}

				}
				catch (IBScanException ibse)
				{
					String str = "";
					if( ibse.getType().equals(IBScanException.Type.DEVICE_LOCK_ILLEGAL_DEVICE) )
					{
						str = "License is not activated";
					}
					else if (ibse.getType().equals(IBScanException.Type.PAD_PROPERTY_DISABLED))
					{
						m_strResSpoof = "PAD Property is not enabled\n(Resource missing)";
					}
					ibse.printStackTrace();
				}

				OnMsg_SetTxtNFIQScore("" + nfiq_score[0] + "-" + nfiq_score[1] + "-" + nfiq_score[2] + "-" + nfiq_score[3]);

			}

			if (imageStatus == null /*STATUS_OK*/)
			{
				m_strImageMessage = "Acquisition completed successfully";
				_SetImageMessage(m_strImageMessage);
			}
			else
			{
				// > IBSU_STATUS_OK
				m_strImageMessage = "Acquisition Warning (Warning code = " + imageStatus.getType().toString() + ")";
				_SetImageMessage(m_strImageMessage);

				OnMsg_DrawImage(device, image);
				OnMsg_AskRecapture(imageStatus);
				return;
			}
		}
		else
		{
			// < IBSU_STATUS_OK
			m_strImageMessage = "Acquisition failed (Error code = " + imageStatus.getType().toString() + ")";
			_SetImageMessage(m_strImageMessage);

			// Stop all of acquisition
			m_nCurrentCaptureStep = (int)m_vecCaptureSeq.size();
		}

		OnMsg_DrawImage(device, image);

		OnMsg_CaptureSeqNext();
	}

	@Override
	public void devicePlatenStateChanged(final IBScanDevice device, final PlatenState platenState)
	{
		if (platenState.equals(PlatenState.HAS_FINGERS))
			m_bNeedClearPlaten = true;
		else
			m_bNeedClearPlaten = false;

		if (platenState.equals(PlatenState.HAS_FINGERS))
		{
			m_strImageMessage = "Please remove your fingers on the platen first!";
			_SetImageMessage(m_strImageMessage);
		}
		else
		{
			if (m_nCurrentCaptureStep >= 0)
			{
				CaptureInfo info = m_vecCaptureSeq.elementAt(m_nCurrentCaptureStep);

				// Display message for image acuisition again
				String strMessage = info.PreCaptureMessage;

//				if (!m_chkAutoCapture.isSelected())
//					strMessage += "\r\nPress button 'Take Result Image' when image is good!";

				_SetImageMessage(strMessage);
				m_strImageMessage = strMessage;
			}
		}

		OnMsg_DrawFingerQuality();
	}

	@Override
	public void deviceWarningReceived(final IBScanDevice device, final IBScanException warning)
	{
	}

	@Override
	public void devicePressedKeyButtons(IBScanDevice device,int pressedKeyButtons)
	{


		boolean idle = m_bInitializing && (m_nCurrentCaptureStep == -1);
		boolean active = m_bInitializing && (m_nCurrentCaptureStep != -1);
		try	{
			if (pressedKeyButtons == __LEFT_KEY_BUTTON__)
			{
				if (idle)
				{
					System.out.println("Capture Start");
					device.setBeeper(IBScanDevice.BeepPattern.BEEP_PATTERN_GENERIC, 2/*Sol*/, 4/*100ms = 4*25ms*/, 0, 0);

				}
			}
			else if (pressedKeyButtons == __RIGHT_KEY_BUTTON__)
			{
				if ( (active) )
				{
					System.out.println("Capture Stop");
					device.setBeeper(IBScanDevice.BeepPattern.BEEP_PATTERN_GENERIC, 2/*Sol*/, 4/*100ms = 4*25ms*/, 0, 0);

				}
			}
		} catch (IBScanException e)	{
			e.printStackTrace();
		}
	}
}
