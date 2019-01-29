package de.uni_stuttgart.informatik.sopra.sopraapp.cameraQrCode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.uni_stuttgart.informatik.sopra.sopraapp.cameraQrCode.ApplianceQrDecode;

import static org.junit.Assert.*;

/**
 * tests the ApplianceQrDecoder class and the methods by using a QR-Code String
 */
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
        String privP = applianceQrDecode.getPrivProtocol();
        String fakePrivP = wrongApplianceQrDecode.getPrivProtocol();
        assertEquals("AES-128", privP);
        assertNotEquals(privP,fakePrivP);
    }

    @Test
    public void getPrivKey() {
        String privK = applianceQrDecode.getPrivKey();
        String fakePrivK = wrongApplianceQrDecode.getPrivKey();
        assertEquals("asdf121!", privK);
        assertNotEquals(privK,fakePrivK);
    }

    @Test
    public void getTarget() {
        String target = applianceQrDecode.getTarget();
        String fakeTarget = wrongApplianceQrDecode.getTarget();
        assertEquals("private", target);
        assertNotEquals(target,fakeTarget);
    }

    @Test
    public void getUsername() {
        String user = applianceQrDecode.getUsername();
        String fakeUser = wrongApplianceQrDecode.getUsername();
        assertEquals("root", user);
        assertNotEquals(user,fakeUser);
    }

    @Test
    public void getPassword() {
        String pw = applianceQrDecode.getPassword();
        String fakePw = wrongApplianceQrDecode.getPassword();
        assertEquals("asdf212!", pw);
        assertNotEquals(pw,fakePw);
    }
}