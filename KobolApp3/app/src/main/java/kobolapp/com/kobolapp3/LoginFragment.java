package kobolapp.com.kobolapp3;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class LoginFragment extends AppCompatActivity  {


    EditText e1, e2;
    Button b1;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.fragment_login);


        db = new DatabaseHelper(this);
        e1 = findViewById(R.id.email);
        e2 = findViewById(R.id.password);
        b1 = findViewById(R.id.login);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = e1.getText().toString();
                String password = e2.getText().toString();

                Boolean checkemailpass = db.emailpassword(email, password);
                if (checkemailpass==true) {
                    Toast.makeText(getApplicationContext(), "Successful Login", Toast.LENGTH_SHORT).show();
                    openActivity();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong email or password", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    public void openActivity() {
        Intent intent = new Intent(this, HomeMenu.class);
        startActivity(intent);
    }

}
