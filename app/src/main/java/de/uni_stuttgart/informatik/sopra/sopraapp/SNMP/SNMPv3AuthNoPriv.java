package de.uni_stuttgart.informatik.sopra.sopraapp.SNMP;

import android.app.Activity;

import org.snmp4j.Target;
import org.snmp4j.UserTarget;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;

/**
 * A client for the SNMP version3 AuthNoPriv management.
 * Maybe will be done in next sprint.
 * TODO
 */
public class SNMPv3AuthNoPriv extends SimpleSNMPClientv3 {
    public SNMPv3AuthNoPriv(String qrCode) {
        super(qrCode);
    }

    @Override
    protected Target getTarget() {
        Address targetAdress = GenericAddress.parse(address);
        UserTarget target = new UserTarget();
        target.setAddress(targetAdress);
        target.setRetries(2);
        target.setTimeout(5000);
        target.setVersion(SnmpConstants.version3);
        target.setSecurityLevel(SecurityLevel.AUTH_NOPRIV);
        target.setSecurityName(new OctetString("MD5DES"));
        return target;
    }
}
