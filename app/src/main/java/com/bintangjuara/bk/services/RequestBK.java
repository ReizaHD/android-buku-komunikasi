package com.bintangjuara.bk.services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bintangjuara.bk.models.Announcement;
import com.bintangjuara.bk.models.Feedback;
import com.bintangjuara.bk.models.Subject;
import com.bintangjuara.bk.models.StaticData;
import com.bintangjuara.bk.models.Student;
import com.bintangjuara.bk.models.UserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class RequestBK {

    private static RequestBK instance;
    private static Context ctx;
    private RequestQueue requestQueue;

//    private static final String BASE_URL = "https://siakad.bintangjuara.sch.id/rest_mobile/";
    private static final String BASE_URL = "https://buku-komunikasi.vercel.app/";
    private static final Map<String, String> HEADER = new HashMap<String, String>(){{
        put("X-API-KEY", "sso-ikitas_1993smb11");
    }};



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

    public void requestUser(String email, String password, UserListener listener){
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                BASE_URL + "api/user",
                response -> {
                    JSONObject json;
                    UserData userData = null;
                    try {
                        json = new JSONObject(response);
                        if(json.getBoolean("success")) {
                            String data = json.getString("data");
                            userData = new UserData(data);
                        }

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
                return HEADER;
            }
        };
        getRequestQueue().add(stringRequest);
    }

    public  void userEditPassword(String username, String userId, String currentPass, String newPass, String confirmPass, ResponseListener listener){
        Map<String, String> body = new HashMap();
        body.put("password", currentPass);
        body.put("password1", newPass);
        body.put("password2", confirmPass);
        body.put("username", username);
        body.put("id", userId);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                BASE_URL + "api/user/edit_password",
                listener::onResponse,
                listener::onError
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return body;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header = new HashMap<>();
                header.put("X-API-KEY", "sso-ikitas_1993smb11");
                return header;
            }
        };
        getRequestQueue().add(stringRequest);
    }

    public void requestAnnouncement(int userId,AnnouncementListener listener){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                BASE_URL + "api/announcement",
                response -> {
                    ArrayList<Object> listData = new ArrayList<>();
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    try {
                        JSONObject responseJson = new JSONObject(response);
                        Log.d("response", responseJson.toString());
                        JSONArray dataArray = responseJson.getJSONArray("data");
                        for(int i=0;i<dataArray.length();i++){
                            JSONObject data = dataArray.getJSONObject(i);
                            String type= data.getString("type");
                            int announcementId = data.getInt("announcement_id");
                            if(type.equals("feedback")){
                                int feedbackId = data.getInt("feedback_id");
                                int studentId = data.getInt("student_id");
                                String studentName = data.getString("student_name");
                                String studentClass = data.getString("student_class");
                                String weekendAssignment = data.getString("weekend_assignment");
                                String additionalFeedback = data.getString("additional_feedback");
                                String extracurricular = data.getString("extracurricular");
                                boolean isRead = data.getBoolean("is_read");
                                String strDate = data.getString("date");
                                Log.d("date", strDate);
                                String parentFeedback = "";
                                if(!data.isNull("parent_feedback")){
                                    parentFeedback = data.getString("parent_feedback");
                                }
                                JSONObject subjects = data.getJSONObject("subjects");

                                ArrayList<Subject> listSubject = new ArrayList<>();
                                for (Iterator<String> it = subjects.keys(); it.hasNext(); ) {
                                    String key = it.next();
                                    listSubject.add(new Subject(key, subjects.getString(key)));
                                }
                                Date date = inputFormat.parse(strDate);
                                listData.add(new Feedback(announcementId,feedbackId, studentId, studentName, studentClass, listSubject, weekendAssignment, additionalFeedback, extracurricular, parentFeedback, isRead, date));
                            }else if(type.equals("announcement")){
                                String title = data.getString("announcement_title");
                                String content = data.getString("content");
                                JSONArray imageJson ;
                                String[] images;
                                if(!data.isNull("image")){
                                    imageJson = data.getJSONArray("image");
                                    images = new String[imageJson.length()];
                                    for (int j = 0; j < imageJson.length(); j++) {
                                        images[j] = imageJson.getString(j);
                                    }
                                }else {
                                    images = new String[]{};
                                }
                                boolean isRead = data.getBoolean("is_read");
                                String strDate = data.getString("date");
                                Date date = inputFormat.parse(strDate);
                                listData.add(new Announcement(announcementId, title, content, images, isRead, date));
                            }
                        }
                    } catch (Exception e) {
                        Log.e("JSON Exception", e.toString());
                    }
                    listener.onResponse(listData);
                },
                listener::onError
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("user_id", String.valueOf(userId));
                return body;
            }
        };
        getRequestQueue().add(stringRequest);
    }

    public void announcementRead(String id, ResponseListener listener){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                BASE_URL + "api/announcement/read",
                listener::onResponse,
                listener::onError
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("announcement_id",id);
                return body;
            }
        };
        getRequestQueue().add(stringRequest);
    }

    public void insertFeedback(String msg, String id, ResponseListener listener){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                BASE_URL + "api/feedback/insert_parent",
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
                BASE_URL + "api/user/students",
                response -> {
                    ArrayList<Student> students = new ArrayList<>();
                    try {
                        JSONObject responseJson = new JSONObject(response);
                        JSONArray jsonArray = responseJson.getJSONArray("data");
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
                error -> {
                    ArrayList<Student> students = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(StaticData.getStudent());
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
                    listener.onError(error, students);
                }
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

    public interface AnnouncementListener {
        void onResponse(ArrayList<Object> listData);
        void onError(Exception error);
    }

    public interface StudentListener{
        void onResponse(ArrayList<Student> students);
        void onError(Exception error, ArrayList<Student> students);
    }
}
