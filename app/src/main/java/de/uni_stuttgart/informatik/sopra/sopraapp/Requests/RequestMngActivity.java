package de.uni_stuttgart.informatik.sopra.sopraapp.Requests;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

public class RequestMngActivity extends AppCompatActivity {

    RequestManager manager;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_mng);
        manager = RequestManager.getInstance();

        //creating the entries for the Request Masks
        ArrayList<String> entrys = new ArrayList<>();
        for (String requestMask : manager.getAllMaks()) {
            entrys.add(requestMask);
        }
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, entrys);

        listView = findViewById(R.id.request_list);
        listView.setAdapter(itemsAdapter);

        //
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getParent(), CustomizeRequestActivity.class);
                intent.putExtra("requestItem", (String)parent.getSelectedItem());
                startActivity(intent);
            }
        });

    }
}
