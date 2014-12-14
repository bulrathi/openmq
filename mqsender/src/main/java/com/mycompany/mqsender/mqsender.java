package com.mycompany.mqsender;

import com.sun.messaging.ConnectionConfiguration;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * @author bw
 */
public class mqsender {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Connection conn = null;
        Session session = null;
            	    
        try {
            com.sun.messaging.ConnectionFactory cf = new com.sun.messaging.ConnectionFactory();
            cf.setProperty(com.sun.messaging.ConnectionConfiguration.imqAddressList, "mq://esb-test-01.msk.mts.ru:7676");
            cf.setProperty(ConnectionConfiguration.imqReconnectEnabled, "true");
            cf.setProperty(ConnectionConfiguration.imqReconnectAttempts, "5");
            cf.setProperty(ConnectionConfiguration.imqReconnectInterval, "500");
            cf.setProperty(ConnectionConfiguration.imqAddressListBehavior, "RANDOM");
            conn = cf.createConnection();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("NKIPOnlineSync");

            MessageProducer producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage();
            message.setText("TEST");
            System.out.println("Sending message: " + message.getText());
            producer.send(message);
        
        } catch (JMSException e) {
            System.out.println("Exception occurred: " + e.toString());
            System.exit(1);
            
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException ex) {
                    System.exit(1);
                }
            }
            
            if (conn != null) {
                try {
                    conn.close();
                } catch (JMSException e) {
                    System.exit(1);
                }
            }
        }
    }    
}
