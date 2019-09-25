package kobolapp.com.kobolapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {

    private Button timeSleptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeSleptButton = findViewById(R.id.timeSleptButton);

        timeSleptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeSleptPage();

            }
        });
    }

    public void openTimeSleptPage() {
        Intent intent = new Intent(this, timeslept.class);
        startActivity(intent);
    }
}
