package burp;

import burp.redirector.MainPanel;
import burp.redirector.RedirectRule;
import burp.redirector.Redirector;
import java.awt.*;
import java.io.PrintWriter;
import java.net.URL;
import java.util.logging.Logger;

public class BurpExtender implements IBurpExtender, IProxyListener, ITab {

    private IBurpExtenderCallbacks callbacks;
    private PrintWriter stdout;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
        
        stdout = new PrintWriter(callbacks.getStdout(), true);
        callbacks.addSuiteTab(this);
        callbacks.registerProxyListener(this);
        
    }

    @Override
    public String getTabCaption() {
        return "Redirector";
    }

    @Override
    public Component getUiComponent() {
        return new MainPanel();
    }

    @Override
    public void processProxyMessage(boolean messageIsRequest, IInterceptedProxyMessage message) {
        if (messageIsRequest) {
            URL url = callbacks.getHelpers().analyzeRequest(message.getMessageInfo()).getUrl();
            for (RedirectRule rule : Redirector.getInstance().getRules()) {

                if (rule.enabled && rule.Matches(url)) {
                    boolean https = rule.dProtocol == 0 ? "https".equals(url.getProtocol()) :  rule.dProtocol == 2;
                    int port = rule.dPort == 0 ?  url.getPort() : rule.dPort;
                    String hostname = rule.dHostname;
                    stdout.println((https ? "https::/" : "http://") + hostname + ":" + port );
                    message.getMessageInfo().setHttpService(callbacks.getHelpers().buildHttpService(hostname,port,https));
                    message.setInterceptAction(IInterceptedProxyMessage.ACTION_FOLLOW_RULES);
                }
            }

        }
    }
}
