package edu.stevens.cs522.contracts;


        import java.util.regex.Pattern;

        import android.content.ContentValues;
        import android.database.Cursor;

public class BookContract {

    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String ISBN = "isbn";
    public static final String PRICE = "price";
    public static final String AUTHORS = "authors";

    public static final	char SEPARATOR_CHAR	= '|';

    private	static final Pattern SEPARATOR	=
            Pattern.compile(Character.toString(SEPARATOR_CHAR),	Pattern.LITERAL);

    public static String[] readStringArray(String in)	{
        return	SEPARATOR.split(in);
    }

    public static String getAuthors(Cursor cursor){
        return cursor.getString(cursor.getColumnIndexOrThrow(AUTHORS));
    }

    public static void putAuthors(ContentValues values, String authors){
        values.put(AUTHORS, authors);
    }

    public static String getTitle(Cursor cursor){
        return cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
    }

    public static void putTitle(ContentValues values, String title){
        values.put(TITLE, title);
    }

    public static String getIsbn(Cursor cursor){
        return cursor.getString(cursor.getColumnIndexOrThrow(ISBN));
    }

    public static void putIsbn(ContentValues values, String isbn){
        values.put(ISBN, isbn);
    }

    public static String getPrice(Cursor cursor){
        return cursor.getString(cursor.getColumnIndexOrThrow(PRICE));
    }

    public static void putPrice(ContentValues values, String price){
        values.put(PRICE, price);
    }

}
