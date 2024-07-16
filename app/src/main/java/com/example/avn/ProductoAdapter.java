package com.example.avn;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.avn.Model.Producto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private List<Producto> listaProductos;
    private OnItemClickListener onItemClickListener;

    public ProductoAdapter(List<Producto> listaProductos, OnItemClickListener onItemClickListener) {
        this.listaProductos = listaProductos;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);

        // Configura las vistas del ViewHolder según los datos del producto
        holder.nombreTextView.setText(producto.getNombre());
        holder.descripcionTextView.setText(producto.getDescripcion());

        // Convierte el precio a String y luego establece el texto en el TextView
        float precio = producto.getPrecio();
        String precioString = String.valueOf(precio);
        holder.precioTextView.setText(precioString);

        if (producto != null) {
            ImageView imageView = holder.imagenProductoImageView;

            if (imageView != null) {
                Picasso.get().load(producto.getImagenUrl()).into(imageView);
                // Otros códigos de asignación de datos a otros elementos de la vista
            }
        }
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public class ProductoViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreTextView;
        private TextView descripcionTextView;
        private TextView precioTextView;
        private ImageView imagenProductoImageView;

        public ProductoViewHolder(@NonNull View itemView) {
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
                        onItemClickListener.onItemClick(listaProductos.get(position));
                    }
                }
            });
        }
    }

    // Interfaz para manejar clics en la lista
    public interface OnItemClickListener {
        void onItemClick(Producto producto);
    }
}

