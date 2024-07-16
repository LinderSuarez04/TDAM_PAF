package com.example.avn;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.avn.Model.Carrito;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {
    private List<Carrito> listaCarritos;
    private OnItemClickListener onItemClickListener;

    public CarritoAdapter(List<Carrito> listaCarritos, OnItemClickListener onItemClickListener) {
        this.listaCarritos = listaCarritos;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        Carrito carrito = listaCarritos.get(position);

        // Configura las vistas del ViewHolder según los datos del producto
        holder.nombreTextView.setText(carrito.getNombre());
        holder.descripcionTextView.setText(carrito.getDescripcion());

        // Convierte el precio a String y luego establece el texto en el TextView
        float precio = carrito.getPrecio();
        String precioString = String.valueOf(precio);
        holder.precioTextView.setText(precioString);

        // Cargar la imagen con Picasso sin verificar si carrito es nulo
        ImageView imageView = holder.imagenProductoImageView;
        Picasso.get().load(carrito.getImagenUrl()).into(imageView);
    }

    @Override
    public int getItemCount() {
        return listaCarritos.size();
    }

    public class CarritoViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreTextView;
        private TextView descripcionTextView;
        private TextView precioTextView;
        private ImageView imagenProductoImageView;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionTextView);
            precioTextView = itemView.findViewById(R.id.precioTextView);
            imagenProductoImageView = itemView.findViewById(R.id.imagenCanchaImageView);

            // Agrega el listener al itemView para manejar clics
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                        // Avisa al listener sobre el clic en el elemento utilizando la posición directamente
                        onItemClickListener.onItemClick(listaCarritos.get(position));
                    }
                }
            });
        }
    }

    // Interfaz para manejar clics en la lista
    public interface OnItemClickListener {
        void onItemClick(Carrito carrito);
    }
}
