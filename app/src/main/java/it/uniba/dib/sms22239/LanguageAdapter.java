package it.uniba.dib.sms22239;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    private List<LanguageData> mList;

    public LanguageAdapter(List<LanguageData> mList) {
        this.mList = mList;
    }

    public void setFilteredList(List<LanguageData> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_item, parent, false);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LanguageViewHolder holder, int position) {
        holder.logo.setImageResource(mList.get(position).getLogo());
        holder.titleTv.setText(mList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class LanguageViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView titleTv;

        public LanguageViewHolder(View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.logoIv);
            titleTv = itemView.findViewById(R.id.titleTv);
        }
    }
}
