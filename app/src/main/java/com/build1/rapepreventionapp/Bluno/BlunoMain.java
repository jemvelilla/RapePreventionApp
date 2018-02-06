package com.build1.rapepreventionapp.Bluno;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import android.widget.BaseAdapter;
import android.os.Handler;
import android.os.IBinder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.build1.rapepreventionapp.R;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;

/**
 * Created by JEMYLA VELILLA on 06/02/2018.
 */

public class BlunoMain extends Activity {
    private Button buttonScan;
    private Button buttonSerialSend;
    private EditText serialSendText;
    private TextView serialReceivedText;

    //bluno library
    public static final String CommandUUID = "0000dfb2-0000-1000-8000-00805f9b34fb";
    public static final String ModelNumberStringUUID = "00002a24-0000-1000-8000-00805f9b34fb";
    private static final int REQUEST_ENABLE_BT = 1;
    public static final String SerialPortUUID = "0000dfb1-0000-1000-8000-00805f9b34fb";
    private static BluetoothGattCharacteristic mCommandCharacteristic;
    private static BluetoothGattCharacteristic mModelNumberCharacteristic;
    private static BluetoothGattCharacteristic mSCharacteristic;
    private static BluetoothGattCharacteristic mSerialPortCharacteristic;
    private int mBaudrate = 115200;
    private String mBaudrateBuffer = ("AT+CURRUART=" + mBaudrate + "\r\n");
    private BluetoothAdapter mBluetoothAdapter;
    BluetoothLeService mBluetoothLeService;
    public boolean mConnected = false;
    private Runnable mConnectingOverTimeRunnable = new C01351();
    public connectionStateEnum mConnectionState = connectionStateEnum.isNull;
    private String mDeviceAddress;
    private String mDeviceName;
    private Runnable mDisonnectingOverTimeRunnable = new C01362();
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList();
    private final BroadcastReceiver mGattUpdateReceiver = new C01395();
    private Handler mHandler = new Handler();
    private LeDeviceListAdapter mLeDeviceListAdapter = null;
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new C01427();
    private String mPassword = "AT+PASSWOR=DFRobot\r\n";
    AlertDialog mScanDeviceDialog;
    private boolean mScanning = false;
    ServiceConnection mServiceConnection = new C01406();
    private Context mainContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluno);

        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonSerialSend = (Button) findViewById(R.id.buttonSerialSend);
        serialSendText = (EditText) findViewById(R.id.serialSendText);
        serialReceivedText = (TextView) findViewById(R.id.serialReceivedText);

        onCreateProcess();

        serialBegin(115200);													//set the Uart Baudrate on BLE chip to 115200

        serialSendText=(EditText) findViewById(R.id.serialSendText);			//initial the EditText of the sending data

        buttonSerialSend = (Button) findViewById(R.id.buttonSerialSend);		//initial the button for sending the data
        buttonSerialSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                serialSend(serialSendText.getText().toString());				//send the data to the BLUNO
            }
        });

        buttonScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                buttonScanOnClickProcess();										//Alert Dialog for selecting the BLE device
            }
        });
    }

    protected void onResume(){
        super.onResume();
        System.out.println("BlUNOActivity onResume");
        //onResume Process by BlunoLibrary
        onResumeProcess();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultProcess(requestCode, resultCode, data);					//onActivityResult Process by BlunoLibrary
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        onPauseProcess();													//onPause Process by BlunoLibrary
    }

    protected void onStop() {
        super.onStop();
        onStopProcess();														//onStop Process by BlunoLibrary
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyProcess();														//onDestroy Process by BlunoLibrary
    }


    public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
        switch (theConnectionState) {											//Four connection state
            case isConnected:
                buttonScan.setText("Connected");
                break;
            case isConnecting:
                buttonScan.setText("Connecting");
                break;
            case isToScan:
                buttonScan.setText("Scan");
                break;
            case isScanning:
                buttonScan.setText("Scanning");
                break;
            case isDisconnecting:
                buttonScan.setText("isDisconnecting");
                break;
            default:
                break;
        }
    }

    public void onSerialReceived(String theString) {							//Once connection data received, this function will be called
        // TODO Auto-generated method stub
        serialReceivedText.append(theString);							//append the text into the EditText
    }

    //BlunoLibrary
    class C01351 implements Runnable {
        C01351() {
        }

        public void run() {
            if (mConnectionState == connectionStateEnum.isConnecting) {
                mConnectionState = connectionStateEnum.isToScan;
            }
            onConectionStateChange(mConnectionState);
            mBluetoothLeService.close();
        }
    }

    class C01362 implements Runnable {
        C01362() {
        }

        public void run() {
            if (mConnectionState == connectionStateEnum.isDisconnecting) {
                mConnectionState = connectionStateEnum.isToScan;
            }
            onConectionStateChange(mConnectionState);
            mBluetoothLeService.close();
        }
    }

    class C01373 implements DialogInterface.OnCancelListener {
        C01373() {
        }

        public void onCancel(DialogInterface arg0) {
            System.out.println("mBluetoothAdapter.stopLeScan");
            mConnectionState = connectionStateEnum.isToScan;
            onConectionStateChange(mConnectionState);
            mScanDeviceDialog.dismiss();
            scanLeDevice(false);
        }
    }

    class C01384 implements DialogInterface.OnClickListener {
        C01384() {
        }

        public void onClick(DialogInterface dialog, int which) {
            BluetoothDevice device = mLeDeviceListAdapter.getDevice(which);
            if (device != null) {
                scanLeDevice(false);
                if (device.getName() == null || device.getAddress() == null) {
                    mConnectionState = connectionStateEnum.isToScan;
                    onConectionStateChange(mConnectionState);
                    return;
                }
                System.out.println("onListItemClick " + device.getName().toString());
                System.out.println("Device Name:" + device.getName() + "   " + "Device Name:" + device.getAddress());
                mDeviceName = device.getName().toString();
                mDeviceAddress = device.getAddress().toString();
                if (mBluetoothLeService.connect(mDeviceAddress)) {
                    mConnectionState = connectionStateEnum.isConnecting;
                    onConectionStateChange(mConnectionState);
                    mHandler.postDelayed(mConnectingOverTimeRunnable, 10000);
                    return;
                }
                Log.d(TAG, "Connect request fail");
                mConnectionState = connectionStateEnum.isToScan;
                onConectionStateChange(mConnectionState);
            }
        }
    }

    class C01395 extends BroadcastReceiver {
        C01395() {
        }

        @SuppressLint({"DefaultLocale"})
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println("mGattUpdateReceiver->onReceive->action=" + action);
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                mHandler.removeCallbacks(mConnectingOverTimeRunnable);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                mConnectionState = connectionStateEnum.isToScan;
                onConectionStateChange(mConnectionState);
                mHandler.removeCallbacks(mDisonnectingOverTimeRunnable);
                mBluetoothLeService.close();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                for (BluetoothGattService gattService : mBluetoothLeService.getSupportedGattServices()) {
                    System.out.println("ACTION_GATT_SERVICES_DISCOVERED  " + gattService.getUuid().toString());
                }
                getGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                if (mSCharacteristic == mModelNumberCharacteristic) {
                    if (intent.getStringExtra(BluetoothLeService.EXTRA_DATA).toUpperCase().startsWith("DF BLUNO")) {
                        mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, false);
                        mSCharacteristic = mCommandCharacteristic;
                        mSCharacteristic.setValue(mPassword);
                        mBluetoothLeService.writeCharacteristic(mSCharacteristic);
                        mSCharacteristic.setValue(mBaudrateBuffer);
                        mBluetoothLeService.writeCharacteristic(mSCharacteristic);
                        mSCharacteristic = mSerialPortCharacteristic;
                        mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, true);
                        mConnectionState = connectionStateEnum.isConnected;
                        onConectionStateChange(mConnectionState);
                    } else {
                        Toast.makeText(mainContext, "Please select DFRobot devices", Toast.LENGTH_SHORT).show();
                        mConnectionState = connectionStateEnum.isToScan;
                        onConectionStateChange(mConnectionState);
                    }
                } else if (mSCharacteristic == mSerialPortCharacteristic) {
                    onSerialReceived(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                }
                System.out.println("displayData " + intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    }

    class C01406 implements ServiceConnection {
        C01406() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder service) {
            System.out.println("mServiceConnection onServiceConnected");
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            System.out.println("mServiceConnection onServiceDisconnected");
            mBluetoothLeService = null;
        }
    }

    class C01427 implements BluetoothAdapter.LeScanCallback {
        C01427() {
        }

        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                public void run() {
                    System.out.println("mLeScanCallback onLeScan run ");
                    mLeDeviceListAdapter.addDevice(device);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private class LeDeviceListAdapter extends BaseAdapter {
        private LayoutInflater mInflator;
        private ArrayList<BluetoothDevice> mLeDevices = new ArrayList();

        public LeDeviceListAdapter() {
            mInflator = getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return (BluetoothDevice) mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        public int getCount() {
            return mLeDevices.size();
        }

        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                System.out.println("mInflator.inflate  getView");
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            BluetoothDevice device = (BluetoothDevice) mLeDevices.get(i);
            String deviceName = device.getName();
            if (deviceName == null || deviceName.length() <= 0) {
                viewHolder.deviceName.setText("Unknown device");
            } else {
                viewHolder.deviceName.setText(deviceName);
            }
            viewHolder.deviceAddress.setText(device.getAddress());
            return view;
        }
    }

    static class ViewHolder {
        TextView deviceAddress;
        TextView deviceName;

        ViewHolder() {
        }
    }

    public enum connectionStateEnum {
        isNull,
        isScanning,
        isToScan,
        isConnecting,
        isConnected,
        isDisconnecting
    }

    public void serialSend(String theString) {
        if (mConnectionState == connectionStateEnum.isConnected) {
            mSCharacteristic.setValue(theString);
            mBluetoothLeService.writeCharacteristic(mSCharacteristic);
        }
    }

    public void serialBegin(int baud) {
        mBaudrate = baud;
        mBaudrateBuffer = "AT+CURRUART=" + mBaudrate + "\r\n";
    }

    public void onCreateProcess() {
        if (!initiate()) {
            Toast.makeText(mainContext, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            ((Activity) mainContext).finish();
        }
        bindService(new Intent(this, BluetoothLeService.class), mServiceConnection, BIND_AUTO_CREATE);
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        mScanDeviceDialog = new AlertDialog.Builder(mainContext).setTitle("BLE Device Scan...").setAdapter(mLeDeviceListAdapter, new C01384()).setOnCancelListener(new C01373()).create();
    }

    public void onResumeProcess() {
        System.out.println("BlUNOActivity onResume");
        if (!(mBluetoothAdapter.isEnabled() || mBluetoothAdapter.isEnabled())) {
            ((Activity) mainContext).startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
        }
        mainContext.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    public void onPauseProcess() {
        System.out.println("BLUNOActivity onPause");
        scanLeDevice(false);
        mainContext.unregisterReceiver(mGattUpdateReceiver);
        mLeDeviceListAdapter.clear();
        mConnectionState = connectionStateEnum.isToScan;
        onConectionStateChange(mConnectionState);
        mScanDeviceDialog.dismiss();
        if (mBluetoothLeService != null) {
            mBluetoothLeService.disconnect();
            mHandler.postDelayed(mDisonnectingOverTimeRunnable, 10000);
        }
        mSCharacteristic = null;
    }

    public void onStopProcess() {
        System.out.println("MiUnoActivity onStop");
        if (mBluetoothLeService != null) {
            mHandler.removeCallbacks(mDisonnectingOverTimeRunnable);
            mBluetoothLeService.close();
        }
        mSCharacteristic = null;
    }

    public void onDestroyProcess() {
        mainContext.unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    public void onActivityResultProcess(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 0) {
            ((Activity) mainContext).finish();
        }
    }

    boolean initiate() {
        if (!mainContext.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            return false;
        }
        mBluetoothAdapter = ((BluetoothManager) mainContext.getSystemService(BLUETOOTH_SERVICE)).getAdapter();
        if (mBluetoothAdapter != null) {
            return true;
        }
        return false;
    }

    void buttonScanOnClickProcess() {
        switch (mConnectionState) {
            case isNull:
                mConnectionState = connectionStateEnum.isScanning;
                onConectionStateChange(mConnectionState);
                scanLeDevice(true);
                mScanDeviceDialog.show();
                return;
            case isToScan:
                mConnectionState = connectionStateEnum.isScanning;
                onConectionStateChange(mConnectionState);
                scanLeDevice(true);
                mScanDeviceDialog.show();
                return;
            case isConnected:
                mBluetoothLeService.disconnect();
                mHandler.postDelayed(mDisonnectingOverTimeRunnable, 10000);
                mConnectionState = connectionStateEnum.isDisconnecting;
                onConectionStateChange(mConnectionState);
                return;
            default:
                return;
        }
    }

    void scanLeDevice(boolean enable) {
        if (enable) {
            System.out.println("mBluetoothAdapter.startLeScan");
            if (mLeDeviceListAdapter != null) {
                mLeDeviceListAdapter.clear();
                mLeDeviceListAdapter.notifyDataSetChanged();
            }
            if (!mScanning) {
                mScanning = true;
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        } else if (mScanning) {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private void getGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices != null) {
            mModelNumberCharacteristic = null;
            mSerialPortCharacteristic = null;
            mCommandCharacteristic = null;
            mGattCharacteristics = new ArrayList();
            for (BluetoothGattService gattService : gattServices) {
                System.out.println("displayGattServices + uuid=" + gattService.getUuid().toString());
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                ArrayList<BluetoothGattCharacteristic> charas = new ArrayList();
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    charas.add(gattCharacteristic);
                    String uuid = gattCharacteristic.getUuid().toString();
                    if (uuid.equals(ModelNumberStringUUID)) {
                        mModelNumberCharacteristic = gattCharacteristic;
                        System.out.println("mModelNumberCharacteristic  " + mModelNumberCharacteristic.getUuid().toString());
                    } else if (uuid.equals(SerialPortUUID)) {
                        mSerialPortCharacteristic = gattCharacteristic;
                        System.out.println("mSerialPortCharacteristic  " + mSerialPortCharacteristic.getUuid().toString());
                    } else if (uuid.equals(CommandUUID)) {
                        mCommandCharacteristic = gattCharacteristic;
                        System.out.println("mSerialPortCharacteristic  " + mSerialPortCharacteristic.getUuid().toString());
                    }
                }
                mGattCharacteristics.add(charas);
            }
            if (mModelNumberCharacteristic == null || mSerialPortCharacteristic == null || mCommandCharacteristic == null) {
                Toast.makeText(mainContext, "Please select DFRobot devices", Toast.LENGTH_SHORT).show();
                mConnectionState = connectionStateEnum.isToScan;
                onConectionStateChange(mConnectionState);
                return;
            }
            mSCharacteristic = mModelNumberCharacteristic;
            mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, true);
            mBluetoothLeService.readCharacteristic(mSCharacteristic);
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

}
