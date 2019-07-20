package com.roiland.platform.dubbo.filter.api;

import com.roiland.platform.dubbo.filter.api.IUserService.User;

public interface IService <P, V> {
    V get(User user);
}
