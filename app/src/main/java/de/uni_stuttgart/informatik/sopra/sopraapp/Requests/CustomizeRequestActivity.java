 package de.uni_stuttgart.informatik.sopra.sopraapp.Requests;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

public class CustomizeRequestActivity extends AppCompatActivity {

    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_request);

    }
}
