package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * Created by Verena Käfer on 03.11.2017.
 */

public class DisplayActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_activity);

        TextView tvMessage = (TextView) findViewById(R.id.tvMessage);
        String message = getIntent().getStringExtra("Message");
        tvMessage.setText(message);
    }




}
