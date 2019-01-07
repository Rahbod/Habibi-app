package ir.rahbod.habibi.api;

import ir.rahbod.habibi.model.AccessToken;
import ir.rahbod.habibi.model.Address;
import ir.rahbod.habibi.model.AddressList;
import ir.rahbod.habibi.model.Authorization;
import ir.rahbod.habibi.model.CheckCode;
import ir.rahbod.habibi.model.Cooperation;
import ir.rahbod.habibi.model.DevicesList;
import ir.rahbod.habibi.model.ItemRequest;
import ir.rahbod.habibi.model.Register;
import ir.rahbod.habibi.model.Request;
import ir.rahbod.habibi.model.RequestList;
import ir.rahbod.habibi.model.RequestInfo;
import ir.rahbod.habibi.model.SubService;
import ir.rahbod.habibi.model.SubServiceItem;
import ir.rahbod.habibi.model.TransactionList;
import ir.rahbod.habibi.model.UserName;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/register")
    Call<Register> register(@Body Register register);

    @POST("api/verify")
    Call<Authorization> checkCode(@Body CheckCode checkCode);

    @POST("oauth/token")
    Call<AccessToken> getToken(@Body Authorization auth);

    @POST("api/setName")
    Call<UserName> setName(@Body UserName name);

    @GET("api/devices")
    Call<DevicesList> getDevices();

    @GET("api/addresses")
    Call<AddressList> getAddress();

    @POST("api/newAddress")
    Call<AddressList> addAddress(@Body Address address);

    @POST("api/request")
    Call<Request> sendRequest(@Body Request request);

    @GET("api/requests")
    Call<RequestList> getRequest();

    @GET("api/transactions")
    Call<TransactionList> getTransaction();

    @POST("api/requestInfo")
    Call<RequestInfo> getDevicesInfo(@Body ItemRequest item);

    @POST("api/cooperation")
    Call<Cooperation> sendCooperation(@Body Cooperation cooperation);

    @POST("api/devices")
    Call<SubService> getSubService(@Body SubServiceItem subService);
}
