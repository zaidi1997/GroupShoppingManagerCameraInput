package sg.edu.rp.c346.groupshoppingmanager_camerainput;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shoppinglist.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_LIST = "note";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_QUANTITY = "quantity";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createShoppingListSql = "CREATE TABLE " + TABLE_LIST + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_QUANTITY + " INTEGER  ) ";
        db.execSQL(createShoppingListSql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);
        onCreate(db);
    }

    public long insertItem(String name, double price, int qty){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_QUANTITY, qty);

        long result = db.insert(TABLE_LIST, null, values);
        db.close();
        return result;
    }

    public ArrayList<ShoppingList> getAllItems() {
        ArrayList<ShoppingList> listItem = new ArrayList<ShoppingList>();

        String selectQuery = "SELECT " + COLUMN_ID + "," + COLUMN_NAME +  "," + COLUMN_PRICE  + "," + COLUMN_QUANTITY + " FROM " + TABLE_LIST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                double price = cursor.getDouble(2);
                int qty = cursor.getInt(3);
                ShoppingList obj = new ShoppingList(id, name, price, qty);
                listItem.add(obj);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listItem;
    }

    public int updateItem(ShoppingList data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, data.getName());
        values.put(COLUMN_PRICE, data.getPrice());
        values.put(COLUMN_QUANTITY, data.getQty());

        String condition = COLUMN_ID + "= ?";

        String[] args = {String.valueOf(data.getId())};
        int result = db.update(TABLE_LIST, values, condition, args);
        db.close();
        return result;
    }

    public int deleteItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(id)};
        int result = db.delete(TABLE_LIST, condition, args);
        db.close();
        return result;
    }
}
