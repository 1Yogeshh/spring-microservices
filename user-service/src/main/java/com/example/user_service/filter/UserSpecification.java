package com.example.user_service.filter;

import org.springframework.data.jpa.domain.Specification;
import com.example.user_service.model.User;

public class UserSpecification {
    public static Specification<User> hasUername(String username){
        return (root, query , cb )->
                   (username == null) ? null :cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase()+ "%");
    }     
}
