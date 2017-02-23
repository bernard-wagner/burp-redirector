package burp;

import burp.redirector.MainPanel;
import burp.redirector.Redirector;
import java.awt.Component;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
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
        return new MainPanel(callbacks);
    }

    @Override
    public void processProxyMessage(boolean messageIsRequest, IInterceptedProxyMessage message) {
        if (messageIsRequest) {
            URL url = callbacks.getHelpers().analyzeRequest(message.getMessageInfo()).getUrl();
            Redirector.getInstance().getRules().stream().filter((rule) -> (rule.enabled && rule.Matches(url))).forEachOrdered((rule) -> {
                try {
                    IRequestInfo rqInfo = this.callbacks.getHelpers().analyzeRequest(message.getMessageInfo());
                    List<String> headers = rqInfo.getHeaders();
                    
                    URL redirect = rule.createRedirect(url);
                    
                    //Update path
                    headers.set(0, headers.get(0).replaceFirst(rqInfo.getUrl().getPath(), redirect.getPath()));
                    
                    //Rebuild request
                    byte[] request = callbacks.getHelpers().buildHttpMessage(headers, Arrays.copyOfRange(message.getMessageInfo().getRequest(), 
                            rqInfo.getBodyOffset(), 
                            message.getMessageInfo().getRequest().length));
                    
                    //Update request
                    message.getMessageInfo().setRequest(request);
                    
                    //Build new HTTP service
                    message.getMessageInfo().setHttpService(callbacks.getHelpers().buildHttpService(redirect.getHost(), redirect.getPort(), "https".equals(redirect.getProtocol())));
                    
                    message.setInterceptAction(IInterceptedProxyMessage.ACTION_FOLLOW_RULES);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(BurpExtender.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        }
    }

}
