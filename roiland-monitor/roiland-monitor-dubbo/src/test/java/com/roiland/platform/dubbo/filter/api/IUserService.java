/**
 * Project: dubbo-examples
 * 
 * File Created at 2012-2-17
 * $Id$
 * 
 * Copyright 1999-2100 Alibaba.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package com.roiland.platform.dubbo.filter.api;

import java.io.Serializable;
import com.roiland.platform.dubbo.filter.api.IUserService.Params;
import com.roiland.platform.dubbo.filter.api.IUserService.User;

/**
 * @author chao.liuc
 *
 */
public interface IUserService extends IService<Params, User> {


    class Params implements Serializable{
        private static final long serialVersionUID = 1L;

        public Params(String query) {
        }
    }
    class User implements Serializable{
        private static final long serialVersionUID = 1L;
        public User(int id, String name) {
            super();
            this.id = id;
            this.name = name;
        }
        private int id;
        private String name;
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        @Override
        public String toString() {
            return "User [id=" + id + ", name=" + name + "]";
        }
    }
}
