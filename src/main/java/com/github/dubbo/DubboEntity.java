package com.github.dubbo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-07-20 下午9:17
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */
public class DubboEntity {

    private int balance;

    private String stock;

    private DubboEntity(Builder builder) {
        setBalance(builder.balance);
        setStock(builder.stock);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public static final class Builder {
        private int balance;
        private String stock;

        private Builder() {
        }

        public Builder balance(int val) {
            balance = val;
            return this;
        }

        public Builder stock(String val) {
            stock = val;
            return this;
        }

        public DubboEntity build() {
            return new DubboEntity(this);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
