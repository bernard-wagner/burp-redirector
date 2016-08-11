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
        RedirectRule rule1 = new RedirectRule(1, "www.mybroadband.co.za", 80, 1, "localost", 8080);
        if (!rule1.Matches(new URL("http://www.mybroadband.co.za"))) fail("Matcher failed");
        if (!rule1.Matches(new URL("http://www.mybroadband.co.za/"))) fail("Matcher failed");
        if (!rule1.Matches(new URL("http://www.mybroadband.co.za/news"))) fail("Matcher failed");
        if (rule1.Matches(new URL("http://www.google.co.za/"))) fail("Matcher failed");
        
        RedirectRule rule2 = new RedirectRule(1, "*.mybroadband.co.za", 80, 1, "localost", 8080);
        if (!rule2.Matches(new URL("http://www.mybroadband.co.za"))) fail("Matcher failed");
        if (!rule2.Matches(new URL("http://www.mybroadband.co.za/"))) fail("Matcher failed");
        if (!rule2.Matches(new URL("http://www.mybroadband.co.za/news"))) fail("Matcher failed");   
        if (rule2.Matches(new URL("http://www.google.co.za/"))) fail("Matcher failed");

        RedirectRule rule3 = new RedirectRule(1, "www.*.co.za", 80, 1, "localost", 8080);
        if (!rule3.Matches(new URL("http://www.mybroadband.co.za"))) fail("Matcher failed");
        if (!rule3.Matches(new URL("http://www.mybroadband.co.za/"))) fail("Matcher failed");
        if (!rule3.Matches(new URL("http://www.mybroadband.co.za/news"))) fail("Matcher failed");  
        if (!rule3.Matches(new URL("http://www.google.co.za/"))) fail("Matcher failed");        
        if (rule3.Matches(new URL("http://www.google.com/"))) fail("Matcher failed");        
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
}
