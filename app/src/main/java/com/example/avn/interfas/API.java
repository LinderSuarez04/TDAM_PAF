package com.example.avn.interfas;
import com.example.avn.Model.Cancha;
import com.example.avn.Model.Carrito;
import com.example.avn.Model.Producto;
import com.example.avn.Model.Reserva;
import com.example.avn.Model.ReservaInfo;
import com.example.avn.token.AuthRequest;
import com.example.avn.token.AuthResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface API {


    @POST("/auth")
    Call<AuthResponse> obtenerToken(@Body AuthRequest authRequest);

    @GET("/obtener_canchas")
    Call<List<Cancha>> obtenerCanchas(@Header("Authorization") String token);

    @POST("/crear_reserva")
    Call<Void> guardarReserva(@Header("Authorization") String token, @Body Reserva reserva);

    @GET("/obtener_reservas_por_usuario/{usuario_id}")
    Call<List<ReservaInfo>> obtenerReservasPorUsuario(@Header("Authorization") String token, @Path("usuario_id") int usuarioId);
    @GET("/obtener_productos")
    Call<List<Producto>> obtenerProductos(@Header("Authorization") String token);

    @GET("/obtener_carritos")
    Call<List<Carrito>> obtenerCarritos(@Header("Authorization") String token);

    @PUT("carritos/{carritoId}")
    Call<Void> actualizarCarrito(@Path("carritoId") int carritoId, @Body Carrito carrito);

    @DELETE("carritos/{carritoId}")
    Call<Void> eliminarCarrito(String s, @Path("carritoId") int carritoId);





}




