package ru.kurtov.remains.orderitems;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "order_id")
    private int orderId;
    
    @Column(name = "goods_name")
    private String goodsName;
    
    @Column(name = "value")
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
    
    public OrderItem(final int orderId, final String goodsName, final int value) {
        this.orderId = orderId;
        this.goodsName = goodsName;
        this.value = value;        
    }
   
    //Для Hibernate
    OrderItem() {} 
    
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
    
    @Override
    public String toString() {
        return String.format(
                "%s{id=%d, orderId='%d', goodsName='%s', value='%d'}",
                getClass().getSimpleName(), id, orderId, goodsName, value
        );
    }
}