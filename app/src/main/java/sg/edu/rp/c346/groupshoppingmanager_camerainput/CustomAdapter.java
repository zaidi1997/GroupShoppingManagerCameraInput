package sg.edu.rp.c346.groupshoppingmanager_camerainput;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<ShoppingList> {

    private Context context;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvQuantity;
    ArrayList<ShoppingList> shopppingList;

    public CustomAdapter(Context context,int resource, ArrayList<ShoppingList> objects) {
        super(context, resource, objects);
        this.context = context;
        shopppingList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.customlayout, parent, false);

        tvName = rowView.findViewById(R.id.listview_name);
        tvPrice = rowView.findViewById(R.id.listview_price);
        tvQuantity = rowView.findViewById(R.id.listview_qty);

        ShoppingList currentItem = shopppingList.get(position);

        tvName.setText(String.valueOf(currentItem.getName()));
        tvPrice.setText(String.valueOf(currentItem.getPrice()));
        tvQuantity.setText(String.valueOf(currentItem.getQty()));

        return rowView;
    }
}
