package kobolapp.com.kobolapp3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeMenu extends AppCompatActivity {

    Button sleep, temp, head, emergency, concussion, csv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_menu);

        // Applies the custom action bar style
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        // Changes the action bar title
        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);
        title.setText(R.string.home);



        sleep = findViewById(R.id.timeSleptButton);
        temp = findViewById(R.id.tempButton);
        head = findViewById(R.id.headMovementButton);
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

        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeMenu.this, TempFragment.class);
                startActivity(intent);
            }
        });

    }

}
