package com.bintangjuara.bk;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bintangjuara.bk.adapters.MessageAdapter;
import com.bintangjuara.bk.models.Berita;
import com.bintangjuara.bk.models.Pelajaran;
import com.bintangjuara.bk.models.UserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RequestBK {

    private static RequestBK instance;
    private static Context ctx;
    private RequestQueue requestQueue;

    private static final String BASE_URL = "https://siakad.bintangjuara.sch.id/rest_mobile/";
    private static final String TEMP_URL = "http://192.168.1.13/buku_komunikasi/school/";


    private RequestBK(Context context){
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized RequestBK getInstance(Context context) {
        if (instance == null)
            instance = new RequestBK(context);
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public void requestUser(Map<String, String> body, UserListener listener){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                BASE_URL + "rest_user",
                response -> {
                    JSONObject json;
                    UserData userData = null;
                    try {
                        json = new JSONObject(response);
                        String data = json.getString("data");
                        userData = new UserData(data);
                    } catch (JSONException e) {
                        Log.e("JSON Exception", e.toString());
                    }
                    listener.onResponse(userData);
                },
                listener::onError
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return body;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-API-KEY", "sso-ikitas_1993smb11");
                return headers;
            }
        };
        getRequestQueue().add(stringRequest);
    }

    public void requestBerita(BeritaListener listener){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                TEMP_URL + "feedback_rest.php",
                response -> {
                    ArrayList<Berita> listBerita = new ArrayList<>();
                    try {
                        JSONObject responseJson = new JSONObject(response);
                        Log.d("Response", responseJson.getString("status"));
                        JSONArray jsonArray = responseJson.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String id = obj.getString("feedback_id");
                            String tugasWeekend = obj.getString("weekend_assignment");
                            String catatan = obj.getString("additional_feedback");
                            String ekstrakurikuler = obj.getString("extracurricular");
                            String catatanOrtu = "NULL";
                            JSONObject mapel = obj.getJSONObject("subjects");

                            ArrayList<Pelajaran> listPelajaran = new ArrayList<>();
                            for (Iterator<String> it = mapel.keys(); it.hasNext(); ) {
                                String key = it.next();
                                listPelajaran.add(new Pelajaran(key, mapel.getString(key)));
                            }
                            listBerita.add(new Berita(id,tugasWeekend, catatan, ekstrakurikuler, catatanOrtu, listPelajaran));
                        }
                    } catch (JSONException e) {
                        Log.e("JSON Exception", e.toString());
                    }
                    listener.onResponse(listBerita);
                },
                listener::onError
        );
        getRequestQueue().add(stringRequest);
    }

    public void beritaRead(String id, ResponseListener listener){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                TEMP_URL + "feedback_read.php",
                listener::onResponse,
                listener::onError
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("feedback_id",id);
                return body;
            }
        };
        getRequestQueue().add(stringRequest);
    }

    public void insertFeedback(String msg, String id, ResponseListener listener){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                TEMP_URL + "parent_feedback_insert.php",
                listener::onResponse,
                listener::onError
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("feedback_id", id);
                body.put("parent_feedback",msg);
                return body;
            }
        };
        getRequestQueue().add(stringRequest);
    }

    public interface ResponseListener{
        void onResponse(String response);
        void onError(Exception error);
    }

    public interface UserListener{
        void onResponse(UserData userData);
        void onError(Exception error);
    }

    public interface BeritaListener{
        void onResponse(ArrayList<Berita> listBerita);
        void onError(Exception error);
    }
}
