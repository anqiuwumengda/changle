// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   Database.java

package hlc.base.db;

import java.io.Serializable;
import java.util.Hashtable;

class ConnSet
    implements Serializable
{

    protected int checkCode;
    protected String connStr;
    protected String dbCharset;
    protected String dbDriver;
    protected String dbName;
    protected String fileEncoding;
    protected boolean isDB2;
    protected boolean isDefault;
    protected boolean isInformix;
    protected boolean isMySQL;
    protected boolean isODBC;
    protected boolean isOracle;
    protected boolean isOtherDataSource;
    protected boolean isSQLServer;
    protected boolean isSameCharset;
    protected boolean isSybase;
    protected String jndiName;
    protected Hashtable mapConn;
    protected String password;
    protected int transactionIsolation;
    protected String user;

    public ConnSet()
    {
        dbName = "";
        dbDriver = "";
        connStr = "";
        dbCharset = "GBK";
        fileEncoding = "GBK";
        checkCode = 0;
        user = "";
        password = "";
        transactionIsolation = 2;
        mapConn = new Hashtable();
        isDefault = false;
        isOtherDataSource = false;
        jndiName = "";
        isSameCharset = true;
        isSQLServer = false;
        isInformix = false;
        isDB2 = false;
        isOracle = false;
        isMySQL = false;
        isSybase = false;
        isODBC = false;
    }
}
