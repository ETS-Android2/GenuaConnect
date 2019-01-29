package de.uni_stuttgart.informatik.sopra.sopraapp.cameraQrCode;

import android.support.test.InstrumentationRegistry;

import org.junit.After;

import static org.junit.Assert.*;

public class ReactionControllerTest {
    String snmp3 = "{\n" +
            "\"snmpVersion\":\"3\",\n" +
            "\"user\": \"root\",\n" +
            "\"target\": \"private\",\n" +
            "\"pw\": \"asdf212!\",\n" +
            "\"enc\": {\n" +
            "  \"auth\": \"SHA\",\n" +
            "  \"priv\": \"AES-128\",\n" +
            "  \"privKey\": \"asdf121!\"\n" +
            "},\n" +
            "\"naddr\": {\n" +
            " \"IPv4\": \"192.168.0.25\",\n" +
            " \"IPv6\": \"2a56:0:1\"\n" +
            "}\n" +
            "}";
    String snmp = "{\n" +
            "\"snmpVersion\":\"3\",\n" +
            "\"user\": \"\",\n" +
            "\"target\": \"private\",\n" +
            "\"pw\": \"asdf212!\",\n" +
            "\"enc\": {\n" +
            "  \"auth\": \"SHA\",\n" +
            "  \"priv\": \"AES-128\",\n" +
            "  \"privKey\": \"asdf121!\"\n" +
            "},\n" +
            "\"naddr\": {\n" +
            " \"IPv4\": \"192.168.0.25\",\n" +
            " \"IPv6\": \"2a56:0:1\"\n" +
            "}\n" +
            "}";
    String wifi = "WIFI:<SSID>;T:<WPA>;P:<password>;;";

    @After
    public void setWifi(){
        ReactionController reactionController = new ReactionController(InstrumentationRegistry.getTargetContext(), wifi);

    }

}