package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("expire_in")
    public long expireIn;
    @SerializedName("refresh_token")
    public String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(long expireIn) {
        this.expireIn = expireIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


}
