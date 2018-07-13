package com.example.android.inventoryapp_s2;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp_s2.data.InventoryContract.InventoryEntry;

public class InventoryCursorAdapter extends CursorAdapter {

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        //Find individual views to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.prodPrice);
        final TextView qtyTextView = (TextView) view.findViewById(R.id.prodQty);
        Button saleButton = view.findViewById(R.id.sale_button);

        int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
        int qtyColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QTY);

        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        String productQty = cursor.getString(qtyColumnIndex);

        nameTextView.setText(productName);
        priceTextView.setText(productPrice);
        qtyTextView.setText(productQty);

        int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
        int productId = cursor.getInt(idColumnIndex);
        final Uri currentProductUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, productId);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productQty = Integer.parseInt(qtyTextView.getText().toString());
                if (productQty > 0) {
                    qtyTextView.setText("" + (--productQty));
                    ContentValues values = new ContentValues();
                    values.put(InventoryEntry.COLUMN_QTY, productQty);
                    context.getContentResolver().update(currentProductUri, values, null, null);
                } else {
                    Toast.makeText(context, (R.string.sale_button_failed), Toast.LENGTH_SHORT).show();
                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, EditorActivity.class);
                i.setData(currentProductUri);
                context.startActivity(i);
            }
        });
    }
}
