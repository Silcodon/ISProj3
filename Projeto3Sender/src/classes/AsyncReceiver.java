package classes;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class AsyncReceiver implements MessageListener{
	private ConnectionFactory connectionFactory;
	private Destination destination;
	
	
	public AsyncReceiver() throws NamingException{
		this.connectionFactory = InitialContext.doLookup("jms/RemoteConnectionFactory");
		this.destination = InitialContext.doLookup("jms/queue/playQueue");
	}
	
	public AsyncReceiver(String destinationQueue) throws NamingException{
		this.connectionFactory = InitialContext.doLookup("jms/RemoteConnectionFactory");
		this.destination = InitialContext.doLookup("jms/queue/"+destinationQueue);
	}
	
	
	@Override
	public void onMessage(Message msg){
		TextMessage textMsg = (TextMessage) msg;
		try{
			System.out.println("Got message: " + textMsg.getText());
		}
		catch (JMSException e){
			e.printStackTrace();
		}
	}
	
	
	public void launch_and_wait() throws IOException{
		try (JMSContext context = connectionFactory.createContext("Antonio", "Antoniomaria2");){
			JMSConsumer consumer = context.createConsumer(destination);
			consumer.setMessageListener(this);
			//System.out.println("Press enter to finish...");
			//System.in.read();
		}
		catch (JMSRuntimeException e){
			e.printStackTrace();
		}
	}
	
	public String receive(){
		String msg = null;
		try (JMSContext context = connectionFactory.createContext("Antonio", "Antoniomaria2");){
			JMSConsumer mc = context.createConsumer(destination);
			msg = mc.receiveBody(String.class);
		}
		catch (JMSRuntimeException re){
			re.printStackTrace();
		}
		return msg;
	}
	
	public void receive_and_reply() throws JMSException {
		try (JMSContext context = connectionFactory.createContext("Antonio", "Antoniomaria2");){
			JMSConsumer consumer = context.createConsumer(destination);
			TextMessage msg = (TextMessage) consumer.receive();
			System.out.println("Message received:" + msg.getText());
			
			JMSProducer producer = context.createProducer();
			TextMessage reply = context.createTextMessage();
			reply.setText("Goodbye!");
			producer.send(msg.getJMSReplyTo(), reply);
			System.out.println("Sent reply to " + msg.getJMSReplyTo());
		}
		catch (JMSRuntimeException e){
			e.printStackTrace();
		}
	}
	
	public void give_publications(List<Publication> lista) throws JMSException {
		try (JMSContext context = connectionFactory.createContext("Antonio", "Antoniomaria2");){
			JMSConsumer consumer = context.createConsumer(destination);
			//TextMessage msg = (TextMessage) consumer.receive();
			System.out.println("Sending publications to user");
			
			//enviar lista 
			JMSProducer producer = context.createProducer();
			ObjectMessage objmessage = context.createObjectMessage();
			objmessage.setObject((Serializable) lista);
			producer.send( destination, objmessage );
			
		}
		catch (JMSRuntimeException e){
			e.printStackTrace();
		}
	}
	
	
	
	
	
	/*
	public static void main(String[] args) throws NamingException, IOException{
		AsyncReceiver asyncReceiver = new AsyncReceiver();
		asyncReceiver.launch_and_wait();
	}*/
}
