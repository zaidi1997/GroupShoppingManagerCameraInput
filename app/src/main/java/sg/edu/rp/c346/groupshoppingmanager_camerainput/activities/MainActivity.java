package sg.edu.rp.c346.groupshoppingmanager_camerainput.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import sg.edu.rp.c346.groupshoppingmanager_camerainput.CustomAdapter;
import sg.edu.rp.c346.groupshoppingmanager_camerainput.DBHelper;
import sg.edu.rp.c346.groupshoppingmanager_camerainput.EditItemActivity;
import sg.edu.rp.c346.groupshoppingmanager_camerainput.R;
import sg.edu.rp.c346.groupshoppingmanager_camerainput.ShoppingList;

public class MainActivity extends AppCompatActivity {

    private TextView textValue, textTotal, titleName, titlePrice, titleQty;
    private Button copyButton;
    private Button mailTextButton;
    private CustomAdapter caShoppingList;
    private ListView namesListView;
    private ScrollView scrollView;
    ArrayList<ShoppingList> alShoppingList;
    ArrayList alCounter;
    View v;
    EditText etName, etPrice, etQty;


    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "MainActivity";

    Double totalCost =  0.0;
    Double itemTotalCost = 0.0;
    Double price = 0.0;
    Integer qty = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleName = (TextView) findViewById(R.id.titleName);
        titlePrice = (TextView) findViewById(R.id.titlePrice);
        titleQty = (TextView) findViewById(R.id.titleQty);
        scrollView = (ScrollView) findViewById(R.id.content_main);
        namesListView =(ListView) findViewById(R.id.namesListView);
        textValue = (TextView)findViewById(R.id.output);
        textTotal = (TextView)findViewById(R.id.tvTotal);

        alShoppingList = new ArrayList<>();

        caShoppingList = new CustomAdapter(this, R.layout.customlayout, alShoppingList);
        namesListView.setAdapter(caShoppingList);

        updateTotalCost();

        namesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long identity) {
                Intent i = new Intent(MainActivity.this,
                        EditItemActivity.class);
                ShoppingList data = alShoppingList.get(position);
                Integer id = data.getId();
                String name = data.getName();
                Double price = data.getPrice();
                Integer qty = data.getQty();

                ShoppingList target = new ShoppingList(id, name, price, qty);
                i.putExtra("data", target);
                startActivityForResult(i, 9);
            }
        });

        //mail and copy to clipboard

        mailTextButton = (Button) findViewById(R.id.mail_text_button);
        mailTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBHelper dbh = new DBHelper(MainActivity.this);
                String emailText = dbh.getAllItems().toString();

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_SUBJECT, "Shopping Cart items");
                i.putExtra(Intent.EXTRA_TEXT, emailText);
                try {
                    startActivity(Intent.createChooser(i, getString(R.string.mail_intent_chooser_text)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(),
                            R.string.no_email_client_error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        copyButton = (Button) findViewById(R.id.copy_text_button);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBHelper dbh = new DBHelper(MainActivity.this);
                String emailText = dbh.getAllItems().toString();

                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard =
                            (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(emailText);
                } else {
                    android.content.ClipboardManager clipboard =
                            (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Shopping List", emailText);
                    clipboard.setPrimaryClip(clip);
                }
                
                Toast.makeText(getApplicationContext(), R.string.clipboard_copy_successful_message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateTotalCost(){
        for(int i = 0; i < alShoppingList.size(); i++)
        {
            price = (alShoppingList.get(i).getPrice());
            qty = (alShoppingList.get(i).getQty());

            itemTotalCost = price * qty;
            totalCost += itemTotalCost;
            String.format("%.2f", totalCost);
        }

        textTotal.setText(totalCost.toString());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 9){


        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        DBHelper dbh = new DBHelper(MainActivity.this);
        alShoppingList.clear();
        alShoppingList.addAll(dbh.getAllItems());
        dbh.close();

        totalCost = 0.0;
        updateTotalCost();

        caShoppingList.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_chatbot) {
            Intent i = new Intent(MainActivity.this, NewItemActivity.class);
            startActivityForResult(i, 9);
        }
        return super.onOptionsItemSelected(item);
    }
}
