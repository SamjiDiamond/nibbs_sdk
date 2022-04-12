package com.nibbssdk.request;

import android.content.Context;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nibbssdk.Constant;
import com.nibbssdk.Nibss;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class GetRequest {
    public abstract void onError(String str);

    public abstract void onSuccess(String str);

    public abstract void onSuccess(JSONObject jSONObject) throws JSONException;

    public void requestDisplay(String str, Context context) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(0, Constant.baseurl +"enrollment/"+ str, null, new Response.Listener<JSONObject>() {
            /* class com.ugswitch.simhost.request.GetRequest.AnonymousClass1 */

            public void onResponse(JSONObject jSONObject) {
                Log.d("RTN", jSONObject.toString());
                try {
                    GetRequest.this.onSuccess(jSONObject.getJSONArray("data").getJSONObject(0).getString("Message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("RTN44", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            /* class com.ugswitch.simhost.request.GetRequest.AnonymousClass2 */

            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                GetRequest.this.onError("network error");
                Log.d("VOLLEY", volleyError.toString());

            }
        }){
            @Override // com.android.volley.Request
            public Map<String, String> getHeaders() {
                HashMap hashMap = new HashMap();
                hashMap.put("Connection", "Keep-Alive");
                return hashMap;
            }

            /* access modifiers changed from: protected */
            @Override // com.android.volley.Request
            public Map<String, String> getParams() {
                HashMap hashMap = new HashMap();
                hashMap.put("agent_id", Nibss.agent_code);
                return hashMap;
            }
        };
        jsonObjectRequest.setShouldCache(false);
        addToRequestQueue(jsonObjectRequest,context);
    }

    public void requestData(Context context, String str, String str2) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(0, "context.getString(R.string.get_url)" + str + "/" + str2, null, new Response.Listener<JSONObject>() {
            /* class com.ugswitch.simhost.request.GetRequest.AnonymousClass3 */
            public void onResponse(JSONObject jSONObject) {
                Log.d("RTN21", jSONObject.toString());
                try {
                    GetRequest.this.onSuccess(jSONObject.getJSONObject("data").getJSONObject("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("RTN22", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            /* class com.ugswitch.simhost.request.GetRequest.AnonymousClass4 */

            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                GetRequest.this.onError("network error");
                Log.d("VOLLEY", volleyError.toString());
            }
        });
        jsonObjectRequest.setShouldCache(false);
        System.out.println("=====> SMS Text ppp => "+jsonObjectRequest.getBody());
        addToRequestQueue(jsonObjectRequest,context);
    }

    public RequestQueue getRequestQueue(Context context) {
        if (this.mRequestQueue == null) {
            this.mRequestQueue = Volley.newRequestQueue(context);
        }
        return this.mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, Context context) {
        request.setTag(TAG);
        getRequestQueue(context).add(request);
    }
    public static final String TAG = "postrequest";
    private RequestQueue mRequestQueue;
}
