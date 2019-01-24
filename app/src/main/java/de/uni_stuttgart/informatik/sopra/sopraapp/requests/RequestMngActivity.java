package de.uni_stuttgart.informatik.sopra.sopraapp.requests;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * This class is for the Activity of the Abfragenmanager.
 */
public class RequestMngActivity extends AppCompatActivity {

    private RequestDbHelper manager;

    private RecyclerView.Adapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_mng);
        manager = new RequestDbHelper(this);

        RecyclerView listView = findViewById(R.id.request_list);
        listView.setLayoutManager(new LinearLayoutManager(this));
        itemsAdapter = new OverviewAdapter(manager, this);
        listView.setAdapter(itemsAdapter);

        if (!alreadyExists(getString(R.string.standardAbfrage))) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(RequestsContract.COLUMN_REQ_NAME, getString(R.string.standardAbfrage));
            manager.getWritableDatabase().insert(RequestsContract.REQ_TABLE_NAME, null, contentValues);
        }
    }

    /**
     * Durch diese Methode werden Abfragemasken erstellt.
     *
     * @param view The View for the query masks.
     */
    public void addMask(View view) {
        SQLiteDatabase database = manager.getWritableDatabase();

        String name = "Abfragemaske";
        if (alreadyExists(name)) {
            int count = 0;
            do {
                name = "Abfragemaske " + count;
                count++;
            } while (alreadyExists(name));
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(RequestsContract.COLUMN_REQ_NAME, name);

        database.insert(RequestsContract.REQ_TABLE_NAME, null, contentValues);

        itemsAdapter.notifyDataSetChanged();

    }

    /**
     * Checks whether the query mask already exists or not.
     *
     * @param name Name der Abfragemaske.
     * @return Returnt die Anzahl zurück der schon existierenden Abfragemaske.
     */
    private boolean alreadyExists(String name) {
        SQLiteDatabase myDatabase = manager.getReadableDatabase();
        Cursor c = myDatabase.rawQuery("select * from " + RequestsContract.REQ_TABLE_NAME + " where " + RequestsContract.COLUMN_REQ_NAME + " = '" + name + "'", null);
        int amount = c.getCount();
        c.close();
        return amount > 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemsAdapter.notifyDataSetChanged();
    }
}