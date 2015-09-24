package com.lenote.qiutu.api;

import java.io.Serializable;

/**
 * Created by lenote on 2015/9/10.
 */
public class TestBean implements Serializable {
    String name;
    String value;

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
}
