package com.example.avn.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Cancha implements Parcelable {
    private int id;
    private String nombre;
    private String ubicacion;
    private String imagen_url;

    public Cancha(int id, String nombre, String ubicacion, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.imagen_url = imagenUrl;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getImagenUrl() {
        return imagen_url;
    }

    // Resto de tus getters y setters

    // Implementación de Parcelable
    protected Cancha(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        ubicacion = in.readString();
        imagen_url = in.readString();
    }

    public static final Creator<Cancha> CREATOR = new Creator<Cancha>() {
        @Override
        public Cancha createFromParcel(Parcel in) {
            return new Cancha(in);
        }

        @Override
        public Cancha[] newArray(int size) {
            return new Cancha[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(ubicacion);
        dest.writeString(imagen_url);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
