package ir.rahbod.habibi.api;

import ir.rahbod.habibi.model.AccessToken;
import ir.rahbod.habibi.model.Address;
import ir.rahbod.habibi.model.AddressList;
import ir.rahbod.habibi.model.Authorization;
import ir.rahbod.habibi.model.CheckCode;
import ir.rahbod.habibi.model.Cooperation;
import ir.rahbod.habibi.model.DataAddress;
import ir.rahbod.habibi.model.DevicesList;
import ir.rahbod.habibi.model.ItemRequest;
import ir.rahbod.habibi.model.Payment;
import ir.rahbod.habibi.model.Register;
import ir.rahbod.habibi.model.RepairManInfo;
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
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("api/repairman/{code}")
    Call<RepairManInfo> getRepairManInfo(@Path("code") String code);

    @POST("api/setPaymentMethod")
    Call<Payment> paymentInvoice(@Body Payment payment);

    @POST("api/deleteAddress")
    Call<Address> removeAddress(@Body Address address);

    @POST("api/updateAddress")
    Call<DataAddress> editAddress(@Body Address address);

    @POST("api/cancelRequest")
    Call<RequestInfo> cancelRequest(@Body RequestInfo info);
}
