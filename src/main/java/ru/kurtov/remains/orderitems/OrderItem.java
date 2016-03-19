package ru.kurtov.remains.orderitems;

import java.util.Objects;

public class OrderItem {
    private Integer id;
    private int orderId;
    private String goodsName;
    private int value;

    public static OrderItem create(final int orderId, final String goodsName, final int value) {
        return new OrderItem(null, orderId, goodsName, value);
    }

    public static OrderItem existing(final Integer id, final int orderId, final String goodsName, final int value) {
        return new OrderItem(id, orderId, goodsName, value);
    }
    
    private OrderItem(final Integer id, final int orderId, final String goodsName, final int value) {
        this.id = id;
        this.orderId = orderId;
        this.goodsName = goodsName;
        this.value = value;
    }
   
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }        
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrderItem other = (OrderItem) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (this.orderId != other.orderId) {
            return false;
        }
        if (!Objects.equals(this.goodsName, other.goodsName)) {
            return false;
        }
        if (this.value != other.value) {
            return false;
        }
        return true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
