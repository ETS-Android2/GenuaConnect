 package de.uni_stuttgart.informatik.sopra.sopraapp.Requests;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

public class CustomizeRequestActivity extends AppCompatActivity {


    private RequestDbHelper manager;
    private RecyclerView listView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private int requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_request);
        manager = new RequestDbHelper(this);

        listView = findViewById(R.id.oids_list);
        layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        requestId = getIntent().getIntExtra("requestId", 0);
        Log.d("CustomizeRequestActivity", "Id : " + requestId);
        adapter = new CustomizeAdapter(manager, requestId);
        listView.setAdapter(adapter);


    }

    public void saveOIDs(View view) {

    }

    public void addOID(View view) {
        SQLiteDatabase database = manager.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(RequestsContract.COLUMN_OID_STRING, "");
        contentValues.put(RequestsContract.COLUMN_OID_REQ, requestId);
        database.insert(RequestsContract.OID_TABLE_NAME, null, contentValues);

        adapter.notifyDataSetChanged();
    }
}
