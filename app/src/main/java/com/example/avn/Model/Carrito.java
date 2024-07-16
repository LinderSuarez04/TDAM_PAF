package com.example.avn.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Carrito implements Parcelable {
    private int id;
    private String nombre;
    private String descripcion;
    private float precio;
    private String imagen_url;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
    public String getImagenUrl() {
        return imagen_url;
    }

    // Resto de tus getters y setters

    // Implementación de Parcelable
    protected Carrito(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
        precio = in.readFloat();
        imagen_url = in.readString();
    }

    public static final Creator<Carrito> CREATOR = new Creator<Carrito>() {
        @Override
        public Carrito createFromParcel(Parcel in) {
            return new Carrito(in);
        }

        @Override
        public Carrito[] newArray(int size) {
            return new Carrito[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeFloat(precio);
        dest.writeString(imagen_url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
