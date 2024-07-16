package com.example.avn;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.avn.Model.ReservaInfo;
import com.example.avn.Model.Cancha;

import java.util.List;

public class MiReservaAdapter extends ArrayAdapter<ReservaInfo> {

    private List<Cancha> listaCanchas;

    public MiReservaAdapter(Context context, List<ReservaInfo> reservas, List<Cancha> listaCanchas) {
        super(context, 0, reservas);
        this.listaCanchas = listaCanchas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReservaInfo reserva = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_reserva, parent, false);
        }

        TextView textViewFecha = convertView.findViewById(R.id.textViewFecha);
        TextView textViewHorario = convertView.findViewById(R.id.textViewHorario);
        TextView textViewNombreCancha = convertView.findViewById(R.id.textViewNombreCancha);

        textViewFecha.setText("Fecha: " + reserva.getFecha());
        textViewHorario.setText("Horario: " + reserva.getHorario());

        // Verifica si la lista de canchas está vacía o nula
        if (listaCanchas != null && !listaCanchas.isEmpty()) {
            // Obtén el nombre de la cancha basado en el ID
            String nombreCancha = getNombreCanchaPorId(reserva.getIdCancha());
            textViewNombreCancha.setText("Cancha: " + nombreCancha);
        } else {
            // Log para indicar que la lista de canchas no contiene datos
            Log.e("MiReservaAdapter", "La lista de canchas está vacía o nula");
            textViewNombreCancha.setText("Cancha: Nombre de Cancha Desconocido");
        }

        return convertView;
    }

    private String getNombreCanchaPorId(int idCancha) {
        for (Cancha cancha : listaCanchas) {
            if (cancha.getId() == idCancha) {
                return cancha.getNombre();
            }
        }
        // Log para indicar que no se encontró la cancha con el ID correspondiente
        Log.w("MiReservaAdapter", "No se encontró la cancha con ID: " + idCancha);
        return "Nombre de Cancha Desconocido";
    }
}
