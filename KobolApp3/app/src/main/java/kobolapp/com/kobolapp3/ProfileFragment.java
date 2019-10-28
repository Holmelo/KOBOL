package kobolapp.com.kobolapp3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ProfileFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        setTitle("Profile");
    }
}
