package de.uni_stuttgart.informatik.sopra.sopraapp.Requests;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

public class RequestMngActivity extends AppCompatActivity {

    private final Activity activity = this;
    private RequestDbHelper manager;

    private RecyclerView listView;
    private RecyclerView.Adapter itemsAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_mng);
        manager = new RequestDbHelper(this);

        listView = findViewById(R.id.request_list);
        listView.setLayoutManager(new LinearLayoutManager(this));
        itemsAdapter = new OverviewAdapter(manager, this);
        listView.setAdapter(itemsAdapter);

        if(!alreadyExists("Standardabfrage")) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(RequestsContract.COLUMN_REQ_NAME, "Standardabfrage");
            manager.getWritableDatabase().insert(RequestsContract.REQ_TABLE_NAME, null, contentValues);
        }
    }

    public void addMask(View view) {
        SQLiteDatabase database = manager.getWritableDatabase();

        String name = "Abfragemaske";
        if(alreadyExists(name)){
            int count = 0;
            do {
                name = "Abfragemaske " + count;
                count++;
            }while(alreadyExists(name));
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(RequestsContract.COLUMN_REQ_NAME, name);

        database.insert(RequestsContract.REQ_TABLE_NAME, null, contentValues);

        itemsAdapter.notifyDataSetChanged();

    }

    private boolean alreadyExists(String name){
        SQLiteDatabase myDatabase = manager.getReadableDatabase();
        Cursor c = myDatabase.rawQuery("select * from " + RequestsContract.REQ_TABLE_NAME + " where " + RequestsContract.COLUMN_REQ_NAME + " = '" + name+"'", null);
        int amount = c.getCount();
        c.close();
        return amount >0;
    }
}
