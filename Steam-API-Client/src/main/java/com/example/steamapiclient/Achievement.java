package com.example.steamapiclient;

public class Achievement {

    private String name;
    private Double percent;


    public Achievement(String name, Double percent) {
        this.name = name;
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "name='" + name + '\'' +
                ", percent=" + percent +
                '}';
    }
}
