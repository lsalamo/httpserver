package com.schibsted.httpserver.infraestructure.user;

import com.schibsted.httpserver.domain.role.Role;
import com.schibsted.httpserver.domain.user.Administrator;
import com.schibsted.httpserver.domain.user.AdministratorService;
import com.schibsted.httpserver.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AdministratorServiceImpl extends UserServiceImpl implements AdministratorService {

    public static final String FIELD_USER = "user";
    public static final String FIELD_PWD = "pwd";
    public static final String FIELD_ROLE = "role";

    public AdministratorServiceImpl(UserInMemoryRepository userRepository) {
        super(userRepository);
    }

    @Override
    public Optional<User> addUser(Map<String, String> post) {
        User user;
        String userName = post.get(FIELD_USER);
        String password = post.get(FIELD_PWD);
        String role = post.get(FIELD_ROLE);

        if (userName != null && password != null && role != null) {
            List<Role> roleList = getRoleList(role);
            if (!roleList.isEmpty()) {
                int id = getUserRepository().getCountUserRepository().incrementAndGet();
                if (isAdminNewUser(roleList)) user = new Administrator(id, userName, password, roleList);
                else user = new User(id, userName, password, roleList);
                getUserRepository().getUserConcurrentHashMap().put(id, user);
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> updateUser(Integer id, Map<String, String> post) {
        String userName = post.get("user");
        String password = post.get("pwd");
        String role = post.get("role");

        Optional<User> user = super.getUserById(id);
        if (user.isPresent()) {
            if (post.get("user") != null) user.get().setUserName(userName);
            if (post.get("pwd") != null) user.get().setPassword(password);

            List<Role> roleList = getRoleList(role);
            if (!roleList.isEmpty()) {
                user.get().setRoleList(roleList);
            }
        }
        return user;
    }

    private Role getRole(String role) {
        Role result = Role.NONE;
        switch (role.toUpperCase()) {
            case "ADMIN":
                result= Role.ADMIN;
                break;
            case "PAGE_1":
                result = Role.PAGE_1;
                break;
            case "PAGE_2":
                result = Role.PAGE_2;
                break;
            case "PAGE_3":
                result = Role.PAGE_3;
                break;
        }
        return result;
    }

    private List<Role> getRoleList(String roleListStr) {
        List<Role> roleList = new ArrayList<>();
        List<String> stringList = Pattern.compile(UserInMemoryRepository.SEPARATOR_ROLE)
                .splitAsStream(roleListStr)
                .collect(Collectors.toList());
        stringList.forEach(s -> {
            Role role = getRole(s);
            if (role != Role.NONE) {
                roleList.add(role);
            }
        });
        return roleList;
    }

    private Boolean isAdminNewUser(List<Role> roleList) {
        return roleList.stream().filter(c->c.toString().toUpperCase().equals(Role.ADMIN.toString())).findFirst().isPresent();
    }

}
