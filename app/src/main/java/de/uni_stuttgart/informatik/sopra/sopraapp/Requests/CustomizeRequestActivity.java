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

 import de.uni_stuttgart.informatik.sopra.sopraapp.R;

public class CustomizeRequestActivity extends AppCompatActivity {

    private RequestDbHelper manager;
    private RecyclerView listView;
    private RecyclerView.Adapter adapter;

    private int requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_request);
        manager = new RequestDbHelper(this);

        listView = findViewById(R.id.oids_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        requestId = getIntent().getIntExtra("requestId", 0);
        Log.d("CustomizeRequestActivit", "Id : " + requestId);
        adapter = new CustomizeAdapter(manager, requestId);
        listView.setAdapter(adapter);

        //ImageButton renameButton = new ImageButton(this);
        //Drawable icon = getDrawable(android.support.fragment.R.drawable.ed);
        //renameButton.setImageDrawable();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SQLiteDatabase titleGetter = manager.getReadableDatabase();
        Cursor cursor = titleGetter.rawQuery("select * from " + RequestsContract.REQ_TABLE_NAME + " where " + RequestsContract.COLUMN_REQ_ID + " = " + requestId, null);
        cursor.moveToFirst();
        String request = cursor.getString(cursor.getColumnIndex(RequestsContract.COLUMN_REQ_NAME));
        setTitle(request);
        cursor.close();
    }

    public void saveOIDs(View view) {
        SQLiteDatabase database = manager.getWritableDatabase();
        ContentValues[] newRows = new ContentValues[adapter.getItemCount()];

        for(int pos = adapter.getItemCount()-1; pos>=0; pos--){
            CustomizeAdapter.ViewHolder element =(CustomizeAdapter.ViewHolder) listView.findViewHolderForLayoutPosition(pos);
            assert element != null;
            String oid = element.editText.getText().toString();
            Log.d("saveOID String", "oid");

            ContentValues contentValues = new ContentValues();
            contentValues.put(RequestsContract.COLUMN_OID_REQ, requestId);
            contentValues.put(RequestsContract.COLUMN_OID_STRING, oid);

            newRows[pos] = contentValues;
        }
        database.delete(RequestsContract.OID_TABLE_NAME, RequestsContract.COLUMN_OID_REQ + " = " + requestId, null);

        for (int pos = (newRows.length-1); pos>=0; pos--) {
            database.insert(RequestsContract.OID_TABLE_NAME, null, newRows[pos]);
        }
        database.close();
        adapter.notifyDataSetChanged();
    }

    public void addOID(View view) {
        saveOIDs(view);
        SQLiteDatabase database = manager.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RequestsContract.COLUMN_OID_STRING, "");
        contentValues.put(RequestsContract.COLUMN_OID_REQ, requestId);
        database.insert(RequestsContract.OID_TABLE_NAME, null, contentValues);
        database.close();
        adapter.notifyDataSetChanged();
    }
}
