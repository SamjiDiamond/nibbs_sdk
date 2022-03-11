package com.nibbssdk.volley;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public abstract class VolleyRequest {
    public String URL;
    private boolean cache = true;
    private String format = "array";

    public abstract void onNetworkError();

    public abstract void onProcess();

    public abstract void onSuccess(JSONObject jSONObject);

    public VolleyRequest(String str) {
        this.URL = str;
    }

    private boolean isCache() {
        return this.cache;
    }

    public void setCache(boolean z) {
        this.cache = z;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String str) {
        this.format = str;
    }

    public void fetchResources() {
        onProcess();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(0, this.URL, null, new Response.Listener<JSONObject>() {
            /* class com.ugswitch.simhost.volley.VolleyRequest.AnonymousClass1 */

            public void onResponse(JSONObject jSONObject) {
                VolleyRequest.this.onSuccess(jSONObject);
            }
        }, new Response.ErrorListener() {
            /* class com.ugswitch.simhost.volley.VolleyRequest.AnonymousClass2 */

            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                VolleyRequest.this.onNetworkError();
            }
        });
        if (!isCache()) {
            jsonObjectRequest.setShouldCache(false);
        }
        InitiateVolley.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
