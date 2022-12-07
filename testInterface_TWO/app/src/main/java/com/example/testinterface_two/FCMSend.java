package com.example.testinterface_two;

import android.content.Context;
import android.os.StrictMode;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class FCMSend {
    private static  String BASE = "https://fcm.googleapis.com/fcm/send";
    private static String key = "key=AAAA5MyUxDo:APA91bGOAYizDeA6FgLKWU1QrEnl_MGRD3oz--H8aG8hYCBn-Ndanr5IWssHwyWxT5v1cMzFjw_sOaDXWqModjEz2mFM94zSHD8u8xHYOoYzzUxDKSE2yHsVox7Wtt0y3qJDvozlVjhu";
        public static  void pushNotification(Context context , String token , String title , String message) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            RequestQueue queue = Volley.newRequestQueue(context);
            try
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("to", token);
                JSONObject notification = new JSONObject();
                notification.put("title", title);
                notification.put("body", message);
                jsonObject.put("notification", notification);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                } , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String ,String> param = new HashMap<>();
                        param.put("Content-Type", "application/json");
                        param.put("Auth", key);
                        return  param;
                    }
                };
                queue.add(jsonObjectRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

}
