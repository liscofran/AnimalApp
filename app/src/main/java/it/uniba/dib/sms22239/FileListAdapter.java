package it.uniba.dib.sms22239;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileViewHolder> {

    private List<String> fileNames;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String fileName);
    }

    public FileListAdapter(List<String> fileNames, OnItemClickListener listener) {
        this.fileNames = fileNames;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_file, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        String fileName = fileNames.get(position);
        holder.bind(fileName, listener);
    }

    @Override
    public int getItemCount() {
        return fileNames.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameTextView;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.file_name_text_view);
        }

        public void bind(final String fileName, final OnItemClickListener listener) {
            fileNameTextView.setText(fileName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(fileName);
                }
            });
        }
    }
}

