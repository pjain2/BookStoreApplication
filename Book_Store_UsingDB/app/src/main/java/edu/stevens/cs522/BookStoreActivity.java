package edu.stevens.cs522;



        import android.app.ListActivity;
        import android.content.Intent;
        import android.database.Cursor;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.ActionMode;
        import android.view.ContextMenu;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView.AdapterContextMenuInfo;
        import android.widget.CursorAdapter;
        import android.widget.ListView;
        import android.widget.SimpleCursorAdapter;
        import edu.stevens.cs522.contracts.BookContract;
        import edu.stevens.cs522.databases.CartDbAdapter;
        import edu.stevens.cs522.entities.Book;

public class BookStoreActivity extends ListActivity {

    // Use this when logging errors and warnings.
    @SuppressWarnings("unused")
    private static final String TAG = BookStoreActivity.class.getCanonicalName();

    // These are request codes for subactivity request calls
    static final private int ADD_REQUEST = 1;

    @SuppressWarnings("unused")
    static final private int CHECKOUT_REQUEST = ADD_REQUEST + 1;


    // There is a reason this must be an ArrayList instead of a List.
    @SuppressWarnings("unused")
    public final static String DETAILS_EXTRA = "edu.stevens.cs522.DetailsMessage";

    private CursorAdapter adapter;
    private CartDbAdapter cartDBAdapter;
    private Cursor cursor;
    private ListView listView;

    private String[] from = new String[]{BookContract.TITLE, BookContract.AUTHORS};
    private int[] to = new int[]{android.R.id.text1, android.R.id.text2};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // TODO Set the layout (use cart.xml layout)
        setContentView(R.layout.cart);

        listView = (ListView)findViewById(android.R.id.list);
        //add Context Menu for Details and Delete options
        registerForContextMenu(listView);

        // TODO use an array adapter to display the cart contents.
        //adapter = new ArrayAdapter<Book>(this, R.layout.cart_row, R.id.cart_row_title, shoppingCart);
        cartDBAdapter = new CartDbAdapter(this);
        //Create the database tables and create the connection
        cartDBAdapter.open();
        cursor = cartDBAdapter.fetchAllBooks();
        startManagingCursor(cursor);

        //Create an new Simple Cursor Adapter
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to);
        setListAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // TODO provide ADD, DELETE and CHECKOUT options
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bookstore_menu, menu);
        Log.i(TAG,"Menu Item created");
        return true;


    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_delete, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        // TODO

        // ADD provide the UI for adding a book
        // Intent addIntent = new Intent(this, AddBookActivity.class);
        // startActivityForResult(addIntent, ADD_REQUEST);
        switch (item.getItemId()) {
            case R.id.add:
                Log.i(TAG,"Add Option Selected");
                Intent addIntent = new Intent(this, AddBookActivity.class);
                startActivityForResult(addIntent, ADD_REQUEST);
                return true;

            // CHECKOUT provide the UI for checking out
            case R.id.checkout:
                Log.i(TAG,"Checkout Option Selected");
                Intent checkoutIntent = new Intent(this, CheckoutActivity.class);
                startActivityForResult(checkoutIntent, CHECKOUT_REQUEST);
                return true;
        }

        return false;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.display:
                Log.i(TAG,"Display item selected: "+ info.id);
                if(info.id>0){
                    Intent intent = new Intent(this, DetailsBookActivity.class);
                    Book book = cartDBAdapter.fetchBook(info.id);
                    intent.putExtra(DETAILS_EXTRA, book);
                    startActivity(intent);
                }
                return true;
            case R.id.delete:
                Log.i(TAG,"Delete item selected: " + info.id);
                if(info.id > 0){
                    Book book = new Book((int)info.id,null,null,null,null);
                    if(cartDBAdapter.delete(book)){
                        Log.i(TAG,"Book deleted ");
                        cursor.requery();
                        adapter.notifyDataSetChanged();
                    }
                }
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // TODO Handle results from the Search and Checkout activities.

        // Use SEARCH_REQUEST and CHECKOUT_REQUEST codes to distinguish the cases.
        // SEARCH: add the book that is returned to the shopping cart.
        if(requestCode == ADD_REQUEST){
            if(resultCode == RESULT_OK){
                Book book = intent.getParcelableExtra(AddBookActivity.BOOK_RESULT_KEY);
                cartDBAdapter.persist(book);
                adapter.notifyDataSetChanged();
            }
        }
        // CHECKOUT: empty the shopping cart.
        else if(requestCode == CHECKOUT_REQUEST){
            if(resultCode == RESULT_OK){
                cartDBAdapter.deleteAll();
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // TODO save the shopping cart contents (which should be a list of parcelables).
        super.onSaveInstanceState(savedInstanceState);

    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        cartDBAdapter.close();
    }
}