package it.uniba.dib.sms22239;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView email,nome,password;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        email=itemView.findViewById(R.id.email);
        nome=itemView.findViewById(R.id.nome);
        password=itemView.findViewById(R.id.password);

    }
}
