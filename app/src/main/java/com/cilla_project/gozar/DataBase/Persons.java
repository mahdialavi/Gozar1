package com.cilla_project.gozar.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.cilla_project.gozar.G;
import com.cilla_project.gozar.JobItemsList;
import java.util.ArrayList;

public class Persons {
    static int id;
    public static String favState;
    public static String tableName = "persons";

    public static ArrayList<JobItemsList> all() {
        ArrayList<JobItemsList> result = new ArrayList<>();
        Cursor cursor;
        cursor = G.DB.rawQuery("SELECT * FROM \"" + tableName + "\"", null);
        while (cursor.moveToNext()) {
            result.add(extract(cursor));
        }
        cursor.close();
        return result;
    }


    //
    public static boolean selectFavState(String id) {

        Cursor cursor = G.DB.rawQuery("SELECT * FROM \"" + tableName + "\" WHERE (\"id\"=?)", new String[]{"" + id});
        while (cursor.moveToNext()) {
            int takeFav = cursor.getInt(cursor.getColumnIndex("featured"));

            favState = String.valueOf(takeFav);
        }
        if (favState.equals("1")) {
            return true;

        } else {
            return false;
        }
    }

    public static boolean update(JobItemsList person) {
        ContentValues values = getContentValues(person);
        return G.DB.update(tableName, values, "\"id\"=?", new String[]{"" + person.id}) > 0;
    }

    public static void clear() {
        G.DB.delete(tableName, "1", null);
    }

    public static boolean clearcategory(int catid) {
        return G.DB.delete(tableName, "\"catid\"=?", new String[]{"" + catid}) > 0;
    }

    public static boolean delete(int id) {
        return G.DB.delete(tableName, "\"id\"=?", new String[]{"" + id}) > 0;
    }

    public static boolean exists(int id) {
        return one(id) != null;
    }

    public static boolean insert(JobItemsList person) {
        ContentValues values = getContentValues(person);
        return G.DB.insert(tableName, null, values) != -1;
    }

    public static JobItemsList one(int id) {
        JobItemsList result = null;
        Cursor cursor = G.DB.rawQuery("SELECT * FROM \"" + tableName + "\" WHERE (\"id\"=?)", new String[]{"" + id});
        if (cursor.moveToNext()) {
            result = extract(cursor);
        }
        cursor.close();
        return result;
    }

    public static boolean save(JobItemsList itemsList) {
        if (!exists(itemsList.id)) {
            return insert(itemsList);
        }
        return update(itemsList);
    }

    @NonNull
    private static ContentValues getContentValues(JobItemsList itemsList) {
        ContentValues values = new ContentValues();
        if (itemsList.id != 0) {
            values.put("id", itemsList.id);
        }
        values.put("itemname", itemsList.name);
        values.put("imglogo", itemsList.image);
        return values;
    }

    public static JobItemsList extract(Cursor cursor) {
        JobItemsList result = new JobItemsList();
        result.id = cursor.getInt(cursor.getColumnIndex("id"));
        result.name= cursor.getString(cursor.getColumnIndex("itemname"));
        result.image= cursor.getString(cursor.getColumnIndex("imglogo"));

        return result;
    }
}
