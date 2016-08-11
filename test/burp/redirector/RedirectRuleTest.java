/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package burp.redirector;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vagrant
 */
public class RedirectRuleTest {
    
    public RedirectRuleTest() {
    }
    
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

    @Test
    public void testSomeMethod() throws MalformedURLException {
        RedirectRule rule1 = new RedirectRule(1, "www.mybroadband.co.za", 80, "(?<path>.*)", 1, "localhost", 8080, "${path}");
        RedirectRule rule2 = new RedirectRule(1, "*.mybroadband.co.za",  80, "(?<path>.*)", 1, "localhost", 8080, "${path}");
        RedirectRule rule3 = new RedirectRule(1, "www.*.co.za",  80, "(?<path>.*)", 1, "localhost", 8080, "${path}");
        URL url1 = new URL("http://www.mybroadband.co.za");
        URL url2 = new URL("http://www.mybroadband.co.za/");
        URL url3 = new URL("http://www.mybroadband.co.za/news");
        URL url4 = new URL("http://www.google.co.za/");
        if (!rule1.Matches(url1)) fail("Matcher failed");
        if (!rule1.Matches(url2)) fail("Matcher failed");
        if (!rule1.Matches(url3)) fail("Matcher failed");
        if (rule1.Matches(url4)) fail("Matcher failed");
        
        
        if (!rule2.Matches(url1)) fail("Matcher failed");
        if (!rule2.Matches(url2)) fail("Matcher failed");
        if (!rule2.Matches(url3)) fail("Matcher failed");
        if (rule2.Matches(url4)) fail("Matcher failed");

        
        if (!rule3.Matches(url1)) fail("Matcher failed");
        if (!rule3.Matches(url2)) fail("Matcher failed");
        if (!rule3.Matches(url3)) fail("Matcher failed");
        if (!rule3.Matches(url4)) fail("Matcher failed");     
        
        System.out.println(rule1.createRedirect(url1).toString());
        System.out.println(rule1.createRedirect(url2).toString());
        System.out.println(rule1.createRedirect(url3).toString());
        
        System.out.println(rule2.createRedirect(url1).toString());
        System.out.println(rule2.createRedirect(url2).toString());
        System.out.println(rule2.createRedirect(url3).toString());

        System.out.println(rule3.createRedirect(url1).toString());
        System.out.println(rule3.createRedirect(url2).toString());
        System.out.println(rule3.createRedirect(url3).toString());        
        System.out.println(rule3.createRedirect(url4).toString());   
        //if (rule3.Matches(url4)) fail("Matcher failed");        
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
}
