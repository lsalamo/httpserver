package com.schibsted.httpserver.domain.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.schibsted.httpserver.domain.role.Role;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties({ "password" })
public class User implements IUser {

    @JsonProperty
    private long id;

    @JsonProperty
    private String userName;

    private String password;

    @JsonProperty
    private List<Role> roleList;

    public User(long id, String userName, String password, List<Role> roleList) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.roleList = roleList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @Override
    public Boolean isAdmin() {
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", roleList=" + roleList.stream().map(Object::toString).collect(Collectors.joining(",")) +
                '}';
    }

}
