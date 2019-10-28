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


    EditText e1, e2, e3;
    Button b1;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.fragment_register);

        e1 = findViewById(R.id.email);
        e2 = findViewById(R.id.password);
        e3 = findViewById(R.id.cpassword);
        b1 = findViewById(R.id.register);
        t1 = findViewById(R.id.loginpage);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    //insert database

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

    private Boolean validate(){
        Boolean result = false;

        String name = e1.getText().toString();
        String password = e2.getText().toString();

        if(name.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT);
        } else {
            result = true;
        }

    return result;
    }
}
