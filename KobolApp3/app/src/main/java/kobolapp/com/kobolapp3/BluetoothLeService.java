package kobolapp.com.kobolapp3;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.UUID;


/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private Context mContext;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothLeScanner mBluetoothScanner;
    private boolean mIsScanning;

    private int mConnectionState = STATE_DISCONNECTED;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    private static final int SCAN_PERIOD = 50;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    public final static String ACCELERATION_UPDATE_DATA =
            "com.example.bluetooth.le.ACCELERATION_UPDATE_DATA";
    public final static String GYROSCOPE_UPDATE_DATA =
            "com.example.bluetooth.le.GYROSCOPE_UPDATE_DATA";
    public final static String TEMPERATURE_UPDATE_DATA =
            "com.example.bluetooth.le.TEMPERATURE_UPDATE_DATA";
    public final static String HUMIDITY_UPDATE_DATA =
            "com.example.bluetooth.le.HUMIDITY_UPDATE_DATA";



    public final static UUID UUID_GYROSCOPE_MEASUREMENT =
            UUID.fromString(GattAttributes.GYROSCOPE_MEASUREMENT);

    public final static UUID UUID_ACCELERATION_MEASUREMENT =
            UUID.fromString(GattAttributes.ACCELERATION_MEASUREMENT);

    public final static UUID UUID_TEMPERATURE_MEASUREMENT =
            UUID.fromString(GattAttributes.TEMPERATURE_MEASUREMENT);

    public final static UUID UUID_HUMIDITY_MEASUREMENT =
            UUID.fromString(GattAttributes.HUMIDITY_MEASUREMENT);


    private final String SLEEP_MONITOR_DEVICE_BT_NAME = "Sleep Monitor Device";

    boolean isSleepDevice (BluetoothDevice bluetoothDevice){
        String name = bluetoothDevice.getName();
        return name != null && name.equals(SLEEP_MONITOR_DEVICE_BT_NAME);
    }

    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            BluetoothDevice device = result.getDevice();


            String name = device.getName();
            if (name != null){
                Log.d(TAG, name + " : " + device.getAddress());
            }else {
                Log.d(TAG, device.getAddress());
            }
            if (isSleepDevice(device)){

                mBluetoothScanner.stopScan(mLeScanCallback);
                mIsScanning = false;


                connect(result.getDevice().getAddress());

            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);

        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.d(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.d(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status){
            Log.d(TAG,"On Descriptor Read!");
        }

        public void onDescriptorRead(){

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);

                final byte[] data = characteristic.getValue();

                Log.d(TAG, characteristic.getUuid().toString());


                if(data != null){
                    // Log.d(TAG, data.toString());
                }

            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {



            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);

            final byte[] data = characteristic.getValue();


            if(data != null){
                // Log.d(TAG, data.toString());
            }

        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        Log.d(TAG, mContext.toString());
        Log.d(TAG, action);
        Log.d(TAG, intent.toString());
        mContext.sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        final byte[] data = characteristic.getValue();

        if(characteristic.getUuid().equals(UUID_ACCELERATION_MEASUREMENT)){
            intent.putExtra(ACCELERATION_UPDATE_DATA, data);
        }

        if(characteristic.getUuid().equals(UUID_GYROSCOPE_MEASUREMENT)){
            intent.putExtra(GYROSCOPE_UPDATE_DATA, data);
        }

        if(characteristic.getUuid().equals(UUID_TEMPERATURE_MEASUREMENT)){
            intent.putExtra(TEMPERATURE_UPDATE_DATA, data);
        }

        if(characteristic.getUuid().equals(UUID_HUMIDITY_MEASUREMENT)){
            intent.putExtra(HUMIDITY_UPDATE_DATA, data);
        }

        mContext.sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    public boolean initialize(Context context, BluetoothManager bluetoothManager) {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        this.mContext = context;
        mBluetoothManager = bluetoothManager;
        if (mBluetoothManager == null) {

            mBluetoothManager = (BluetoothManager) getSystemService(mContext.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        mBluetoothScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mIsScanning = false;

        return true;
    }

    public boolean isScanning(){
        return mIsScanning;
    }

    public void toggleBluetooth(){
        if(mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.disable();
        }else{
            mBluetoothAdapter.enable();
        }
    }

    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;

        mBluetoothGatt.getServices();

        return true;
    }

//    public void startScan(){
//        mBluetoothScanner.startScan(mLeScanCallback);
//        mIsScanning = true;
//        final Handler handle = new Handler();
//        handle.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mBluetoothScanner.stopScan(mLeScanCallback);
//            }
//        },SCAN_PERIOD);
//    }

//    public void stopScan(){
//        if(mIsScanning)
//            mBluetoothScanner.stopScan(mLeScanCallback);
//        mIsScanning = false;
//    }

//    public void disconnect() {
//        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
//            Log.w(TAG, "BluetoothAdapter not initialized");
//            return;
//        }
//        mBluetoothGatt.disconnect();
//    }

//    public void close() {
//        if (mBluetoothGatt == null) {
//            return;
//    }
//        mBluetoothGatt.close();
//        mBluetoothGatt = null;
//    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {

        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        boolean setNotification = mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        Log.d(TAG, "Set Notification: " + String.valueOf(setNotification));
        List<BluetoothGattDescriptor> bluetoothGattDescriptors = characteristic.getDescriptors();

        Log.d(TAG, characteristic.getUuid().toString());
        Log.d(TAG, "number of descriptors: " + bluetoothGattDescriptors.size());

        for(BluetoothGattDescriptor descriptor : bluetoothGattDescriptors){
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            boolean worked = mBluetoothGatt.writeDescriptor(descriptor);

            readCharacteristic(characteristic);

            Log.d(TAG, "Descriptor: " + descriptor.toString());
            Log.d(TAG, "Set Descriptor: " + String.valueOf(worked));
        }

    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }

}