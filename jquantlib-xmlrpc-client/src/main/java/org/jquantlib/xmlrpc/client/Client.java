package org.jquantlib.xmlrpc.client;

import java.net.URL;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.apache.xmlrpc.client.util.ClientFactory;
import org.jquantlib.xmlrpc.services.interfaces.CalendarWidget;

public class Client {

    public static void main(String[] args) throws Exception {
        // create configuration
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://localhost:8080/jquantlib-xmlrpc/xmlrpc"));
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(60 * 1000);
        config.setReplyTimeout(60 * 1000);

        XmlRpcClient client = new XmlRpcClient();

        // use Commons HttpClient as transport
        client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
        // set configuration
        client.setConfig(config);

        ClientFactory factory = new ClientFactory(client);
        CalendarWidget calendar = (CalendarWidget) factory.newInstance(CalendarWidget.class);
        
        int year=2005, month=11, day=13;
        
        // make proxied calls
        System.out.println("Weekday("+year+","+month+","+day+")       = "+ calendar.getWeekday(year, month, day));
        System.out.println("isBusinessDay("+year+","+month+","+day+") = "+ calendar.isBusinessDay(year, month, day));
    }
    
    
}
