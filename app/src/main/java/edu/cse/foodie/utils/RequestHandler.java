package edu.cse.foodie.utils;

import android.content.Context;
import android.location.Location;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.cse.foodie.model.SensorDataObject;

public class RequestHandler {

    private final RequestQueue requestQueue;
    private String TAG = "RequestHandler";

    public RequestHandler(Context mContext) {
        requestQueue = Volley.newRequestQueue(mContext);
    }

    public void sendUpdate(SensorDataObject dataObject, Response.Listener<JSONObject> onResponce, Response.ErrorListener onError) throws JSONException {
        String url = Utils.getUrl("user/inside-restaurant");
        JSONObject jsonBody = new JSONObject();
        String id = "T" + System.currentTimeMillis();
        jsonBody.put("id", id);
//        Actual Location
//        jsonBody.put("latitude", dataObject.getLat());
//        jsonBody.put("longitude", dataObject.getLng());
//         Near to the goda canteen
//        jsonBody.put("latitude", 6.7963281);
//        jsonBody.put("longitude", 79.900239);
//        Inside the goda canteen
        jsonBody.put("latitude", 6.7962781);
        jsonBody.put("longitude", 79.9001792);
        jsonBody.put("light", dataObject.getLux());
        jsonBody.put("sound", dataObject.getNoiceLevel());
        jsonBody.put("proximity", dataObject.isProximity());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody, onResponce, onError);
        requestQueue.add(request);
    }

    public void getNearbyRestaurantsByLocation(Location location, Response.Listener<JSONObject> onResponse, Response.ErrorListener onErrorResponse) throws JSONException {
        String url = Utils.getUrl("user/nearby-restaurants");
        Map<String, Double> params = new HashMap<>();
        params.put("userLatitude", location.getLatitude());
        params.put("userLongitude", location.getLongitude());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), onResponse, onErrorResponse);
        requestQueue.add(request);
    }

    public void getAllRestaurants(Response.Listener<JSONObject> onResponse, Response.ErrorListener onErrorResponse){
        String url = Utils.getUrl("restaurant/all-restaurant");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), onResponse, onErrorResponse);
        requestQueue.add(request);
    }


    public void getNoiseLevel(String id, Response.Listener<JSONObject> onResponse, Response.ErrorListener onErrorResponse) throws JSONException {
        String url = Utils.getUrl("restaurant/get-intensities");
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("_id", id);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody, onResponse, onErrorResponse);
        requestQueue.add(request);
    }

    public void notifyVisited(String _id, String id, Response.Listener<JSONObject> onResponse, Response.ErrorListener onErrorResponse) throws JSONException {
        String url = Utils.getUrl("restaurant/update-intensities");
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("_id", _id);
        jsonBody.put("id", id);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody, onResponse, onErrorResponse);
        requestQueue.add(request);
    }
}
