package com.cilla_project.gozar.DataBase;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cilla_project.gozar.G;
import java.util.ArrayList;

public class DB extends SQLiteOpenHelper {
    private static final String DB_NAME = "pb.sqlite";
    private static final ArrayList<String> TABLE_SCHEMA = new ArrayList<>();
    private static int DB_VERSION = 1;

    public DB() {
        super(G.Context, DB_NAME, null, DB_VERSION);
        TABLE_SCHEMA.add("CREATE  TABLE  IF NOT EXISTS \"persons\" (" +
                "\"id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " +
                "\"itemname\" VARCHAR, " +
                "\"imglogo\" VARCHAR " +

                ")");
    }
//    public DB2() {
//        super(G.Context, DB_NAME, null, DB_VERSION);
//        TABLE_SCHEMA.add("CREATE  TABLE  IF NOT EXISTS \"persons\" (" +
//                "\"id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " +
//                "\"itemname\" VARCHAR, " +
//                "\"telephone\" VARCHAR, " +
//                "\"website\" VARCHAR, " +
//                "\"imglogo\" VARCHAR, " +
//                "\"email\" VARCHAR, " +
//                "\"area\" VARCHAR, " +
//                "\"address\" VARCHAR" +
//                ")");
//    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        create(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        drop(db);
        create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        drop(db);
        create(db);
    }

    private void create(SQLiteDatabase db) {
        db.beginTransaction();
        for (String createTable : TABLE_SCHEMA) {
            db.execSQL(createTable);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void drop(SQLiteDatabase db) {
//        db.execSQL("DROP TABLE IF EXISTS \"persons\"");
    }

//    private void drop(SQLiteDatabase db) {
//        db.execSQL("DROP TABLE IF EXISTS \"persons\"");
//    }
}
