package com.roiland.platform.dubbo.filter.impl;

import com.roiland.platform.dubbo.filter.api.IUserService;

public class UserServiceImpl implements IUserService {

    public User get(User user) {
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return new User(user.getId(), user.getName() + "-provider");
    }
}
