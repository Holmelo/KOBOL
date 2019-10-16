package kobolapp.com.kobolapp3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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

import java.util.ArrayList;
import java.lang.Math;

import java.util.List;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Timer;

public class TimeSleptFragment extends AppCompatActivity {
    Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Time Slept");
        setContentView(R.layout.fragment_timeslept);

        /**set up chart**/
        LineChart chart = (LineChart) findViewById(R.id.chart);

        LineData data = new LineData(GetDataValues());
        chart.setData(data);
        chart.invalidate();

    }

    /**grab data**/
    private ArrayList<ILineDataSet> GetDataValues()
    {
        /**try {
         CSVReader reader = new CSVReader(new FileReader("csvfile.csv"));
         csv = reader.readAll();
         } catch (IOException e) {

         }**/
        String[] data = {"11","1","6","4","7","2","6","7","2","12","1","6","8","6","5","3","5","1","13","8","6","8","1","5","2","9","5"};

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
            seperatedData.add(x);;
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
        LineDataSet LineDataSet2 = new LineDataSet(avalues, "Accel");
        LineDataSet2.setColor(Color.YELLOW);
        LineDataSet LineDataSet3 = new LineDataSet(tvalues, "Temp");
        LineDataSet3.setColor(Color.GREEN);
        LineDataSet LineDataSet4 = new LineDataSet(hvalues, "Humidity");
        LineDataSet4.setColor(Color.BLUE);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(LineDataSet1);
        dataSets.add(LineDataSet2);
        dataSets.add(LineDataSet3);
        dataSets.add(LineDataSet4);

        return dataSets;
    }

    String convertSingleByte(byte[] b1){
        float f;
        f = ByteBuffer.wrap(b1).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        return Float.toString(f);
    }

    void addToCSV(float[] gyro, float[] accel, float temp, float humid, String csv, float time) throws IOException {
        FileWriter csvWriter = new FileWriter(csv);
        csvWriter.append(Float.toString(time));
        csvWriter.append(",");
        csvWriter.append(Float.toString(gyro[0]));
        csvWriter.append(",");
        csvWriter.append(Float.toString(gyro[1]));
        csvWriter.append(",");
        csvWriter.append(Float.toString(gyro[2]));
        csvWriter.append(",");
        csvWriter.append(Float.toString(accel[0]));
        csvWriter.append(",");
        csvWriter.append(Float.toString(accel[1]));
        csvWriter.append(",");
        csvWriter.append(Float.toString(accel[2]));
        csvWriter.append(",");
        csvWriter.append(Float.toString(temp));
        csvWriter.append(",");
        csvWriter.append(Float.toString(humid));
        csvWriter.append(",");
        csvWriter.append("\n");
        csvWriter.flush();
        csvWriter.close();
    }

}
