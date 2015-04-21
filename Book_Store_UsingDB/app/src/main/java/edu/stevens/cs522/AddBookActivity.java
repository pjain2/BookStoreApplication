package edu.stevens.cs522;


        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.widget.EditText;
        import edu.stevens.cs522.entities.Author;
        import edu.stevens.cs522.entities.Book;

public class AddBookActivity extends Activity {

    // Use this as the key to return the book details as a Parcelable extra in the result intent.
    public static final String BOOK_RESULT_KEY = "book_result";

    private static final String TAG = AddBookActivity.class.getCanonicalName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // TODO provider SEARCH and CANCEL options
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addbook_menu, menu);
        Log.i(TAG,"Add Book Menu Created");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        // TODO
        // SEARCH: return the book details to the BookStore activity
        switch (item.getItemId()) {
            case R.id.search:
                Log.i(TAG,"Search Menu Selected");
                Book book = searchBook();
                Intent intent = getIntent();
                intent.putExtra(BOOK_RESULT_KEY, book);
                setResult(RESULT_OK,intent);
                finish();
                return true;

            // CANCEL: cancel the search request
            case R.id.cancel:
                Log.i(TAG,"Cancel Menu Selected");
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }

        return false;
    }

    public Book searchBook(){
		/*
		 * Search for the specified book.
		 */
        // TODO Just build a Book object with the search criteria and return that.
        //Fetch the author entered by user
        EditText authorText = (EditText) findViewById(R.id.search_author);
        String authorStr = authorText.getText().toString();

        //Get authors array from input text
        Author[] authors = Author.getAuthors(authorStr);

        //Fetch the title entered by user
        EditText titleText = (EditText) findViewById(R.id.search_title);
        String titleStr = titleText.getText().toString();

        //Fetch the ISBN entered by user
        EditText isbnText = (EditText) findViewById(R.id.search_isbn);
        String isbnStr = isbnText.getText().toString();

        //Fetch the Price entered by user
        EditText priceText = (EditText) findViewById(R.id.search_price);
        String priceStr = priceText.getText().toString();

        //Create new Book Object
        Book book = new Book(1, titleStr, authors, isbnStr, priceStr);

        return book;
    }

}