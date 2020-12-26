package classes;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;


public class AsyncReceiver implements MessageListener{
	private ConnectionFactory connectionFactory;
	private Destination destination;
	private List<Destination> list_dest;
	
	
	public AsyncReceiver() throws NamingException{
		this.connectionFactory = InitialContext.doLookup("jms/RemoteConnectionFactory");
		this.destination = InitialContext.doLookup("jms/queue/playQueue");
		this.list_dest = new ArrayList<Destination>();
	}
	
	public AsyncReceiver(String destinationQueue) throws NamingException{
		this.connectionFactory = InitialContext.doLookup("jms/RemoteConnectionFactory");
		this.destination = InitialContext.doLookup("jms/"+destinationQueue);
		this.list_dest = new ArrayList<Destination>();
	}
	
	
	@Override
	public void onMessage(Message msg){
		TextMessage textMsg = (TextMessage) msg;
		try{
			
			
			System.out.println("\n Got message: " + textMsg.getText());
			/*
			if(textMsg.getText().equals("getPublications")) {
				
				give_publications(textMsg);
			}else if(textMsg.getText().startsWith("getByTitle")) {
				give_publicationByTitle(textMsg);
			}else if(textMsg.getText().startsWith("Login")) {
				login(textMsg);
			}*/
		}
		catch (JMSException e){
			e.printStackTrace();
		}
	}
	public void noti(String user) throws JMSException {
		try (JMSContext context = connectionFactory.createContext("Antonio", "Antoniomaria2");){
			context.setClientID(user);
			JMSConsumer mc = context.createDurableConsumer((Topic) destination, "Subscription='"+ user+"'");
			mc.setMessageListener(this);
			//System.out.println("Press enter to finish...");
			//System.in.read();
																																																																																																																while(true) {	
																																																																																																																							}
		}
		catch (JMSRuntimeException e){
			e.printStackTrace();
		}
	}
	
	public void launch_and_wait() throws IOException{
		try (JMSContext context = connectionFactory.createContext("Antonio", "Antoniomaria2");){
			JMSConsumer consumer = context.createConsumer(destination);
			consumer.setMessageListener(this);
			//System.out.println("Press enter to finish...");
			//System.in.read();
																																																																																																																while(true) {	
																																																																																																																							}
		}
		catch (JMSRuntimeException e){
			e.printStackTrace();
		}
	}
	
	
	public String receive() throws JMSException{
		String msg = null;
		try (JMSContext context = connectionFactory.createContext("Antonio", "Antoniomaria2");){
			JMSConsumer mc = context.createConsumer(destination);
			TextMessage tmsg = (TextMessage) mc.receive(2000);
		
			Destination lixo = this.destination;
			if(tmsg!=null) {
				msg = tmsg.getText();
				System.out.println("Message received:" + msg);
				if(tmsg.getJMSReplyTo()!=null) {
					this.list_dest.add(tmsg.getJMSReplyTo());
				}else {
					this.list_dest.add(lixo);
				}
				
			}
			
		}
		catch (JMSRuntimeException re){
			re.printStackTrace();
		}
		return msg;
	}
	
	public Destination get_dest(int i) {
		return this.list_dest.get(i);
	}
	
	public void remove_dest(int i) {
		this.list_dest.remove(i);
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
	
	
	
	//POR COMPLETAR
	public void Listen_Notifications(long id) throws NamingException {
		try (JMSContext context = connectionFactory.createContext("Antonio", "Antoniomaria2");){
			context.setClientID(String.valueOf(id));
			JMSConsumer mc = context.createDurableConsumer((Topic) InitialContext.doLookup("jms/topic/playTopic"), "mySubscription");
			//mc.setMessageListener(this);
			//System.out.println("Press enter to finish...");
			//System.in.read();
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
