package com.nibbssdk.request;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nibbssdk.Constant;
import com.nibbssdk.Nibss;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PostRequest {

    public abstract void onError(String str);

    public abstract void onSuccess(String str);

    public void sendIncomingSmS(String ticketID, String title, String surname, String middle_name, String first_name, String gender, String date_of_birth,
                                String marital_status, String nationality, String state_of_origin, String lga_of_origin, String state_of_capture, String lga_of_capture,
                                String nin, String residential_address, String state_of_residence, String lga_of_residence, String landmarks, String email,
                                String phone_number_1, String phone_number_2, String sign_image, String face_image, String finger_image, String bankname, Context context) {
        StringRequest r12 = new StringRequest(Request.Method.POST, Constant.baseurl+"enrollment", new Response.Listener<String>() {
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
                hashMap.put("agent_id", Nibss.agent_code);
                hashMap.put("ticketID", ticketID);
                hashMap.put("title", title);
                hashMap.put("surname", surname);
                hashMap.put("middle_name", middle_name);
                hashMap.put("first_name", first_name);
                hashMap.put("gender", gender);
                hashMap.put("date_of_birth", date_of_birth);
                hashMap.put("marital_status", marital_status);
                hashMap.put("nationality", nationality);
                hashMap.put("state_of_origin", state_of_origin);
                hashMap.put("lga_of_origin", lga_of_origin);
                hashMap.put("state_of_capture", state_of_capture);
                hashMap.put("lga_of_capture", lga_of_capture);
                hashMap.put("nin", nin);
                hashMap.put("residential_address", residential_address);
                hashMap.put("state_of_residence", state_of_residence);
                hashMap.put("lga_of_residence", lga_of_residence);
                hashMap.put("landmarks", landmarks);
                hashMap.put("email", email);
                hashMap.put("phone_number_1", phone_number_1);
                hashMap.put("phone_number_2", phone_number_2);
                hashMap.put("face_image", face_image);
                hashMap.put("sign_image", sign_image);
                hashMap.put("finger_image", finger_image);
                hashMap.put("bankname", bankname);
                return hashMap;
            }
        };
        r12.setShouldCache(false);
        r12.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1.0f));
        addToRequestQueue(r12,context);
    }

    public void sendfingerprint(String ticketID, List<String> finger_image, Context context) {
        StringRequest r12 = new StringRequest(Request.Method.POST, Constant.baseurl+"enrollment", new Response.Listener<String>() {
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
                hashMap.put("agent_id", Nibss.agent_code);
                hashMap.put("ticketID", ticketID);
                for(int i = 0; i<finger_image.size(); i++) {
                    hashMap.put("finger_image"+i, finger_image.get(i));
                }
                return hashMap;
            }
        };
        r12.setShouldCache(false);
        r12.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1.0f));
        addToRequestQueue(r12,context);
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