package edu.stevens.cs522.databases;




        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteDatabase.CursorFactory;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;
        import edu.stevens.cs522.contracts.AuthorContract;
        import edu.stevens.cs522.contracts.BookContract;
        import edu.stevens.cs522.entities.Author;
        import edu.stevens.cs522.entities.Book;

public class CartDbAdapter {

    private SQLiteDatabase db;
    private final Context context;
    private DatabaseHelper dbHelper;

    private static final String TAG = CartDbAdapter.class.getCanonicalName();

    private static final String DATABASE_NAME = "bookstore.db";
    private static final String BOOK_TABLE = "books";
    private static final String AUTHOR_TABLE = "authors";
    private static final String INDEX = "AuthorsBookIndex";
    private static final int DATABASE_VERSION = 1;

    //Query to create book table
    private static final String DATABASE_CREATE_BOOKS = "create table " + BOOK_TABLE + " ("+
            BookContract._ID + " integer primary key, " + BookContract.TITLE + " text not null, " +
            BookContract.ISBN + " text not null, " + BookContract.PRICE+" text not null )";

    //Query to create the Author table
    private static final String DATABASE_CREATE_AUTHORS = "create table " + AUTHOR_TABLE + " ("+
            AuthorContract._ID + " integer primary key, " + AuthorContract.NAME + " text not null, " +
            AuthorContract.BOOK_FK + " integer not null, foreign key (" + AuthorContract.BOOK_FK + ") references " + BOOK_TABLE + "(" +
            BookContract._ID + ") on delete cascade)";

    private static final String CREATE_INDEX = " create index " + INDEX + " on "+ AUTHOR_TABLE + "(book_fk)";


    private static class DatabaseHelper extends SQLiteOpenHelper{
        public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
            // TODO Auto-generated constructor stub
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            // TODO Auto-generated method stub
            _db.execSQL(DATABASE_CREATE_BOOKS);
            _db.execSQL(DATABASE_CREATE_AUTHORS);
            _db.execSQL(CREATE_INDEX);
            //_db.execSQL("PRAGMA	foreign_keys=ON;");

        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
            // TODO Auto-generated method stub
            Log.i(TAG," Upgrading database: from " + _oldVersion + " to " + _newVersion);

            _db.execSQL("DROP TABLE IF EXISTS '" + AUTHOR_TABLE+"'");
            _db.execSQL("DROP TABLE IF EXISTS '" + BOOK_TABLE+"'");

            onCreate(_db);
        }
    }


    public CartDbAdapter(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(this.context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public CartDbAdapter open() throws SQLException{
        db = dbHelper.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");
        return this;
    }

    public void close(){
        db.close();
    }

    /*
     * Method to fetch all the books
     */
    public Cursor fetchAllBooks(){

        String sql = "SELECT books._id, " + BookContract.TITLE + ", " + BookContract.PRICE +", "+ BookContract.ISBN+
                ", " + "GROUP_CONCAT(name,'|') as authors " +
                "FROM "+ BOOK_TABLE +" LEFT OUTER JOIN "+ AUTHOR_TABLE +" ON Books._id = Authors.book_fk " +
                "GROUP BY Books._id, "+ BookContract.TITLE + ", " + BookContract.PRICE +", "+ BookContract.ISBN;

        return db.rawQuery(sql, null);
    }

    /*
     * Method to fetch a book using rowid
     */
    public Book fetchBook(long rowid){

        String sql = "SELECT books._id, " + BookContract.TITLE + ", " + BookContract.PRICE +", "+ BookContract.ISBN+
                ", " + "GROUP_CONCAT(name,'|') as authors " +
                "FROM "+ BOOK_TABLE +" LEFT OUTER JOIN "+ AUTHOR_TABLE +" ON Books._id = Authors.book_fk  where books._id = ? ";

        Cursor bookCursor = db.rawQuery(sql, new String[]{String.valueOf(rowid)});
        Book book = null;
        if(bookCursor.moveToFirst()){
            book = new Book(bookCursor);
        }
        return book;
    }

    /*
     * Method to save the book details in the database
     */
    public void persist(Book book) throws SQLException{

        ContentValues bookContentValues = new ContentValues();
        book.writeToProvider(bookContentValues);
        //get the bookid of the inserted book record
        long rowid = db.insert(BOOK_TABLE, null, bookContentValues);

        ContentValues authorContentValues = new ContentValues();
        Author[] authors = book.authors;
        for(Author a : authors){
            if(a != null){
                a.writeToProvider(authorContentValues);
                AuthorContract.putBookFk(authorContentValues, rowid);
                db.insert(AUTHOR_TABLE, null, authorContentValues);
            }
        }
        Log.i(TAG,"Inserted values for booka dn Author");
    }

    /*
     * Method to delete a particular book
     */
    public boolean delete(Book book) {

        String where = BookContract._ID +"= ?";
        String[] whereArgs = {String.valueOf(book.id)};
        int count = db.delete(BOOK_TABLE, where, whereArgs);
        if(count > 0){
            return true;
        }
        else {
            return false;
        }
    }

    /*
     * Method to delete all the books from database
     */
    public boolean deleteAll(){
        String where = "1";
        int count = db.delete(BOOK_TABLE, where, null);
        if(count > 0){
            return true;
        }
        else {
            return false;
        }
    }

}
