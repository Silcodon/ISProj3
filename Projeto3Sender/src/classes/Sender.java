package classes;

import java.util.List;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Sender {
	private ConnectionFactory connectionFactory;
	private Destination destination;
	public Sender() throws NamingException{
		this.connectionFactory = InitialContext.doLookup("jms/RemoteConnectionFactory");
		this.destination = InitialContext.doLookup("jms/queue/playQueue");

		
	}
	/*
	 * QUEUES 
	 * 
	ExpiryQueue
    DLQ
    RegistQueue
    UpdateQueue
    AddQueue
    RemoveQueue
    playQueue
    
	 * 
	 * TOPIC
	 * 
    playTopic

	 */
	
	public Sender(String destinationQueue) throws NamingException{
		this.connectionFactory = InitialContext.doLookup("jms/RemoteConnectionFactory");
		this.destination = InitialContext.doLookup("jms/queue/" + destinationQueue);
	}
	
	
	public void send(String text){
		try (JMSContext context = connectionFactory.createContext("Antonio", "Antoniomaria2");){
				JMSProducer messageProducer = context.createProducer();
				messageProducer.send(destination, text);
		}
		catch (Exception re){
			re.printStackTrace();
		}
	}
	
	
	public void send_and_reply(String text) {
		try (JMSContext context = connectionFactory.createContext("Antonio", "Antoniomaria2");){
			JMSProducer messageProducer = context.createProducer();
			TextMessage msg = context.createTextMessage();
			Destination tmp = context.createTemporaryQueue();
			msg.setJMSReplyTo(tmp);
			msg.setText(text);
			messageProducer.send(destination, msg);
			JMSConsumer cons = context.createConsumer(tmp);
			String str = cons.receiveBody(String.class);
			System.out.println("I received the reply sent to the temporary queue: " + str);
		}
		catch (Exception re){
			re.printStackTrace();
		}
		
	}
	
	public List<Publication> getPublications() {
		List<Publication> aux = null;
		try (JMSContext context = connectionFactory.createContext("Antonio", "Antoniomaria2");){
			JMSProducer messageProducer = context.createProducer();
			TextMessage msg = context.createTextMessage();
			Destination tmp = context.createTemporaryQueue();
			msg.setJMSReplyTo(tmp);
			msg.setText("getPublication");
			messageProducer.send(destination,msg);
			
			JMSConsumer cons = context.createConsumer(tmp);
			ObjectMessage objmsg = (ObjectMessage) cons.receive();
			aux= (List<Publication>) objmsg.getObject();
			//System.out.println("I received the reply sent to the temporary queue: "  );
			
		}
		catch (Exception re){
			re.printStackTrace();
		}
		return aux;
	}
	
	/*
	 public static void main(String[] args) throws NamingException{
		Sender sender = new Sender();
		sender.send("Hello Receiver!");
		System.out.println("Finished sender...");
	}
	 */
}
