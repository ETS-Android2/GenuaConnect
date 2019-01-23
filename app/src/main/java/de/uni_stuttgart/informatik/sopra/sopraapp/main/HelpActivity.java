package de.uni_stuttgart.informatik.sopra.sopraapp.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        TextView helpViewRef = findViewById(R.id.helpView);
        helpViewRef.setMovementMethod(new ScrollingMovementMethod());
    }
}
