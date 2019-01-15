package de.uni_stuttgart.informatik.sopra.sopraapp.Monitoring;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import de.uni_stuttgart.informatik.sopra.sopraapp.SNMP.SimpleSNMPClientV1AndV2c;

public class SnmpAdapter extends ArrayAdapter<SimpleSNMPClientV1AndV2c> {

    private ArrayList<SimpleSNMPClientV1AndV2c> elements;
    private Context context;

    public SnmpAdapter(Context context, List<SimpleSNMPClientV1AndV2c> objects) {
        super(context, 0, objects);
        this.context = context;
        elements = (ArrayList<SimpleSNMPClientV1AndV2c>)objects;
    }


}
