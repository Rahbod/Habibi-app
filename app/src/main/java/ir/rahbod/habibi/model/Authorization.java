package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

public class Authorization {
    @SerializedName("authorization_code")
    public String auth;
    @SerializedName("grant_type")
    public String grantType;
    @SerializedName("refresh_token")
    public String refresh_token;


    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
