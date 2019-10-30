package kobolapp.com.kobolapp3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    Button viewSleepButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Home");

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewSleepButton = view.findViewById(R.id.timeSleptButton);
        viewSleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                Intent intent = new Intent(getActivity(), TimeSleptFragment.class);
                startActivity(intent);
            }
        });


        return view;

    }


}

