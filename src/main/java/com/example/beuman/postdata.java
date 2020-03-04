package com.example.beuman;

public class postdata {
    private String name,address,needs,age,reason,time,type;
    public  postdata(){

    }
    public postdata(String name,String address,String needs,String age,String reason,String time,String type)
    {
        this.name=name;
        this.address=address;
        this.needs=needs;
        this.age=age;
        this.reason=reason;
        this.time=time;
        this.type=type;
    }

    public String getType() {
        return type;
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

    public String getNeeds() {
        return needs;
    }

    public void setNeeds(String needs) {
        this.needs = needs;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTime() {
        return time;
    }

    public void setType(String time) {
        this.time = time;
    }
}
