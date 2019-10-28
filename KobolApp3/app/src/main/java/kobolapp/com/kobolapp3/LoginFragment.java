package kobolapp.com.kobolapp3;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class LoginFragment extends AppCompatActivity  {


    EditText e1, e2;
    Button b1;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.fragment_login);


        e1 = findViewById(R.id.email);
        e2 = findViewById(R.id.password);
        t1 = findViewById(R.id.regolink);
        b1 = findViewById(R.id.login);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

    }

    public void openActivity1() {
        Intent intent = new Intent(this, HomeMenu.class);
        startActivity(intent);
    }

    public void openActivity2() {
        Intent intent = new Intent(this, RegisterFragment.class);
        startActivity(intent);
    }
}
