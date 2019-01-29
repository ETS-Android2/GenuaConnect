package de.uni_stuttgart.informatik.sopra.sopraapp.requests;

import android.app.Instrumentation;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;

import net.bytebuddy.implementation.bind.annotation.Super;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Request;

import static org.junit.Assert.*;

public class RequestDbHelperTest {

    private RequestDbHelper requestDbHelper;

    @Before
    public void setUp(){
        requestDbHelper = new RequestDbHelper(InstrumentationRegistry.getTargetContext());
        requestDbHelper.truncateDatabase();
    }

    @After
    public void setDown(){
        requestDbHelper.close();
    }

    @Test
    public void testCondition(){
        assertNotNull(requestDbHelper);
    }

    @Test
    public void testNewMask(){
        SQLiteDatabase database = requestDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RequestsContract.COLUMN_REQ_NAME, "newMask");
        database.insert(RequestsContract.REQ_TABLE_NAME, null, contentValues);

        assertTrue(requestDbHelper.getAllMasks().contains("newMask"));

    }

    @Test
    public void deleteNewMask(){
        SQLiteDatabase database = requestDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RequestsContract.COLUMN_REQ_NAME, "newMask");
        database.insert(RequestsContract.REQ_TABLE_NAME, null, contentValues);

        assertTrue(requestDbHelper.getAllMasks().contains("newMask"));

        assertEquals(1,database.delete(RequestsContract.REQ_TABLE_NAME, RequestsContract.COLUMN_REQ_NAME + " = 'newMask'", null));
    }

}