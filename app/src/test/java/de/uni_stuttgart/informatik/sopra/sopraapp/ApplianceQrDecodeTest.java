package de.uni_stuttgart.informatik.sopra.sopraapp;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.cameraQrCode.ApplianceQrDecode;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ApplianceQrDecodeTest {
    ApplianceQrDecode applianceQrDecode = new ApplianceQrDecode("{\n" +
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
            "}");

    ApplianceQrDecode wrongApplianceQrDecode = new ApplianceQrDecode("{\n" +
            "\"snmpVersion\":\"7\",\n" +
            "\"user\": \"wurzel1\",\n" +
            "\"target\": \"public\",\n" +
            "\"pw\": \"asdf121!\",\n" +
            "\"enc\": {\n" +
            "  \"auth\": \"SAH\",\n" +
            "  \"priv\": \"AES-265\",\n" +
            "  \"privKey\": \"assdf121!\"\n" +
            "},\n" +
            "\"naddr\": {\n" +
            " \"IPv4\": \"-192.168.1.25\",\n" +
            " \"IPv6\": \"2a56:0:3\"\n" +
            "}\n" +
            "}");

    @Test
    public void getAddress() {
        String address = applianceQrDecode.getAddress();
        String fakeAddress = wrongApplianceQrDecode.getAddress();
        assertEquals("192.168.0.25", address);
        assertNotEquals(address,fakeAddress);
    }

    @Test
    public void getAddressV6() {
        String address = applianceQrDecode.getAddressV6();
        String fakeAddress = wrongApplianceQrDecode.getAddressV6();
        assertEquals("2a56:0:1", address);
        assertNotEquals(address,fakeAddress);
    }

    @Test
    public void getSnmpVersion() {
        String version= applianceQrDecode.getSnmpVersion();
        String fakeVersion = wrongApplianceQrDecode.getSnmpVersion();
        assertEquals("3", version);
        assertNotEquals(version,fakeVersion);
    }

    @Test
    public void getAuthProtocol() {
        String authP = applianceQrDecode.getAuthProtocol();
        String fakeAuthP = wrongApplianceQrDecode.getAuthProtocol();
        assertEquals("SHA", authP);
        assertNotEquals(authP,fakeAuthP);
    }

    @Test
    public void getPrivProtocol() {
    }

    @Test
    public void getPrivKey() {
    }

    @Test
    public void getTarget() {
    }

    @Test
    public void getUsername() {
    }

    @Test
    public void getPassword() {
    }
}