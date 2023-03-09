package it.uniba.dib.sms22239;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class My_View_Holder_Spesa extends RecyclerView.ViewHolder {

     TextView nome,prezzo,quantita, dataAcquisto;


    public My_View_Holder_Spesa(@NonNull View itemView) {
        super(itemView);
        nome=itemView.findViewById(R.id.add_item_name);
        prezzo=itemView.findViewById(R.id.add_item_price);
        quantita=itemView.findViewById(R.id.add_item_quantity);
        dataAcquisto=itemView.findViewById(R.id.add_item_date);

    }
}
