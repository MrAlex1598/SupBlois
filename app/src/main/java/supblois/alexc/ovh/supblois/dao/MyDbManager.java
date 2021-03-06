package supblois.alexc.ovh.supblois.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by ben on 10/12/17.
 */

public class MyDbManager implements IDbManager {

    private SQLiteDatabase database;
    private MyDB dbhelper;

    private IAccountDAO accountDAO;
    private IMessageDAO messageDAO;
    private IConnectedDAO connectedDAO;

    private static MyDbManager dbManager = null;

    private MyDbManager(Context context){
        dbhelper = new MyDB(context);
    }

    public static MyDbManager getInstance(Context context){
        if(dbManager == null){
            System.out.println("getInstance: creating instance...");
           dbManager = new MyDbManager(context);
        }
        return dbManager;
    }

    @Override
    public void open() throws SQLException {
        System.out.println("open: Accessing writable database...");
        database = dbhelper.getWritableDatabase();
        accountDAO = new MyAccountDAO(database);
        connectedDAO = new MyConnectedDao(database);
        messageDAO = new MyMessageDAO(database);
    }

    @Override
    public void close() {
        dbhelper.close();
    }

    @Override
    public IAccountDAO getAccountDAO() {
        return accountDAO;
    }

    @Override
    public IConnectedDAO getConnectedDAO() {
        return connectedDAO;
    }

    @Override
    public IMessageDAO getMessageDAO(){
        return messageDAO;
    }
}
