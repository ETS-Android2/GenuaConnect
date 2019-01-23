package de.uni_stuttgart.informatik.sopra.sopraapp.requests;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * Diese Klasse ist die Adapterklasse für die Oberfläche der Abfragemasken.
 */
public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.ViewHolder> {
    private static final String TAG = "OverviewAdapter";

    private RequestDbHelper data;
    private Activity activity;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView requestText;
        ImageButton imageButton;

        ViewHolder(View view) {
            super(view);
            requestText = view.findViewById(R.id.reqText);
            imageButton = view.findViewById(R.id.deleteReq_btn);

            imageButton.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                SQLiteDatabase database = data.getWritableDatabase();

                Cursor cursor = database.rawQuery("select * from " + RequestsContract.REQ_TABLE_NAME +
                        " order by " + RequestsContract.COLUMN_REQ_ID + " desc limit 1 offset " + pos, null);

                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex(RequestsContract.COLUMN_REQ_ID));
                cursor.close();

                database.delete(RequestsContract.REQ_TABLE_NAME, RequestsContract.COLUMN_REQ_ID + " = " + id, null);
                notifyDataSetChanged();
            });

            requestText.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                SQLiteDatabase database = data.getWritableDatabase();

                Cursor cursor = database.rawQuery("select * from " + RequestsContract.REQ_TABLE_NAME +
                        " order by " + RequestsContract.COLUMN_REQ_ID + " desc limit 1 offset " + pos, null);

                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex(RequestsContract.COLUMN_REQ_ID));
                cursor.close();

                Intent intent = new Intent(activity, CustomizeRequestActivity.class);
                intent.putExtra("requestId", id);
                Log.d(TAG, "onClick: starting " + id);
                activity.startActivity(intent);


            });
        }
    }

    /**
     * Konstruktor.
     *
     * @param requestDbHelper Objekt aus RequestDbHelper.
     * @param activity        Die übergebene Activity.
     */
    OverviewAdapter(RequestDbHelper requestDbHelper, Activity activity) {
        this.data = requestDbHelper;
        this.activity = activity;
    }

    @NonNull
    @Override
    public OverviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SQLiteDatabase database = data.getReadableDatabase();

        //getting all different request masks
        Cursor cursor = database.rawQuery("select * from " + RequestsContract.REQ_TABLE_NAME +
                " order by " + RequestsContract.COLUMN_REQ_ID + " desc limit 1 offset " + position, null);

        cursor.moveToFirst();
        String req = cursor.getString(cursor.getColumnIndex(RequestsContract.COLUMN_REQ_NAME));
        cursor.close();

        holder.requestText.setText(req);
    }

    @Override
    public int getItemCount() {
        SQLiteDatabase database = data.getReadableDatabase();

        Cursor cursor = database.rawQuery("select * from " + RequestsContract.REQ_TABLE_NAME, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        return cursorCount;
    }


}
