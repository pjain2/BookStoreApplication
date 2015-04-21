package edu.stevens.cs522.entities;



        import android.content.ContentValues;
        import android.database.Cursor;
        import android.os.Parcel;
        import android.os.Parcelable;
        import edu.stevens.cs522.contracts.BookContract;

public class Book implements Parcelable{

    // TODO Modify this to implement the Parcelable interface.

    // TODO redefine toString() to display book title and price (why?).

    public int id;

    public String title;

    public Author[] authors;

    public String isbn;

    public String price;

    public Book(int id, String title, Author[] author, String isbn, String price) {
        this.id = id;
        this.title = title;
        this.authors = author;
        this.isbn = isbn;
        this.price = price;
    }

    /*Method required by Parceable interface
     * */
    private Book(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.authors = new Author[5];
        in.readTypedArray(authors, Author.CREATOR);;
        this.isbn = in.readString();;
        this.price = in.readString();;
    }

    public Book(Cursor cursor){
        //this.id = BookContract.getID(cursor);
        this.title = BookContract.getTitle(cursor);
        this.isbn = BookContract.getIsbn(cursor);
        this.price = BookContract.getPrice(cursor);
        String authorsStr = BookContract.getAuthors(cursor);
        this.authors = Author.getAuthors(authorsStr);
    }

    /*Method required by Parceable interface
     * */
    public int describeContents() {
        return 0;
    }

    /*Method required by Parceable interface
     * */
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(title);
        out.writeTypedArray(authors, 0);
        out.writeString(isbn);
        out.writeString(price);
    }

    public void writeToProvider(ContentValues values){
        BookContract.putTitle(values,title);
        BookContract.putIsbn(values, isbn);
        BookContract.putPrice(values, price);
    }

    /*Variable required by Parceable interface
     * */
    public static final Parcelable.Creator<Book> CREATOR
            = new Parcelable.Creator<Book>() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public String toString(){
        return this.title +" $" +  this.price;
    }

}