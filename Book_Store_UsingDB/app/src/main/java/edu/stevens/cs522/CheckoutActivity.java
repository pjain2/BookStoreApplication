package edu.stevens.cs522;



        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.widget.Toast;

public class CheckoutActivity extends Activity {

    private static final String TAG = CheckoutActivity.class.getCanonicalName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // TODO display ORDER and CANCEL options.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.checkout_menu, menu);
        Log.i(TAG,"Checkout Book Menu Created");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        // TODO
        // ORDER: display a toast message of how many books have been ordered and return
        switch (item.getItemId()) {
            case R.id.order:
                Log.i(TAG,"Order Menu Selected");
                Intent intent = getIntent();
                //int orderQuantity = intent.getIntExtra(BookStoreActivity.CHECKOUT_EXTRA,0);

                Context context = getApplicationContext();
                CharSequence text = "Order placed for books"; //+ orderQuantity + " books";
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

                setResult(RESULT_OK,intent);
                finish();
                return true;

            // CANCEL: just return with REQUEST_CANCELED as the result code
            case R.id.cancel:
                Log.i(TAG,"Cancel Menu Selected");
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }
        return false;
    }

}