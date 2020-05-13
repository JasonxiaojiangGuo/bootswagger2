package com.jason.bootswagger2.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel

public class User {
    @ApiModelProperty(name = "1", value = "用户id")
    private Integer id;

    @ApiModelProperty(name = "2", value = "用户名")
    private String username;

    @ApiModelProperty(name = "3", value = "用户地址")
    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
