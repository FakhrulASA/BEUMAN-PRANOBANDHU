package com.example.beuman;

public class askHelper {
    String name,address,phone,type,needs,time,age,imageurl;
    public askHelper()
    {
        //empty constructor
    }
    public askHelper(String name,String address,String phone,String type,String needs,String time,String age,String imageurl)
    {
        this.name=name;
        this.address=address;
        this.phone=phone;
        this.type=type;
        this.needs=needs;
        this.time=time;
        this.age=age;
        this.imageurl=imageurl;
    }

    public String getUserid() {
        return imageurl;
    }

    public void setUserid(String userid) {
        this.imageurl = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNeeds() {
        return needs;
    }

    public void setNeeds(String needs) {
        this.needs = needs;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
