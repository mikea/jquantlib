package org.jquantlib.xmlrpc.server;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

public class Server {
    private static final int port = 8080;

    public static void main(String[] args) throws Exception {
        WebServer webServer = new WebServer(port);

        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

        PropertyHandlerMapping phm = new PropertyHandlerMapping();
        /*
         * Load handler definitions from a property file. The property file might look like:
         * Calculator=org.apache.xmlrpc.demo.Calculator
         * org.apache.xmlrpc.demo.proxy.Adder=org.apache.xmlrpc.demo.proxy.AdderImpl
         */
        phm.load(Thread.currentThread().getContextClassLoader(), "org/apache/xmlrpc/webserver/XmlRpcServlet.properties");

        /*
         * You may also provide the handler classes directly, like this: phm.addHandler("Calculator",
         * org.apache.xmlrpc.demo.Calculator.class); phm.addHandler(org.apache.xmlrpc.demo.proxy.Adder.class.getName(),
         * org.apache.xmlrpc.demo.proxy.AdderImpl.class);
         */
        xmlRpcServer.setHandlerMapping(phm);

        XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(false);

        webServer.start();
    }
}
