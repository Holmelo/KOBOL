package kobolapp.com.kobolapp3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeMenu extends AppCompatActivity {

    Button sleep, temp, head, emergency, concussion, csv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_menu);

        sleep = findViewById(R.id.timeSleptButton);
        temp = findViewById(R.id.headMovementButton);
        head = findViewById(R.id.emergencyButton);
        emergency = findViewById(R.id.emergencyButton);
        concussion = findViewById(R.id.concussionButton);
        csv = findViewById(R.id.csvButton);


        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeMenu.this, TimeSleptFragment.class);
                startActivity(intent);
            }
        });

    }

}
