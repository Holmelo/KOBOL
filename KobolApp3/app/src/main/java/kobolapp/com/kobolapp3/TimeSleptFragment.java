package kobolapp.com.kobolapp3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import android.graphics.Color;
import android.os.Bundle;

import com.opencsv.CSVReader;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.opencsv.exceptions.CsvException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.lang.Math;

import java.util.List;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Random;
import java.lang.Object;
import java.util.Timer;

import android.Manifest;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TimeSleptFragment extends AppCompatActivity {

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private final String TAG = "Main Activity: ";
    private Context mContext;
    private BluetoothManager mBluetoothManager;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private boolean mConnected;
    private String mDeviceName;
    private String mDeviceAddress;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    private int mConnectionState = STATE_DISCONNECTED;


    Timer timer = new Timer();
    Random randint = new Random();
    List<String> testdata = new ArrayList<>();
    String[] data2 = new String[900];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkBTPermissions();



        mContext = this;

        if (mBluetoothManager == null) {

            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");

            }
        }
        mBluetoothLeService = new BluetoothLeService();
        mBluetoothLeService.initialize(this, mBluetoothManager);


        setTitle("Time Slept");
        setContentView(R.layout.fragment_timeslept);

        /**set up chart**/
        LineChart chart = (LineChart) findViewById(R.id.chart);

        for (int i = 0; i < 900;i += 9){
            if (testdata.isEmpty()) {
                testdata.add("0");
                testdata.add("5");
                testdata.add("5");
                testdata.add("5");
                testdata.add("5");
                testdata.add("5");
                testdata.add("5");
                testdata.add("2");
                testdata.add("3");
            }
            else {
                testdata.add(Integer.toString(i/9));
                testdata.add(Integer.toString((randint.nextInt(4)-3)+(Integer.parseInt(testdata.get(i-9)))));
                testdata.add(Integer.toString((randint.nextInt(4)-3)+(Integer.parseInt(testdata.get(i-8)))));
                testdata.add(Integer.toString((randint.nextInt(4)-3)+(Integer.parseInt(testdata.get(i-7)))));
                testdata.add(Integer.toString((randint.nextInt(4)-3)+(Integer.parseInt(testdata.get(i-6)))));
                testdata.add(Integer.toString((randint.nextInt(4)-3)+(Integer.parseInt(testdata.get(i-5)))));
                testdata.add(Integer.toString((randint.nextInt(4)-3)+(Integer.parseInt(testdata.get(i-4)))));
                testdata.add(Integer.toString((randint.nextInt(4)-3)+(Integer.parseInt(testdata.get(i-3)))));
                testdata.add(Integer.toString((randint.nextInt(4)-3)+(Integer.parseInt(testdata.get(i-2)))));
            }
        }
        for (int i = 0;i < testdata.size(); i += 1){
            data2[i] = testdata.get(i);
        }

        LineData data = new LineData(GetDataValues());
        chart.setData(data);

        chart.getXAxis().setLabelCount(100, true);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.invalidate();

        //
        mBluetoothLeService.startScan();
        //
        mBluetoothLeService.startScan();

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        float[] gyro = {1f, 2f, 1f};
        float[] accel = {1f, 1f, 3f};
        float temp = 1;
        float humid = 1;

        float time = 1;
        try {
            addToCSV(gyro, accel, temp, humid, this, 1.0f);
            String s = readFromFile(this);
            Log.d(TAG, s);
        } catch (Exception e) {

        }
        time = 1;
    }

    /**grab data**/
    private ArrayList<ILineDataSet> GetDataValues() {

        List<String[]> csv = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.test)));
            csv = reader.readAll();
        } catch (Exception e){}
        String[] data;
        if (csv.size() > 1) {
            data = csv.get(0);
            for (int i = 1; i < csv.size(); i += 1) {
                data = ArrayUtils.addAll(data, csv.get(i));
            }
        }
        else{
            data = csv.get(0);
        }


        List<List<String>> seperatedData = new ArrayList<List<String>>();


        for (int i = 0; i < data.length; i += 9){
            List<String> x = new ArrayList<String>();

            float a = Float.parseFloat(data[i+1])+Float.parseFloat(data[i+2])+Float.parseFloat(data[i+3]);
            float b = Float.parseFloat(data[i+4])+Float.parseFloat(data[i+5])+Float.parseFloat(data[i+6]);

            x.add(0, data[i]);
            x.add(1, Float.toString(a));
            x.add(2, Float.toString(b));
            x.add(3, data[i+7]);
            x.add(4, data[i+8]);
            seperatedData.add(x);
        }

        List<Entry> gvalues = new ArrayList<Entry>();
        List<Entry> avalues = new ArrayList<Entry>();
        List<Entry> tvalues = new ArrayList<Entry>();
        List<Entry> hvalues = new ArrayList<Entry>();

        for (int i = 0; i < seperatedData.size(); i += 1){

            gvalues.add(new Entry(Float.parseFloat(seperatedData.get(i).get(0)),Float.parseFloat(seperatedData.get(i).get(1))));
            avalues.add(new Entry(Float.parseFloat(seperatedData.get(i).get(0)),Float.parseFloat(seperatedData.get(i).get(2))));
            tvalues.add(new Entry(Float.parseFloat(seperatedData.get(i).get(0)),Float.parseFloat(seperatedData.get(i).get(3))));
            hvalues.add(new Entry(Float.parseFloat(seperatedData.get(i).get(0)),Float.parseFloat(seperatedData.get(i).get(4))));

        }

        LineDataSet LineDataSet1 = new LineDataSet(gvalues, "Gyro");
        LineDataSet1.setColor(Color.RED);
        LineDataSet1.setDrawCircles(false);
        LineDataSet LineDataSet2 = new LineDataSet(avalues, "Accel");
        LineDataSet2.setColor(Color.YELLOW);
        LineDataSet2.setDrawCircles(false);
        LineDataSet LineDataSet3 = new LineDataSet(tvalues, "Temp");
        LineDataSet3.setColor(Color.GREEN);
        LineDataSet3.setDrawCircles(false);
        LineDataSet LineDataSet4 = new LineDataSet(hvalues, "Humidity");
        LineDataSet4.setColor(Color.BLUE);
        LineDataSet4.setDrawCircles(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(LineDataSet1);
        dataSets.add(LineDataSet2);
        dataSets.add(LineDataSet3);
        dataSets.add(LineDataSet4);

        return dataSets;
    }

    String convertByte(byte[] b1){
        float f;
        f = ByteBuffer.wrap(b1).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        return Float.toString(f);
    }

    void addToCSV(float[] gyro, float[] accel, float temp, float humid, Context context, float time) throws IOException {

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("test.csv", Context.MODE_APPEND));
        String data = "";
        data += (Float.toString(time));
        data +=(",");
        data +=(Float.toString(gyro[0]));
        data +=(",");
        data +=(Float.toString(gyro[1]));
        data +=(",");
        data +=(Float.toString(gyro[2]));
        data +=(",");
        data +=(Float.toString(accel[0]));
        data +=(",");
        data +=(Float.toString(accel[1]));
        data +=(",");
        data +=(Float.toString(accel[2]));
        data +=(",");
        data +=(Float.toString(temp));
        data +=(",");
        data +=(Float.toString(humid));
        data +=(",");
        data +=("\n");
        outputStreamWriter.write(data);
        outputStreamWriter.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d("checkBTPermissions: ", "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize(mContext, mBluetoothManager)) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter(){
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public static byte[] reverse(byte[] array) {
        if (array == null) {
            return array;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
        return array;
    }

    private static float bytesToFloat(byte[] byteArr){
        byte[] reByteArray = reverse(byteArr);

        ByteBuffer buffer = ByteBuffer.wrap(reByteArray);

        return buffer.getFloat();
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("test.csv");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.d(TAG, "ACTION_GATT_CONNECTED");
                mConnected = true;
                // updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.d(TAG, "ACTION_GATT_DISCONNECTED");
                mConnected = false;
                // updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                // clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.d(TAG, "ACTION_GATT_SERVICES_DISCOVERED");
                // Show all the supported services and characteristics on the user interface.
                List<BluetoothGattService> gattServices = mBluetoothLeService.getSupportedGattServices();
                for (BluetoothGattService service : gattServices ) {
                    List<BluetoothGattCharacteristic> gattCharacteristics = service.getCharacteristics();

                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {

                        if (gattCharacteristic.getUuid().equals(UUID.fromString(GattAttributes.GYROSCOPE_MEASUREMENT))) {
                            String msg = "GYROSCOPE_MEASUREMENT FOUND - SETTING CHARACTERISTIC NOTIFICATION";
                            Log.d(TAG, msg);
                            mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                        }

                        if (gattCharacteristic.getUuid().equals(UUID.fromString(GattAttributes.ACCELERATION_MEASUREMENT))) {
                            String msg = "ACCELERATION_MEASUREMENT FOUND - SETTING CHARACTERISTIC NOTIFICATION";
                            Log.d(TAG, msg);
                            mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                        }

                        if (gattCharacteristic.getUuid().equals(UUID.fromString(GattAttributes.TEMPERATURE_MEASUREMENT))) {
                            String msg = "TEMPERATURE_MEASUREMENT FOUND - SETTING CHARACTERISTIC NOTIFICATION";
                            Log.d(TAG, msg);
                            mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                        }

                        if (gattCharacteristic.getUuid().equals(UUID.fromString(GattAttributes.HUMIDITY_MEASUREMENT))) {
                            String msg = "HUMIDITY_MEASUREMENT FOUND - SETTING CHARACTERISTIC NOTIFICATION";
                            Log.d(TAG, msg);
                            mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                        }

                    }

                }

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)){
                Bundle extras = intent.getExtras();
                update(extras.getByteArray(BluetoothLeService.ACCELERATION_UPDATE_DATA));

            }
        }
    };

    private String makeStr(float x, float y, float z){
        return "<" + String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z) + ">";
    }
    private int i = 1;
    private void update(byte [] acc){
        if(acc == null){
            return;
        }else {
            Log.d(TAG, "UPDATING ACCELERATION!");

            // Acceleration
            byte[] accxb = {acc[0], acc[1], acc[2], acc[3]};
            float accx = bytesToFloat(accxb);

            byte[] accyb = {acc[4], acc[5], acc[6], acc[7]};
            float accy = bytesToFloat(accyb);

            byte[] acczb = {acc[8], acc[9], acc[10], acc[11]};
            float accz = bytesToFloat(acczb);

            float[] accel = {accx, accy, accz};


            // Gyroscope
            byte[] gyroxb = {acc[12], acc[13], acc[14], acc[15]};
            float gyrox = bytesToFloat(gyroxb);

            byte[] gyroyb = {acc[16], acc[17], acc[18], acc[19]};
            float gyroy = bytesToFloat(gyroyb);

            byte[] gyrozb = {acc[20], acc[21], acc[22], acc[23]};
            float gyroz = bytesToFloat(gyrozb);

            float[] gyro = {gyrox, gyroy, gyroz};

            // Temperature
            byte[] tempb = {acc[24], acc[25], acc[26], acc[27]};
            float temp = bytesToFloat(tempb);

            // Humidity
            byte[] humib = {acc[28], acc[29], acc[30], acc[31]};
            float humi = bytesToFloat(humib);

            Log.d(TAG, makeStr(accx, accy, accz) +
                    makeStr(gyrox, gyroy, gyroz) +
                    "<" + String.valueOf(temp) + ">" +
                    "<" + String.valueOf(humi) + ">");


            i = i + 1;
            float fs = i;
            try {
                addToCSV(gyro, accel, temp, humi, this, 1.0f);
                String s = readFromFile(this);
                Log.d(TAG, s);
            } catch (Exception e) {

            }

        }
    }

}
