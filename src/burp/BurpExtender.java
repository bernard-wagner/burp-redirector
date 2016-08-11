package burp;

import burp.redirector.MainPanel;
import burp.redirector.RedirectRule;
import burp.redirector.Redirector;
import java.awt.*;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
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
                    try {
                        IRequestInfo rqInfo = this.callbacks.getHelpers().analyzeRequest(message.getMessageInfo());
                        URL redirect = rule.createRedirect(url);
                        String request = new String(message.getMessageInfo().getRequest());
                        message.getMessageInfo().setRequest(request.replace(rqInfo.getUrl().getFile(), redirect.getFile()).getBytes());                             
                        message.getMessageInfo().setHttpService(callbacks.getHelpers().buildHttpService(redirect.getHost(), redirect.getPort(), "https".equals(redirect.getProtocol())));
                        message.setInterceptAction(IInterceptedProxyMessage.ACTION_FOLLOW_RULES);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(BurpExtender.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }
    }
}
