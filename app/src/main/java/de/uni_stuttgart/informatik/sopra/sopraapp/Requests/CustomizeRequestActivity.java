package de.uni_stuttgart.informatik.sopra.sopraapp.Requests;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * Diese Klasse ist für den Aufbau einer Abfragemaske zuständig.
 */
public class CustomizeRequestActivity extends AppCompatActivity {

    private RequestDbHelper manager;
    private RecyclerView listView;
    private RecyclerView.Adapter adapter;

    private EditText requestNameField;

    private int requestId;
    private String requestName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_request);
        manager = new RequestDbHelper(this);

        requestNameField = findViewById(R.id.et_requestName);
        listView = findViewById(R.id.oids_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        requestId = getIntent().getIntExtra("requestId", 0);
        Log.d("CustomizeRequestActivit", "Id : " + requestId);
        adapter = new CustomizeAdapter(manager, requestId);
        listView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        SQLiteDatabase titleGetter = manager.getReadableDatabase();
        Cursor cursor = titleGetter.rawQuery("select * from " + RequestsContract.REQ_TABLE_NAME + " where " + RequestsContract.COLUMN_REQ_ID + " = " + requestId, null);
        cursor.moveToFirst();
        requestName = cursor.getString(cursor.getColumnIndex(RequestsContract.COLUMN_REQ_NAME));
        requestNameField.setText(requestName);

        cursor.close();
    }

    /**
     * Diese Methode ist für die Speicherfunktion zuständig.
     *
     * @param view Die View für die Speicherfunktion.
     */
    public void save(View view) {
        save();
    }

    /**
     * Diese Methode Speichert das zu speichernde Element in der Datenbank.
     */
    private void save() {
        SQLiteDatabase writableDatabase = manager.getWritableDatabase();
        String posNewName = requestNameField.getText().toString();
        if(manager.getAllMasks().contains(posNewName) && !posNewName.equals(requestName)){
            Toast.makeText(this, "The Request name already exists. Please change it to save your modification", Toast.LENGTH_LONG).show();
        }else if(!manager.getAllMasks().contains(posNewName)){
            ContentValues posChangedName = new ContentValues();
            posChangedName.put(RequestsContract.COLUMN_REQ_NAME, requestNameField.getText().toString());
            writableDatabase.update(RequestsContract.REQ_TABLE_NAME, posChangedName, RequestsContract.COLUMN_REQ_ID + " = " + requestId, null);
        }
        ContentValues[] newRows = new ContentValues[adapter.getItemCount()];

        for (int pos = adapter.getItemCount() - 1; pos >= 0; pos--) {
            CustomizeAdapter.ViewHolder element = (CustomizeAdapter.ViewHolder) listView.findViewHolderForLayoutPosition(pos);
            assert element != null;
            String oid = element.editText.getText().toString();
            Log.d("saveOID String", "oid");

            ContentValues contentValues = new ContentValues();
            contentValues.put(RequestsContract.COLUMN_OID_REQ, requestId);
            contentValues.put(RequestsContract.COLUMN_OID_STRING, oid);

            newRows[pos] = contentValues;
        }
        writableDatabase.delete(RequestsContract.OID_TABLE_NAME, RequestsContract.COLUMN_OID_REQ + " = " + requestId, null);

        for (int pos = (newRows.length - 1); pos >= 0; pos--) {
            writableDatabase.insert(RequestsContract.OID_TABLE_NAME, null, newRows[pos]);
        }
        writableDatabase.close();
        adapter.notifyDataSetChanged();
    }

    /**
     * Mit dieser Funktion kann man OIDs in die Datenbank hinzufügen.
     *
     * @param view Die View für das hinzufügen.
     */
    public void addOID(View view) {
        save(view);
        SQLiteDatabase database = manager.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RequestsContract.COLUMN_OID_STRING, "");
        contentValues.put(RequestsContract.COLUMN_OID_REQ, requestId);
        database.insert(RequestsContract.OID_TABLE_NAME, null, contentValues);
        database.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        save();
    }
}
