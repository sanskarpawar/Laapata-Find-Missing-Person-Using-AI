package com.laapata.findmissingperson.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAI4E1fw8:APA91bEJ7OJJEqpXwXemU_UILyeqe1uqkQU5SAsY1bkdPolLISI2CZgY4X-83BGeKeYoGuKVq1ydQzw27afhLXrDTc26r7ho4Dp9OmcN_LCY2xiuejm10HA7gykFvPSZ_sMX2iW2gixG"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
