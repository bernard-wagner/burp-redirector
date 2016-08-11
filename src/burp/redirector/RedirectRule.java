/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package burp.redirector;

import java.io.Serializable;
import java.net.URL;
import java.util.UUID;

/**
 *
 * @author vagrant
 */
public class RedirectRule implements Serializable{

    public UUID uuid;
    public boolean enabled;
    
    public int sProtocol;
    public String sHostname;
    public int sPort;

    public int dProtocol;
    public String dHostname;
    public int dPort;

    RedirectRule(int sourceProtocol, String sourceHostname, int sourcePort, int destinationProtocol, String destinationHostname, int destinationPort) {
        this.uuid = UUID.randomUUID();
        this.enabled = true;
        this.sProtocol = sourceProtocol;
        this.sHostname = sourceHostname;
        this.sPort = sourcePort;
        this.dProtocol = destinationProtocol;
        this.dHostname = destinationHostname;
        this.dPort = destinationPort;
    }

     RedirectRule(){
        this.uuid = UUID.randomUUID();
        this.enabled = true;
        this.sProtocol = 0;
        this.sHostname = "";
        this.sPort = 0;
        this.dProtocol = 0;
        this.dHostname = "";
        this.dPort = 0;
    }
    
    public boolean Matches(URL url) {
        return urlToString(url).matches(wildcardToRegex(this.getSourcePattern()));
    }

    public String getSourcePattern() {
        return (this.sProtocol == 0 ? "*://" : (this.sProtocol == 1 ? "http://" : "https://")) + this.sHostname + ":" + (this.sPort == 0 ? "*" : this.sPort)  + "/*";
    }
    
    public String getDestinationPattern() {
        return (this.dProtocol == 0 ? "*://" : (this.dProtocol == 1 ? "http://" : "https://")) + this.dHostname + ":" + (this.dPort == 0 ? "*" : this.dPort) + "/*";
    }

    private String urlToString(URL url) {
        return url.getProtocol() + "://" + url.getHost() + ":" + (url.getPort() == -1 ? url.getDefaultPort() : url.getPort()) + (url.getFile() == "" ? "/" : url.getFile());
    }
    
    public String describe(){
        return getSourcePattern()+"  -->  "+getDestinationPattern();
    }

    public static String wildcardToRegex(String wildcard) {
        StringBuffer s = new StringBuffer(wildcard.length());
        s.append('^');
        for (int i = 0, is = wildcard.length(); i < is; i++) {
            char c = wildcard.charAt(i);
            switch (c) {
                case '*':
                    s.append(".*");
                    break;
                case '?':
                    s.append(".");
                    break;
                case '^': // escape character in cmd.exe
                    s.append("\\");
                    break;
                // escape special regexp-characters
                case '(':
                case ')':
                case '[':
                case ']':
                case '$':
                case '.':
                case '{':
                case '}':
                case '|':
                case '\\':
                    s.append("\\");
                    s.append(c);
                    break;
                default:
                    s.append(c);
                    break;
            }
        }
        s.append('$');
        return (s.toString());
    }
}
