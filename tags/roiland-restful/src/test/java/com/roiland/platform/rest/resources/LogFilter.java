package com.roiland.platform.rest.resources;

import javax.ws.rs.NameBinding;
import java.lang.annotation.*;

/**
 * Created by user on 2015/12/4.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@NameBinding
public @interface LogFilter {
}
