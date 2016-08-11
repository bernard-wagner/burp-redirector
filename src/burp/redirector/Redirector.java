/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package burp.redirector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author vagrant
 */
public final class Redirector implements RedirectorObserver {

    private static final String PREF_NAME = "burp.redirector";

    private static Preferences prefs;

    private static Redirector instance = null;

    private static ArrayList<RedirectorObserver> observers = new ArrayList<>();

    private ArrayList<RedirectRule> rules;

    public Redirector() {
        prefs = Preferences.userNodeForPackage(Redirector.class);
        String encoded = prefs.get(PREF_NAME, "");
        
        try {
            rules = (ArrayList<RedirectRule>) new ObjectInputStream(new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(encoded))).readObject();
        } catch (IOException | ClassNotFoundException ex) {
            rules  = new ArrayList<>();
        }
        this.registerObserver(this);
    }

    public final ArrayList<RedirectRule> getRules() {
        return rules;
    }

    public void insertOrUpdate(RedirectRule rule) {
        boolean updated = false;
        for (RedirectRule localRule : rules) {
            if (localRule.uuid.equals(rule.uuid)) {
                rules.set(rules.indexOf(localRule), rule);
                updated = true;
            }
        }
        if (!updated) {
            rules.add(rule);
        }
        this.updateObservers();
    }

    public void removeRedirectRule(RedirectRule rule) {
        rules.remove(rule);
        this.updateObservers();
    }

    public static Redirector getInstance() {
        if (instance == null) {
            instance = new Redirector();
        }
        return instance;
    }

    public static void main(String[] args) {
        Redirector redirector = Redirector.getInstance();

        /* RedirectRule rule;

        for (int i = 0; i < 3; i++) {
            rule = new RedirectRule();
            redirector.rules.add(rule);
        }*/
        MainPanel mainPanel = new MainPanel();

        JFrame f = new JFrame();
        f.getContentPane().add(mainPanel);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(300, 300);
        f.show();
    }

    public void moveRuleUp(RedirectRule rule) {
        int index = rules.indexOf(rule);
        if (index > 0) {
            RedirectRule top = rules.get(index - 1);
            rules.set(index - 1, rule);
            rules.set(index, top);
        }
        this.updateObservers();
    }

    public void moveRuleDown(RedirectRule rule) {
        int index = rules.indexOf(rule);
        if (index < rules.size() - 1) {
            RedirectRule down = rules.get(index + 1);
            rules.set(index + 1, rule);
            rules.set(index, down);
        }
        this.updateObservers();
    }

    private void updateObservers() {
        for (RedirectorObserver observer : observers) {
            observer.update();
        }
    }

    public void registerObserver(RedirectorObserver observer) {
        observers.add(observer);
    }

    public void deregisterObserver(RedirectorObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void update() {

        try {
            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            new ObjectOutputStream(writer).writeObject(rules);
            prefs.put(PREF_NAME, DatatypeConverter.printBase64Binary(writer.toByteArray()));
            prefs.flush();            
        } catch (IOException | BackingStoreException ex) {
            
        }
       
    }

}
