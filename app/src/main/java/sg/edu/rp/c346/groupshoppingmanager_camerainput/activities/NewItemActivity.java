package sg.edu.rp.c346.groupshoppingmanager_camerainput.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.ArrayList;

import sg.edu.rp.c346.groupshoppingmanager_camerainput.DBHelper;
import sg.edu.rp.c346.groupshoppingmanager_camerainput.R;
import sg.edu.rp.c346.groupshoppingmanager_camerainput.ShoppingList;

public class NewItemActivity extends AppCompatActivity {

    private Button scan, addToPrice, addToName, confirm;
    private CompoundButton useFlash;
    private TextView textValue;
    private EditText etName, etPrice, etQty;
    ArrayList<ShoppingList> alShoppingList;
    ShoppingList data;


    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "NewItemActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item);

        etName = (EditText) findViewById(R.id.etName);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etQty = (EditText) findViewById(R.id.etQty);

        addToName = (Button) findViewById(R.id.addName);
        addToPrice = (Button) findViewById(R.id.addPrice);

        scan = (Button) findViewById(R.id.scan);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);
        textValue = (TextView)findViewById(R.id.output);

        Button scan = (Button) findViewById(R.id.scan);
        Button confirm = (Button) findViewById(R.id.confirm);

        Intent i = getIntent();
        data = (ShoppingList) i.getSerializableExtra("data");

        textValue.setVisibility(View.GONE);

        addToName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText(textValue.getText());
                Toast.makeText(NewItemActivity.this, "Added to name", Toast.LENGTH_SHORT).show();
            }
        });

        addToPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPrice.setText(textValue.getText());
                Toast.makeText(NewItemActivity.this, "Added to price", Toast.LENGTH_SHORT).show();
            }
        });


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
                intent.putExtra(CaptureActivity.AutoFocus, true);
                intent.putExtra(CaptureActivity.UseFlash, useFlash.isChecked());

                startActivityForResult(intent, RC_OCR_CAPTURE);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                Double price = Double.parseDouble(etPrice.getText().toString());
                Integer quantity = Integer.parseInt(etQty.getText().toString());

                DBHelper dbh = new DBHelper(NewItemActivity.this);
                long row_affected = dbh.insertItem(name, price, quantity);
                dbh.close();

                if (row_affected != -1){
                    Toast.makeText(NewItemActivity.this, "Insert successful",
                            Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        textValue.setVisibility(View.VISIBLE);

        if (textValue.getText().toString().isEmpty()) {
            addToPrice.setVisibility(View.GONE);
            addToName.setVisibility(View.GONE);
        } else {
            addToPrice.setVisibility(View.VISIBLE);
            addToName.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(CaptureActivity.TextBlockObject);
                    textValue.setText(text);
                    Log.d(TAG, "Text read: " + text);
                } else {
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
