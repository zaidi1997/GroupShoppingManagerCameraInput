package sg.edu.rp.c346.groupshoppingmanager_camerainput;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditItemActivity extends AppCompatActivity {

    TextView tvID;
    EditText etName, etPrice, etQty;
    Button btnUpdate, btnDelete;
    ShoppingList data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etQty = findViewById(R.id.etQty);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        Intent i = getIntent();
        data = (ShoppingList) i.getSerializableExtra("data");

        etName.setText(data.getName());
        etPrice.setText(String.valueOf(data.getPrice()));
        etQty.setText(String.valueOf(data.getQty()));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(EditItemActivity.this);

                data.setName(etName.getText().toString());
                data.setPrice(Double.valueOf(etPrice.getText().toString()));
                data.setQty(Integer.valueOf(etQty.getText().toString()));
                dbh.updateItem(data);
                dbh.close();

                Intent i = new Intent();
                setResult(RESULT_OK, i);
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(EditItemActivity.this);
                dbh.deleteItem(data.getId());
                dbh.close();
                finish();
            }
        });
    }
}
