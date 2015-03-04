package org.oregami.data;

/**
 * Created by sebastian on 04.03.15.
 */
public class RevisionInfo {


    public RevisionInfo(Number n, Object value) {
        this.n = n;
        this.value = value;
    }


    private Number n;
    private Object value;

    public Number getN() {
        return n;
    }

    public void setN(Number n) {
        this.n = n;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
