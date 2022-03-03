package com.nibbs.request;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nibbs.Constant;
import com.nibbs.volley.InitiateVolley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public abstract class PostRequest {

    public abstract void onError(String str);

    public abstract void onSuccess(String str);

    public void sendIncomingSmS(String str, String endpoint, String time) {
        StringRequest r12 = new StringRequest(1, Constant.baseurl, new Response.Listener<String>() {
            /* class com.ugswitch.simhost.request.PostRequest.AnonymousClass4 */

            public void onResponse(String str) {
//                Log.d("RTN", str);
                PostRequest.this.onSuccess(str);
            }
        }, new Response.ErrorListener() {
            /* class com.ugswitch.simhost.request.PostRequest.AnonymousClass5 */

            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
//                Log.d("ERR", volleyError.toString());
                PostRequest.this.onError(volleyError.toString());
            }
        }) {
            /* class com.ugswitch.simhost.request.PostRequest.AnonymousClass6 */
            static final /* synthetic */ boolean $assertionsDisabled = false;

            @Override // com.android.volley.Request
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

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
                hashMap.put("sender", str);
                hashMap.put("message", endpoint);
                hashMap.put("time", time);
                return hashMap;
            }
        };
        r12.setShouldCache(false);
        r12.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1.0f));
        InitiateVolley.getInstance().addToRequestQueue(r12);
    }

}