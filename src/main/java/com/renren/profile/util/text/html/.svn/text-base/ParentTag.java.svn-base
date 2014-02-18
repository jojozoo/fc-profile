package com.renren.profile.util.text.html;



public class ParentTag implements Cloneable  {

    private String value;
    private int distance;

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        if(distance < 0){
            return Integer.MAX_VALUE;
        }
        return distance;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public Object clone() {
        
        ParentTag toReturn = new ParentTag();

        toReturn.setValue(value);
        toReturn.setDistance(distance);
        return toReturn;
    }

}
