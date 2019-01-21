package de.uni_stuttgart.informatik.sopra.sopraapp.monitoring;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import de.uni_stuttgart.informatik.sopra.sopraapp.*;
import de.uni_stuttgart.informatik.sopra.sopraapp.requests.RequestDbHelper;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SimpleSNMPClientV1AndV2c;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SimpleSNMPClientv3;

/**
 * Dies ist der SNMP Manager. Hiermit können die beiden SNMP Klassen mit Ihrem Task gemanaged werden.
 */
public class SnmpAdapter extends ArrayAdapter<SimpleSNMPClientV1AndV2c> {

    private ArrayList<SimpleSNMPClientV1AndV2c> elements;
    private Context context;
    private RequestDbHelper dbHelper;
    private ApplianceManager manager;

    /**
     * Konstruktor.
     *
     * @param context Context der Klasse.
     */
    public SnmpAdapter(Context context) {
        super(context, 0, ApplianceManager.getInstance(context).getClientList());
        manager = ApplianceManager.getInstance(context);
        elements = manager.getClientList();
        this.context = context;
        dbHelper = new RequestDbHelper(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(context).inflate(R.layout.snmp_appl_item, parent, false);
        }

        final SimpleSNMPClientV1AndV2c client = elements.get(position);

        final Switch req_switch = listItem.findViewById(R.id.switch1);
        req_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
            }
        });

        Spinner spinner = listItem.findViewById(R.id.request_spinner);
        spinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,dbHelper.getAllMasks()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                req_switch.setEnabled(true);
                String req = (String) parent.getSelectedItem();
                manager.setRequestFor(client,req);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                req_switch.setChecked(false);
                req_switch.setEnabled(false);
            }
        });
        spinner.setSelection(dbHelper.getAllMasks().indexOf(manager.getRequestMaskFrom(client)));

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

        if(req_switch.isChecked()){
            infos.addAll(manager.getResults(client));
        }
        listView.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, infos));

        return listItem;
    }
}
