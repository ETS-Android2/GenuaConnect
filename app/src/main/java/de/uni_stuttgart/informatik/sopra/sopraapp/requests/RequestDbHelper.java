package de.uni_stuttgart.informatik.sopra.sopraapp.requests;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * this is the Helper class in the database. The database is here initialized.
 * Dies ist die Helper Klasse der Datenbank. Hier werden die Datenbanken initialisiert.
 */
public class RequestDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 13;
    private static final String DATABASE_NAME = "Requests.db";

    //sql command to destroy request table
    private static final String SQL_DELETE_REQUESTS =
            "DROP TABLE IF EXISTS " + RequestsContract.REQ_TABLE_NAME;

    //sql command to destroy oid table
    private static final String SQL_DELETE_OID =
            "DROP TABLE IF EXISTS " + RequestsContract.OID_TABLE_NAME;

    public RequestDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //sql command to create requestmask table(for the mask not oids)
        final String SQL_CREATE_REQUESTS =
                "CREATE TABLE " + RequestsContract.REQ_TABLE_NAME + " (" +
                        RequestsContract.COLUMN_REQ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RequestsContract.COLUMN_REQ_NAME + " TEXT);";


        final String SQL_CREATE_OIDS =
                "CREATE TABLE " + RequestsContract.OID_TABLE_NAME + " (" +
                        RequestsContract.COLUMN_OID_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RequestsContract.COLUMN_OID_STRING + " TEXT, " +
                        RequestsContract.COLUMN_OID_DESCRIPT + " TEXT, " +
                        RequestsContract.COLUMN_OID_REQ + " INTEGER);";

        db.execSQL(SQL_CREATE_REQUESTS);
        db.execSQL(SQL_CREATE_OIDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_REQUESTS);
        db.execSQL(SQL_DELETE_OID);
        onCreate(db);
    }

    public void truncateDatabase(){
        onUpgrade(getWritableDatabase(), 13, 13);
    }

    /**
     * Through this method the query masks can be getted.
     *
     * @return Returns the query mask.
     */
    public ArrayList<String> getAllMasks() {
        SQLiteDatabase reading = getReadableDatabase();
        ArrayList<String> masks = new ArrayList<>();

        //query for the request table
        Cursor cursor = reading.rawQuery("select * from " + RequestsContract.REQ_TABLE_NAME, null);
        String mask;
        cursor.moveToFirst();

        //iterate over all elements of query and get the request name
        for (int i = 1; i <= cursor.getCount(); i++) {
            mask = cursor.getString(cursor.getColumnIndex(RequestsContract.COLUMN_REQ_NAME));
            masks.add(mask);
            cursor.moveToNext();
        }
        cursor.close();
        return masks;
    }

    /**
     * Getter for the OIDs for a certain query mask.
     *
     * @param request Name of the mask.
     * @return Returns the OIDs.
     */
    public ArrayList<OidElement> getOIDsFrom(String request) {
        SQLiteDatabase reading = getReadableDatabase();

        //finding the request parameter in the table
        Cursor cursor = reading.rawQuery("select * from " + RequestsContract.REQ_TABLE_NAME +
                " where " + RequestsContract.COLUMN_REQ_NAME + " = '" + request + "' ", null);
        cursor.moveToFirst();

        //getting the id of this request
        int id = cursor.getInt(cursor.getColumnIndex(RequestsContract.COLUMN_REQ_ID));
        cursor.close();

        ArrayList<OidElement> oids = new ArrayList<>();

        //finding the oid's with the matching request id
        Cursor cursorOid = reading.rawQuery("select * from " + RequestsContract.OID_TABLE_NAME +
                " where " + RequestsContract.COLUMN_OID_REQ + " = " + id, null);

        cursorOid.moveToFirst();

        //saving the column index of oid-string and description
        int columnIndexOid = cursorOid.getColumnIndex(RequestsContract.COLUMN_OID_STRING);
        int columnIndexDescr = cursorOid.getColumnIndex(RequestsContract.COLUMN_OID_DESCRIPT);

        //iterate over all elements of oid rows and get the oid string and the description
        for (int i = 1; i <= cursorOid.getCount(); i++) {
            String oid = cursorOid.getString(columnIndexOid);
            String des = cursorOid.getString(columnIndexDescr);
            oids.add(new OidElement(oid, des));
            cursorOid.moveToNext();
        }
        cursorOid.close();
        return oids;
    }

}
