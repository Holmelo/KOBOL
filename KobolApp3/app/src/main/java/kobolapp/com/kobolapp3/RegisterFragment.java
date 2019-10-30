package kobolapp.com.kobolapp3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterFragment extends AppCompatActivity {

    DatabaseHelper db;

    EditText e1, e2, e3, e4, e5, e6, e7, e8, e9;
    Button b1;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.fragment_register);
        db = new DatabaseHelper(this);
        e1 = findViewById(R.id.email);
        e2 = findViewById(R.id.password);
        e3 = findViewById(R.id.cpassword);
        e4 = findViewById(R.id.name);
        e5 = findViewById(R.id.address);
        e6 = findViewById(R.id.phone);
        e7 = findViewById(R.id.medicare);
        e8 = findViewById(R.id.doctor);
        e9 = findViewById(R.id.occupation);
        b1 = findViewById(R.id.register);

        t1 = findViewById(R.id.loginpage);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = e1.getText().toString();
                String s2 = e2.getText().toString();
                String s3 = e3.getText().toString();
                String s4 = e4.getText().toString();
                String s5 = e5.getText().toString();
                String s6 = e6.getText().toString();
                String s7 = e7.getText().toString();
                String s8 = e8.getText().toString();
                if (s1.equals("") || s2.equals("") || s3.equals("")) {
                    Toast.makeText(getApplicationContext(),"Fields are empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (s2.equals(s3)) {
                        Boolean checkmail = db.checkemail(s1);
                        if (checkmail==true) {
                            db.insert(s1,s2, s3, s4, s5, s6, s7, s8);
                            Toast.makeText(getApplicationContext(), "Register Complete", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterFragment.this, LoginFragment.class);
                            startActivity(intent);
                            //if (insert==true) {

                            //}
                        } else {
                            Toast.makeText(getApplicationContext(), "Email Already Exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                Intent intent = new Intent(RegisterFragment.this, LoginFragment.class);
                startActivity(intent);
            }
        });



    }
}
