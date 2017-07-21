package com.github.dubbo;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-07-20 下午9:17
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */
public class DubboEntity {

    private int balance;

    public int getBalance() {
        return balance;
    }

    public static DubboEntity.Builder builder() {
        return new DubboEntity.Builder();
    }

    public DubboEntity(){}

    public DubboEntity(int balance) {
        this.balance = balance;
    }

    public static class Builder {
        private int balance;

        public Builder() {

        }

        public Builder balance(int balance) {
            this.balance = balance;
            return this;
        }

        public DubboEntity build() {
            return new DubboEntity(this.balance);
        }
    }
}
