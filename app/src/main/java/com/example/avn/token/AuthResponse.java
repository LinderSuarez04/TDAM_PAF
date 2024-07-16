package com.example.avn.token;

import com.example.avn.token.Usuario;
import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("user")
    private Usuario user;

    public String getAccessToken() {
        return accessToken;
    }

    public Usuario getUser() {
        return user;
    }
}
