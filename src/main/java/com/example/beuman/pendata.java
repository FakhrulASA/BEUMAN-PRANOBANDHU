package com.example.beuman;

public class pendata {
    private String name;
    private String description;
    private String needs;
    private String age;
    public pendata()
    {

    }
    public pendata(String name,String description,String needs,String age)
    {
             this.name=name;
             this.description=description;
             this.needs=needs;
             this.age=age;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getNeeds() {
        return needs;
    }

    public String getAge() {
        return age;
    }
}
