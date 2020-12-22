package main;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import classes.AppUser;
import classes.Publication;



/**
 * Message-Driven Bean implementation class for: mdb
 */
@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destination", propertyValue = "playQueue"), @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue")
		}, 
		mappedName = "playQueue")
public class mdb implements MessageListener {
	@EJB
	private actionbeanRemote testBean;
	
	@Inject
	private JMSContext context;

	/**
     * Default constructor. 
     */
    public mdb() {
        // TODO Auto-generated constructor stub
    	
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message msg) {
    	
        // TODO Auto-generated method stub
    	TextMessage textMsg = (TextMessage) msg;
		try{
				
				
				System.out.println("Got message: " + textMsg.getText());
				if(textMsg.getText().equals("getPublications")) {
					
					give_publications(textMsg);
				}else if(textMsg.getText().startsWith("getByTitle")) {
					give_publicationByTitle(textMsg);
				}else if(textMsg.getText().startsWith("Login")) {
					login(textMsg);
				}
			}
			catch (JMSException e){
				e.printStackTrace();
			}	
		
        
    }
    
    public void login( TextMessage msg) throws JMSException {
		String aux[];
		aux = msg.getText().split(":");
		
		List<AppUser> lista = testBean.login(aux[1],aux[2]);
		JMSProducer producer = context.createProducer();
		ObjectMessage objmessage = context.createObjectMessage();
		objmessage.setObject((Serializable) lista);
		producer.send( msg.getJMSReplyTo(), objmessage );
		
	}
    
    
    public void give_publications( TextMessage msg) throws JMSException {
			
			List<Publication> lista = testBean.GetallPubs();
			JMSProducer producer = context.createProducer();
			ObjectMessage objmessage = context.createObjectMessage();
			objmessage.setObject((Serializable) lista);
			producer.send( msg.getJMSReplyTo(), objmessage );
		
	}
    
    
    public void give_publicationByTitle( TextMessage msg) throws JMSException {
			String aux[];
			aux = msg.getText().split(":");
			
			List<Publication> lista = testBean.GetPublicationByNome(aux[1]);
			JMSProducer producer = context.createProducer();
			ObjectMessage objmessage = context.createObjectMessage();
			objmessage.setObject((Serializable) lista);
			producer.send( msg.getJMSReplyTo(), objmessage );
		
	}

}
