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
        applianceInfos.put("snmpVersion", findInJSON(qrCode, "snmpVersion"));
        applianceInfos.put("user", findInJSON(qrCode, "user"));
        applianceInfos.put("target", findInJSON(qrCode, "target"));
        applianceInfos.put("auth", findInJSON(qrCode, "auth"));
        applianceInfos.put("priv", findInJSON(qrCode, "priv"));
        applianceInfos.put("privKey", findInJSON(qrCode, "privKey"));
        applianceInfos.put("pw", findInJSON(qrCode, "pw"));
        applianceInfos.put("IPv4", findInJSON(qrCode, "IPv4"));
        applianceInfos.put("IPv6", findInJSON(qrCode, "IPv6"));
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

    /**
     * Findet einen Parameter in der JSON Datei
     *
     * @param jSonString Die JSON Datei als String.
     * @param valueOf    Der gesuchte Parameter als String.
     * @return Returned den Wert des Parameters.
     */
    private static String findInJSON(String jSonString, String valueOf) {
        int index = jSonString.lastIndexOf("\"" + valueOf + "\"");
        if (index == -1) {
            return null;
        }
        for (int i = index; jSonString.charAt(index) != ':'; index++) {
        }
        for (int i = index; jSonString.charAt(index) != '"'; index++) {
        }
        index++;
        StringBuilder buildValue = new StringBuilder();
        for (int i = index; jSonString.charAt(i) != '"'; i++) {
            buildValue.append(jSonString.charAt(i));
        }

        return buildValue.toString();

    }
}
