package com.schibsted.httpserver.domain.user;

import com.schibsted.httpserver.domain.role.Role;
import java.util.List;

public class Administrator extends User {

    public Administrator(long id, String userName, String password, List<Role> roleList) {
        super(id, userName, password, roleList);
    }

    @Override
    public Boolean isAdmin() {
        return true;
    }

}
