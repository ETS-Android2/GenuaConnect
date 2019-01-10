package de.uni_stuttgart.informatik.sopra.sopraapp.Requests;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

public class CustomizeAdapter extends RecyclerView.Adapter<CustomizeAdapter.ViewHolder> {
    private RequestDbHelper data;
    private int requestId;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public EditText editText;
        public ImageButton imageButton;

        public ViewHolder(View view) {
            super(view);
            editText = view.findViewById(R.id.oid_field);
            imageButton = view.findViewById(R.id.delete_btn);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    SQLiteDatabase database = data.getWritableDatabase();

                    Cursor cursor = database.rawQuery("select * from " + RequestsContract.OID_TABLE_NAME + " where " + RequestsContract.COLUMN_OID_REQ + " = " + requestId +
                            " order by " + RequestsContract.COLUMN_REQ_ID + " desc limit 1 offset " + pos, null);

                    cursor.moveToFirst();
                    int id = cursor.getInt(cursor.getColumnIndex(RequestsContract.COLUMN_REQ_ID));
                    cursor.close();

                    database.delete(RequestsContract.OID_TABLE_NAME, RequestsContract.COLUMN_OID_ID + " = " + id, null);
                    notifyDataSetChanged();
                }
            });
        }
    }
    public CustomizeAdapter(RequestDbHelper requestDbHelper, int id) {
        this.data = requestDbHelper;
        this.requestId = id;
    }

    @Override
    public CustomizeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_edit_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SQLiteDatabase database = data.getReadableDatabase();

        //getting all different request masks
        Cursor cursor = database.rawQuery("select * from " + RequestsContract.OID_TABLE_NAME + " where " + RequestsContract.COLUMN_OID_REQ + " = " + requestId +
                " order by " + RequestsContract.COLUMN_OID_ID + " desc limit 1 offset " + position, null);

        cursor.moveToFirst();
        String oid = cursor.getString(cursor.getColumnIndex(RequestsContract.COLUMN_OID_STRING));
        cursor.close();

        holder.editText.setText(oid);
    }

    @Override
    public int getItemCount() {
        SQLiteDatabase database = data.getReadableDatabase();

        Cursor cursor = database.rawQuery("select * from " + RequestsContract.OID_TABLE_NAME + " where " + RequestsContract.COLUMN_OID_REQ + " = " + requestId , null);
        return cursor.getCount();
    }




}
