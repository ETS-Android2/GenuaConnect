package de.uni_stuttgart.informatik.sopra.sopraapp.Requests;

/**
 * Hier sind die Stringdaten für die Datenbank gelistet.
 */
class RequestsContract {
    static final String REQ_TABLE_NAME = "Requests";
    static final String COLUMN_REQ_ID = "id";
    static final String COLUMN_REQ_NAME = "request";

    static final String OID_TABLE_NAME = "OID";
    static final String COLUMN_OID_ID = "id";
    static final String COLUMN_OID_STRING = "oid";
    static final String COLUMN_OID_REQ = "requestId";

    static final String DESCR_TABLE_NAME = "Description";
    static final String COLUMN_DES_ID  = "id";
    static final String COLUMN_DES_OID = "oid";
    static final String COLUMN_DES_TEXT = "DescriptionText";
}
