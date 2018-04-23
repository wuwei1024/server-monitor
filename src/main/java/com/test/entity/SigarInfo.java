package com.test.entity;

public class SigarInfo {
    private String name;
    private String value;

    public SigarInfo(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SigarInfo{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
