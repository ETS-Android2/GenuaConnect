package de.uni_stuttgart.informatik.sopra.sopraapp.requests;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

public class OidAdapter extends ArrayAdapter<OidElement> {
    private static final String TAG = "OidAdapter";

    private RequestDbHelper requestDbHelper;
    private int requestId;
    private String requestName;
    private Context context;

    OidAdapter(Context context, RequestDbHelper dbHelper, int requestId, String requestName) {
        super(context, 0);

        this.context = context;
        requestDbHelper = dbHelper;
        this.requestName = requestName;
        this.requestId = requestId;
        addAll(requestDbHelper.getOIDsFrom(requestName));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View oidView = convertView;
        if (oidView == null) {
            oidView = LayoutInflater.from(context).inflate(R.layout.recycler_edit_layout, parent, false);
        }

        final OidElement element = getItem(position);
        EditText oidField = oidView.findViewById(R.id.oid_field);
        Log.d(TAG, "getView: requestName: " +requestName);
        String oid = requestDbHelper.getOIDsFrom(requestName).get(position).getOidString();
        oidField.setText(oid);

        EditText descText = oidView.findViewById(R.id.description_field);
        String des = requestDbHelper.getOIDsFrom(requestName).get(position).getDescription();
        descText.setText(des);

        ImageButton deleteImBtn = oidView.findViewById(R.id.delete_btn);
        deleteImBtn.setOnClickListener(v -> {
            SQLiteDatabase readableDatabase = requestDbHelper.getReadableDatabase();
            Cursor cursor = readableDatabase.rawQuery("select * from " + RequestsContract.OID_TABLE_NAME + " where " +
                    RequestsContract.COLUMN_OID_REQ + " = " + requestId + " order by " + RequestsContract.COLUMN_OID_ID, null);
            cursor.moveToPosition(position);
            int idToDel = cursor.getInt(cursor.getColumnIndex(RequestsContract.COLUMN_OID_ID));
            readableDatabase.delete(RequestsContract.OID_TABLE_NAME, RequestsContract.COLUMN_OID_ID + " = " + idToDel, null);
            super.remove(element);
            cursor.close();
        });

        descText.setEnabled(false);
        oidField.setEnabled(false);
        return oidView;
    }


    @Override
    public void notifyDataSetChanged() {
        setNotifyOnChange(false);
        SQLiteDatabase titleGetter = requestDbHelper.getReadableDatabase();
        Cursor cursor = titleGetter.rawQuery("select * from " + RequestsContract.REQ_TABLE_NAME + " where " + RequestsContract.COLUMN_REQ_ID + " = " + requestId, null);
        cursor.moveToFirst();
        requestName = cursor.getString(cursor.getColumnIndex(RequestsContract.COLUMN_REQ_NAME));
        clear();
        Log.d(TAG, "notifyDataSetChanged: oids from " + requestDbHelper.getOIDsFrom(requestName).size());
        addAll(requestDbHelper.getOIDsFrom(requestName));
        Log.d(TAG, "notifyDataSetChanged: elementsList " + getCount());
        super.notifyDataSetChanged();
    }
}
