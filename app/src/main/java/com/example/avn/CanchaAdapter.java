package com.example.avn;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.avn.Model.Cancha;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CanchaAdapter extends RecyclerView.Adapter<CanchaAdapter.ViewHolder> {

    private List<Cancha> listaCanchas;
    private OnItemClickListener onItemClickListener;

    public CanchaAdapter(List<Cancha> listaCanchas, OnItemClickListener onItemClickListener) {
        this.listaCanchas = listaCanchas;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cancha, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cancha cancha = listaCanchas.get(position);

        // Configura las vistas del ViewHolder según los datos de la cancha
        holder.nombreTextView.setText(cancha.getNombre());
        holder.ubicacionTextView.setText(cancha.getUbicacion());

        // Utiliza Picasso para cargar la imagen desde la URL
        Picasso.get().load(cancha.getImagenUrl()).into(holder.imagenCanchaImageView);
    }

    @Override
    public int getItemCount() {
        return listaCanchas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreTextView;
        private TextView ubicacionTextView;
        private ImageView imagenCanchaImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            ubicacionTextView = itemView.findViewById(R.id.ubicacionTextView);
            imagenCanchaImageView = itemView.findViewById(R.id.imagenCanchaImageView);

            // Agrega el listener al itemView para manejar clics
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                        // Avisa al listener sobre el clic en el elemento
                        onItemClickListener.onItemClick(listaCanchas.get(position));
                    }
                }
            });
        }
    }

    // Interfaz para manejar clics en la lista
    public interface OnItemClickListener {
        void onItemClick(Cancha cancha);
    }
}
