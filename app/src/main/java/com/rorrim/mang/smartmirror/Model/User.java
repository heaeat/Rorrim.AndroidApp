package com.rorrim.mang.smartmirror.Model;

import com.google.gson.annotations.SerializedName;
import com.rorrim.mang.smartmirror.Interface.Connectable;

import java.util.Date;

public class User implements Connectable{

    //Gson이 Json키를 필드에 매핑하기 위해서 필요한데 두개가 같은 경우는 안해도 되지만 소스코드의
    //난독화를 해결하기 위해 사용하는 것이 좋다.
    @SerializedName("uid")
    private String uid;

    @SerializedName("email")
    private String email;

    @SerializedName("profileUrl")
    private String profileUrl;

    private String mirrorUid;

    private String name;

    public User(String uid, String email, String profileUrl){
        this.uid = uid;
        this.email = email;
        this.profileUrl = profileUrl;
        this.profileUrl = baseURL + "/profileImage.jpg?fileName="+uid;
        this.name = name;
    }

    public User(String uid, String email){
        this.uid = uid;
        this.email = email;
        this.profileUrl = baseURL + "/profileImage.jpg?fileName="+uid;
    }

    public String getUid(){
        return uid;
    }

    public String getEmail(){
        return email;
    }

    public String getProfileUrl(){
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl){
        this.profileUrl = profileUrl;
    }

    public String getMirrorUid(){
        return mirrorUid;
    }

    public void setMirrorUid(String mirrorUid){
        this.mirrorUid = mirrorUid;
    }

    public String getName(){ return name;}
    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "User [uid=" + uid + ", email =" + email;
    }

}
