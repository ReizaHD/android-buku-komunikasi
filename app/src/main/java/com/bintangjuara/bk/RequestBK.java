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
import com.bintangjuara.bk.models.NewBerita;
import com.bintangjuara.bk.models.Pelajaran;
import com.bintangjuara.bk.models.Student;
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
                            int feedbackId = obj.getInt("feedback_id");
                            int studentId = obj.getInt("student_id");
                            String studentName = obj.getString("student_name");
                            String studentClass = obj.getString("student_class");
                            String weekendAssignment = obj.getString("weekend_assignment");
                            String additionalFeedback = obj.getString("additional_feedback");
                            String extracurricular = obj.getString("extracurricular");
                            boolean isRead = obj.getBoolean("is_read");
                            String date = obj.getString("date");
                            String parentFeedback = "";
                            if(!obj.isNull("parent_feedback")){
                                parentFeedback = obj.getString("parent_feedback");
                            }
                            JSONObject subjects = obj.getJSONObject("subjects");

                            ArrayList<Pelajaran> listSubject = new ArrayList<>();
                            for (Iterator<String> it = subjects.keys(); it.hasNext(); ) {
                                String key = it.next();
                                listSubject.add(new Pelajaran(key, subjects.getString(key)));
                            }
                            listBerita.add(new Berita(feedbackId, studentId, studentName, studentClass, listSubject, weekendAssignment, additionalFeedback, extracurricular, parentFeedback, isRead, date));
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

    public void requestStudent(int userId, StudentListener listener){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                TEMP_URL + "student_rest.php",
                response -> {
                    ArrayList<Student> students = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i=0; i < jsonArray.length(); i++){
                            JSONObject obj = jsonArray.getJSONObject(i);
                            int id = obj.getInt("id");
                            String name = obj.getString("name");
                            String className = obj.getString("class");
                            students.add(new Student(id, name, className));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    listener.onResponse(students);
                },
                listener::onError
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> body = new HashMap<>();
                body.put("user_id", String.valueOf(userId));
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

    public interface StudentListener{
        void onResponse(ArrayList<Student> students);
        void onError(Exception error);
    }
}
