package com.gsven.library.widget.graph;


/**
 * @Autor: WGsven
 * @Time: 2019/1/8 \
 * @Desc: Depth
 * 走势数据
 */

public class Depth implements Comparable<Depth> {

    private double price;
    private String volume;//日期
    //buy:0, sell:1
    private int tradeType;
    private float x;
    private float y;

    public Depth() {
    }

    public Depth(double price, String volume, int tradeType) {
        this.price = price;
        this.volume = volume;
        this.tradeType = tradeType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public int compareTo(Depth o) {
        double diff = this.getPrice() - o.getPrice();
        if (diff > 0) {
            return 1;
        } else if (diff < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Depth{" +
                "price=" + price +
                ", volume=" + volume +
                ", tradeType=" + tradeType +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
