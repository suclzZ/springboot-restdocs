package com.sucl.restdocs.web;

import com.sucl.restdocs.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sucl
 * @since 2019/11/5
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Long id){
        return User.of().setUserId(id);
    }

    @GetMapping
    public List<User> getUsers(@RequestParam(required = false) String userName,
                               @RequestParam(required = false) String userCaption,
                               @RequestParam(required = false) Integer age){
        List<User> users = new ArrayList<>();
        if(!(userName==null && userCaption==null && age==null)){
            users.add(User.of().setUserName(userName).setUserCaption(userCaption).setAge(age));
        }
        return users;
    }

    @PostMapping
    public User saveUser(@RequestBody User user){
        return user;
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Long id){
        if(id==-1){
            return false;
        }
        return true;
    }

}
