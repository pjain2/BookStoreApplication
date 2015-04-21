package edu.stevens.cs522;



/*
 * The DetailsBookActivity class is called to display the details of a book
 */
        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.Menu;
        import android.widget.TextView;
        import edu.stevens.cs522.entities.Author;
        import edu.stevens.cs522.entities.Book;

public class DetailsBookActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_book);

        Intent intent = getIntent();
        Book book = intent.getParcelableExtra(BookStoreActivity.DETAILS_EXTRA);
        //Update the view with book details
        if(book !=null){
            TextView titleView = (TextView)findViewById(R.id.display_title);
            titleView.setText(book.title);

            TextView authorView = (TextView)findViewById(R.id.display_author);
            Author[] authorsArr = book.authors;
            StringBuilder authorStr = new StringBuilder();

            for(Author a : authorsArr){
                if(a!=null){
                    authorStr.append(a.firstName);
                }
            }
            authorView.setText(authorStr.toString());

            TextView isbnView = (TextView)findViewById(R.id.display_isbn);
            isbnView.setText(book.isbn);

            TextView priceView = (TextView)findViewById(R.id.display_price);
            priceView.setText(book.price);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_book, menu);
        return true;
    }

}
