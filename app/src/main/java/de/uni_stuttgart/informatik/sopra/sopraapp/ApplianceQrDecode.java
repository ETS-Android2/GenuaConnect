package de.uni_stuttgart.informatik.sopra.sopraapp;

import java.util.HashMap;

/**
 * Mit dieser Klasse werden die Infos aus dem QR Code gegettet.
 */
public class ApplianceQrDecode {

    private HashMap<String, String> applianceInfos;

    /**
     * Konstruktor
     *
     * @param qrCode Die Variable fuer die QR Code Infos.
     */
    public ApplianceQrDecode(String qrCode) {
        applianceInfos = new HashMap<>();
        fillMap(qrCode);
    }

    public String getAddress() {
        return applianceInfos.get("IPv4");
    }

    public String getAddressV6() {
        return applianceInfos.get("IPv6");
    }

    String getSnmpVersion() {
        return applianceInfos.get("snmpVersion");
    }

    public String getAuthProtocol() {
        return applianceInfos.get("auth");
    }

    public String getPrivProtocol() {
        return applianceInfos.get("priv");
    }

    public String getPrivKey() {
        return applianceInfos.get("privKey");
    }

    public String getTarget() {
        return applianceInfos.get("target");
    }

    public String getUsername() {
        return applianceInfos.get("user");
    }

    public String getPassword() {
        return applianceInfos.get("pw");
    }



    private  void fillMap(String jSon) {
        int i = 0;
        String key = "";
        String value;
        while (i < jSon.length()) {
            char c = jSon.charAt(i);
            switch (c) {
                case '"':
                    i++;
                    if (key.isEmpty()) {
                        key = jSon.substring(i, jSon.indexOf('"', i));
                        i = jSon.indexOf(':', i);
                    }else {
                        value = jSon.substring(i, jSon.indexOf('"', i));
                        i = jSon.indexOf(',', i);
                        applianceInfos.put(key, value);
                        key = "";
                    }
                    break;
                case '{':
                    key = "";
                    break;
            }
            if(i<0) {
                break;
            }
            i++;
        }
    }
}
