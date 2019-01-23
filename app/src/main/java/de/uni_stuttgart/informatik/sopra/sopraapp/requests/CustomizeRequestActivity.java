package de.uni_stuttgart.informatik.sopra.sopraapp.requests;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * Diese Klasse ist für den Aufbau einer Abfragemaske zuständig.
 */
public class CustomizeRequestActivity extends AppCompatActivity {

    private RequestDbHelper manager;
    private OidAdapter adapter;

    private EditText requestNameField;

    private int requestId;
    private String requestName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_request);
        manager = new RequestDbHelper(this);
        requestId = getIntent().getIntExtra("requestId", 0);

        SQLiteDatabase titleGetter = manager.getReadableDatabase();
        Cursor cursor = titleGetter.rawQuery("select * from " + RequestsContract.REQ_TABLE_NAME + " where " + RequestsContract.COLUMN_REQ_ID + " = " + requestId, null);
        cursor.moveToFirst();
        requestName = cursor.getString(cursor.getColumnIndex(RequestsContract.COLUMN_REQ_NAME));
        requestNameField = findViewById(R.id.et_requestName);
        requestNameField.setText(requestName);

        cursor.close();

        ListView listView = findViewById(R.id.oids_list);
        Log.d("CustomizeRequestActivit", "Id : " + requestId);
        adapter = new OidAdapter(this, manager, requestId, requestName);
        listView.setAdapter(adapter);

    }


    /**
     * Mit dieser Funktion kann man OIDs in die Datenbank hinzufügen.
     *
     * @param view Die View für das hinzufügen.
     */
    public void addOID(View view) {
        edit(this);
    }

    /**
     * Diese Methode ist für die Speicherfunktion zuständig.
     *
     * @param view Die View für die Speicherfunktion.
     */
    public void save(View view) {
        saving();
    }

    @Override
    protected void onPause() {
        saving();
        super.onPause();
    }

    private void saving() {
        SQLiteDatabase writableDatabase = manager.getWritableDatabase();
        String posNewName = requestNameField.getText().toString();
        if (manager.getAllMasks().contains(posNewName) && !posNewName.equals(requestName)) {
            Toast.makeText(this, "The Request name already exists. Please change it to save your modification", Toast.LENGTH_LONG).show();
        } else if (!manager.getAllMasks().contains(posNewName)) {
            ContentValues posChangedName = new ContentValues();
            posChangedName.put(RequestsContract.COLUMN_REQ_NAME, requestNameField.getText().toString());
            writableDatabase.update(RequestsContract.REQ_TABLE_NAME, posChangedName, RequestsContract.COLUMN_REQ_ID + " = " + requestId, null);
            requestName = posNewName;
        }
        adapter.notifyDataSetChanged();
    }


    private void edit(Context context) {
        LayoutInflater li = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View promptsView = li.inflate(R.layout.recycler_edit_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Comments");

        builder.setView(promptsView);

        final EditText oidField = promptsView.findViewById(R.id.oid_field);
        final EditText descriptField = promptsView.findViewById(R.id.description_field);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            // do something here on OK
            String oid = oidField.getText().toString();
            String desc = descriptField.getText().toString();

            ContentValues contentValues = new ContentValues();
            contentValues.put(RequestsContract.COLUMN_OID_REQ, requestId);
            contentValues.put(RequestsContract.COLUMN_OID_DESCRIPT, desc);
            contentValues.put(RequestsContract.COLUMN_OID_STRING, oid);

            manager.getWritableDatabase().insert(RequestsContract.OID_TABLE_NAME, null, contentValues);
            adapter.notifyDataSetChanged();

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    protected void onStop() {
        //saving();
        super.onStop();
    }
}
