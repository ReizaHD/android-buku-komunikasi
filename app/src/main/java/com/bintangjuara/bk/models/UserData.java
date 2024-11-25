package com.bintangjuara.bk.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class UserData implements Serializable {
    int id, roleId;
    String name, image, email, profile;

    public UserData(String data){
        try {
            JSONObject dataJson = new JSONObject(data);
            this.email = dataJson.getString("user_email");
            this.id = Integer.parseInt(dataJson.getString("user_id"));
            this.image = dataJson.getString("user_image");
            this.name = dataJson.getString("user_name");
            this.profile = dataJson.getString("profile");
            this.roleId = 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
