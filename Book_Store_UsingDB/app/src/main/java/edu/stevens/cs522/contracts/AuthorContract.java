package edu.stevens.cs522.contracts;



        import android.content.ContentValues;
        import android.database.Cursor;


public class AuthorContract {

    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String BOOK_FK = "book_fk";


    public static String getName(Cursor cursor){
        return cursor.getString(cursor.getColumnIndexOrThrow(NAME));
    }

    public static void putName(ContentValues values, String name){
        values.put(NAME, name);
    }

    public static long getBookFk(Cursor cursor){
        return cursor.getLong(cursor.getColumnIndexOrThrow(BOOK_FK));
    }

    public static void putBookFk(ContentValues values, long bookFk){
        values.put(BOOK_FK, bookFk);
    }
}
