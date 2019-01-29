package de.uni_stuttgart.informatik.sopra.sopraapp.cameraQrCode;

import java.util.HashMap;

/**
 * This class enables that the information from the QR-Code can be getted.
 */
public class ApplianceQrDecode {

    private HashMap<String, String> applianceInfos;

    /**
     * Constructor
     *
     * @param qrCode The variable for the QR-Code information.
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

    public String getSnmpVersion() {
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
                        key = jSon.substring(i, jSon.indexOf('"',i));
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
