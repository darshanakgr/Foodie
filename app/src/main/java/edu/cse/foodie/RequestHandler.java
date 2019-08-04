package edu.cse.foodie;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestHandler {


    private final RequestQueue requestQueue;
    private String TAG = "RequestHandler";

    public RequestHandler(Context mContext) {
        requestQueue = Volley.newRequestQueue(mContext);
    }

    public void sendUpdate(SensorDataObject dataObject) throws JSONException {
        String url = Utils.getUrl("sensor/update");
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("latitude", dataObject.getLat());
        jsonBody.put("longitude", dataObject.getLng());
        jsonBody.put("light", dataObject.getLux());
        jsonBody.put("noise", dataObject.getNoiceLevel());
        jsonBody.put("proximity", dataObject.isProximity());
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "Success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });
        requestQueue.add(req);
    }
}
