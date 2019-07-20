/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roiland.platform.annotation.utils;

import com.roiland.platform.annotation.iface.beanutils.BeanProperty;
import com.roiland.platform.annotation.iface.spi.Extension;
import com.roiland.platform.annotation.iface.spi.SPI;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jeffy.yang
 */
public class AnnotationUtilsTest {
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isTargetAnnotation method, of class AnnotationUtils.
     */
    @Test
    public void testIsTargetAnnotation() {
        assertTrue(AnnotationUtils.isTargetAnnotation(SPI.class, TestSpiService.class));
        assertTrue(AnnotationUtils.isTargetAnnotation(Extension.class, TestSpiService.class));
        assertFalse(AnnotationUtils.isTargetAnnotation(BeanProperty.class, TestSpiService.class));
    }

    /**
     * Test of explain method, of class AnnotationUtils.
     */
    @Test
    public void testExplain() {
        Extension result = AnnotationUtils.explain(Extension.class, TestSpiService.class);
        assertNotNull(result);
        assertEquals("jeffy.yang", result.value());
        
        
        BeanProperty firstname = AnnotationUtils.explain(BeanProperty.class, TestSpiService.class, "firstName");
        assertEquals("jeffy", firstname.value());
    }

    /**
     * Test of findByAnnotationProperty method, of class AnnotationUtils.
     */
    @Test
    public void testFindByAnnotationProperty() {
        String result = AnnotationUtils.findByAnnotationProperty(Extension.class, TestSpiService.class, "value", String.class);
        assertEquals("jeffy.yang", result);
    }

    /**
     * Test of check method, of class AnnotationUtils.
     */
    @Test
    public void testCheck() {
        boolean result = AnnotationUtils.check(Extension.class, TestSpiService.class, "value", "jeffy.yang");
        assertTrue(result);
        
        result = AnnotationUtils.check(Extension.class, TestSpiService.class, "value", "jeffy");
        assertFalse(result);
    }

    /**
     * Test of findFieldByAnnotationProperty method, of class AnnotationUtils.
     */
    @Test
    public void testFindFieldByAnnotationProperty() {
        Field result = AnnotationUtils.findFieldByAnnotationProperty(BeanProperty.class, TestSpiService.class, "value", "jeffy");
        assertEquals("firstName", result.getName());
    }

    /**
     * Test of findFieldNameByAnnotationProperty method, of class AnnotationUtils.
     */
    @Test
    public void testFindFieldNameByAnnotationProperty() {
        String result = AnnotationUtils.findFieldNameByAnnotationProperty(BeanProperty.class, TestSpiService.class, "value", "jeffy");
        assertEquals("firstName", result);
    }

    /**
     * Test of findFields method, of class AnnotationUtils.
     */
    @Test
    public void testFindFields() {
        Set<Field> result = AnnotationUtils.findFields(BeanProperty.class, TestSpiService.class);
        List<String> names = new ArrayList<>(result.size());
        for (Field field : result) {
            names.add(field.getName());
        }
        assertEquals(2, result.size());
        assertTrue(names.contains("firstName"));
        assertTrue(names.contains("lastName"));
    }

    /**
     * Test of findAsMap method, of class AnnotationUtils.
     */
    @Test
    public void testFindAsMap() {
        Map result = AnnotationUtils.findAsMap(BeanProperty.class, TestSpiService.class, "value", String.class);
        assertEquals(2, result.size());
        assertEquals("jeffy", result.get("firstName"));
        assertEquals("yang", result.get("lastName"));
    }

    /**
     * Test of findAsReverseMap method, of class AnnotationUtils.
     */
    @Test
    public void testFindAsReverseMap() {
        Map result = AnnotationUtils.findAsReverseMap(BeanProperty.class, TestSpiService.class, "value", String.class);
        assertEquals(2, result.size());
        assertEquals("firstName", result.get("jeffy"));
        assertEquals("lastName", result.get("yang"));
    }
    
}
