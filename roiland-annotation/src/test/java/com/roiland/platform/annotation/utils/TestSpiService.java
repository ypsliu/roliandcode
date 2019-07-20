/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roiland.platform.annotation.utils;

import com.roiland.platform.annotation.iface.beanutils.BeanProperty;
import com.roiland.platform.annotation.iface.spi.Extension;
import com.roiland.platform.annotation.iface.spi.SPI;


    @SPI
    @Extension(value = "jeffy.yang")
    public class TestSpiService {
        
        public String name;
        
        @BeanProperty(value = "jeffy")
        public String firstName;
        
        @BeanProperty(value = "yang")
        public String lastName;
    }
    
