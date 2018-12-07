 package de.uni_stuttgart.informatik.sopra.sopraapp.Requests;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.snmp4j.smi.OID;

import java.util.ArrayList;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

public class CustomizeRequestActivity extends AppCompatActivity {

    RequestMask thisRequest;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_request);

        String name = getIntent().getStringExtra("requestItem");
        Log.d("request Item", name);
        Log.d("request Item", name + "  " +RequestManager.getInstance().getAllMaks().contains("name"));
        thisRequest = RequestManager.getInstance().getMask(name);

        //creating the entries for the Request Masks
        ArrayList<String> entrys = thisRequest.getRequests();
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, entrys);

        listView = findViewById(R.id.oids_list);
        listView.setAdapter(itemsAdapter);

    }
}
