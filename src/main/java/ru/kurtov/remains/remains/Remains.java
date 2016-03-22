package ru.kurtov.remains.remains;

import java.util.Objects;

public class Remains {
    private Integer id;
    private String goodsName;
    private int value;

    public static Remains create(final String goodsName, final int value) {
        return new Remains(null, goodsName, value);
    }

    public static Remains existing(final Integer id, final String goodsName, final int value) {
        return new Remains(id, goodsName, value);
    }
    
    private Remains(Integer id, String goodsName, int value) {
        this.id = id;
        this.goodsName = goodsName;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Remains other = (Remains) obj;
        if (!Objects.equals(this.id, other.id)) {
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
    
    @Override
    public String toString() {
        return String.format(
                "%s{id=%d, goodsName='%s', value='%d'}",
                getClass().getSimpleName(), id, goodsName, value
        );
    }
}