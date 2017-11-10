package com.github.Entity;

import com.github.dubbo.DubboEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author yu.gao 2017-11-09 下午10:03
 */
public class Hello {

    private Integer id;

    private String name;

    private DubboEntity dubboEntity;

    public Hello() {
    }

    private Hello(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setDubboEntity(builder.dubboEntity);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DubboEntity getDubboEntity() {
        return dubboEntity;
    }

    public void setDubboEntity(DubboEntity dubboEntity) {
        this.dubboEntity = dubboEntity;
    }

    public static final class Builder {
        private Integer id;
        private String name;
        private DubboEntity dubboEntity;

        private Builder() {
        }

        public Builder id(Integer val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder dubboEntity(DubboEntity val) {
            dubboEntity = val;
            return this;
        }

        public Hello build() {
            return new Hello(this);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
