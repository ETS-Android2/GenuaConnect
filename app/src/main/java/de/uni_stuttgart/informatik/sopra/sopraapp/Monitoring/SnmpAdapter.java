package de.uni_stuttgart.informatik.sopra.sopraapp.Monitoring;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import de.uni_stuttgart.informatik.sopra.sopraapp.SNMP.SimpleSNMPClientV1AndV2c;

/**
 * Dies ist der SNMP Manager. Hiermit können die beiden SNMP Klassen mit Ihrem Task gemanaged werden.
 */
public class SnmpAdapter extends ArrayAdapter<SimpleSNMPClientV1AndV2c> {

    private ArrayList<SimpleSNMPClientV1AndV2c> elements;
    private Context context;

    /**
     * Konstruktor.
     *
     * @param context Context der Klasse.
     * @param objects Ein Objekt aus der SNMP Klasse.
     */
    public SnmpAdapter(Context context, List<SimpleSNMPClientV1AndV2c> objects, RequestDbHelper data) {
        super(context, 0, objects);
        this.context = context;
        elements = (ArrayList<SimpleSNMPClientV1AndV2c>)objects;
        dbHelper = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(context).inflate(R.layout.snmp_appl_item, parent, false);
        }

        SimpleSNMPClientV1AndV2c client = elements.get(position);

        Spinner spinner = listItem.findViewById(R.id.request_spinner);
        spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,dbHelper.getAllMasks()));

        TextView textView = listItem.findViewById(R.id.appl_name_field);
        textView.setText("Gerät " + position);

        ListView listView = listItem.findViewById(R.id.appl_info_list);
        ArrayList<String> infos = new ArrayList<>();
        infos.add("IPv4-Addresse:\t"+ client.getAddress());
        if(client instanceof SimpleSNMPClientv3){
            infos.add("Benutzer:\t"+ client.getTarget().getSecurityName().toString());
        }else {
            infos.add("Community Target:\t" + client.getTarget().getSecurityName().toString());
        }
        listView.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, infos));

        return listItem;
    }
}
