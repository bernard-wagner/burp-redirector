/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package burp.redirector;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public String sPath;

    public int dProtocol;
    public String dHostname;
    public int dPort;
    public String dPath;

    RedirectRule(int sourceProtocol, String sourceHostname, int sourcePort, String sourcePath, int destinationProtocol, String destinationHostname, int destinationPort, String destinationPath) {
        this.uuid = UUID.randomUUID();
        this.enabled = true;
        this.sProtocol = sourceProtocol;
        this.sHostname = sourceHostname;
        this.sPort = sourcePort;
        this.sPath = sourcePath;
        this.dProtocol = destinationProtocol;
        this.dHostname = destinationHostname;
        this.dPort = destinationPort;
        this.dPath = destinationPath;
    }

     RedirectRule(){
        this.uuid = UUID.randomUUID();
        this.enabled = true;
        this.sProtocol = 0;
        this.sHostname = "";
        this.sPort = 0;
        this.sPath = "(?<path>.*)";
        this.dProtocol = 0;
        this.dHostname = "";
        this.dPort = 0;
        this.dPath = "${path}";
    }
    
    public boolean Matches(URL url) {
        return urlToString(url).matches(this.getSourcePattern());
    }

    public URL createRedirect(URL url) throws MalformedURLException {
         StringBuffer buffer = new StringBuffer();
        
         Pattern srcPattern = Pattern.compile(sPath);

         Matcher matcher = srcPattern.matcher(url.getPath());
         
         
        
         String result = (dProtocol == 0 ? "https".equals(url.getProtocol()) :  dProtocol == 2) ? "https://" : "http://"
                 + dHostname + ":" 
                 + Integer.toString(dPort == 0 ?  url.getPort() : dPort) 
                 + matcher.replaceAll(dPath);
                 
                            
        return new URL(result);
    }
    
    public String getSourcePattern() {
        return wildcardToRegex((this.sProtocol == 0 ? "*://" : (this.sProtocol == 1 ? "http://" : "https://")) + this.sHostname + ":" + (this.sPort == 0 ? "*" : this.sPort))  + sPath;
    }
    
    public String getDestinationPattern() {
        return (this.dProtocol == 0 ? "*://" : (this.dProtocol == 1 ? "http://" : "https://")) + this.dHostname + ":" + (this.dPort == 0 ? "*" : this.dPort) + dPath;
    }

    private String urlToString(URL url) {
        return url.getProtocol() + "://" + url.getHost() + ":" + (url.getPort() == -1 ? url.getDefaultPort() : url.getPort()) + (url.getFile() == "" ? "/" : url.getFile());
    }
    
    public String describe(){
        return getSourcePattern()+"  -->  "+getDestinationPattern();
    }

    public static String wildcardToRegex(String wildcard) {
        StringBuffer s = new StringBuffer(wildcard.length());
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
        return (s.toString());
    }
}
