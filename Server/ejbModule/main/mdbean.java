package main;

import java.io.Serializable;
import java.util.List;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.JMSRuntimeException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import classes.AppUser;
import classes.Publication;

/**
 * Message-Driven Bean implementation class for: mdbean
 */

@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destination", propertyValue = "playQueue"), @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue")
		}, 
		mappedName = "playQueue")
public class mdbean implements MessageListener {

    /**
     * Default constructor. 
     */
    public mdbean() {
    	
        
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
	public void onMessage(Message msg){
		TextMessage textMsg = (TextMessage) msg;
		try{
			
			
			System.out.println("Got message: " + textMsg.getText());
			if(textMsg.getText().equals("getPublications")) {
				
				//give_publications(textMsg);
			}else if(textMsg.getText().startsWith("getByTitle")) {
			//	give_publicationByTitle(textMsg);
			}else if(textMsg.getText().startsWith("Login")) {
				//login(textMsg);
			}
		}
		catch (JMSException e){
			e.printStackTrace();
		}
	}
	
	

}
