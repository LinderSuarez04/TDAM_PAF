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

    @GET("/api/v1/canchas")
    Call<List<Cancha>> obtenerCanchas(@Header("Authorization") String token);

    @POST("/api/v1/reservas")
    Call<Void> guardarReserva(@Header("Authorization") String token, @Body Reserva reserva);

    //@GET("/api/v1/reservas/usuario/<int:usuario_id>")
    @GET("/api/v1/reservas/usuario/{usuario_id}")

    Call<List<ReservaInfo>> obtenerReservasPorUsuario(@Header("Authorization") String token, @Path("usuario_id") int usuarioId);
    @GET("/api/v1/productos")
    Call<List<Producto>> obtenerProductos(@Header("Authorization") String token);

    @GET("/api/v1/carritos")
    Call<List<Carrito>> obtenerCarritos(@Header("Authorization") String token);

    @PUT("/api/carritos/{carrito_id}")
    Call<Void> actualizarCarrito(@Path("carritoId") int carritoId, @Body Carrito carrito);

    @DELETE("/api/v1/carritos/<int:id>")
    Call<Void> eliminarCarrito(String s, @Path("carritoId") int carritoId);





}




