package classes;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class Sender {
	private ConnectionFactory connectionFactory;
	private Destination destination;
	private final int timeout;
	public Sender() throws NamingException{
		this.connectionFactory = InitialContext.doLookup("jms/RemoteConnectionFactory");
		this.destination = InitialContext.doLookup("jms/queue/playQueue");
		this.timeout=2000;

		
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
		this.timeout=2000;
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
			msg.setText("getPublications");
			messageProducer.send(destination,msg);
			
			JMSConsumer cons = context.createConsumer(tmp);
			ObjectMessage objmsg = (ObjectMessage) cons.receive(timeout);
			aux= (List<Publication>) objmsg.getObject();
			System.out.println("I received the reply sent to the temporary queue: "  );
			
		}
		catch (Exception re){
			System.out.println("Nenhum admin ativo! Tente novamente mais tarde");
		}
		return aux;
	}
	public List<Publication> getPublicationByTitle(String title) {
		List<Publication> aux = null;
		try (JMSContext context = connectionFactory.createContext("Antonio", "Antoniomaria2");){
			JMSProducer messageProducer = context.createProducer();
			TextMessage msg = context.createTextMessage();
			Destination tmp = context.createTemporaryQueue();
			msg.setJMSReplyTo(tmp);
			msg.setText("getByTitle:"+title);
			messageProducer.send(destination,msg);
			
			JMSConsumer cons = context.createConsumer(tmp);
			ObjectMessage objmsg = (ObjectMessage) cons.receive(timeout);
			aux= (List<Publication>) objmsg.getObject();
			System.out.println("Mensagem recebida: "  );
			
		}
		catch (Exception re){
			System.out.println("Nenhum admin ativo! Tente novamente mais tarde");
		}
		return aux;
	}
	
	public List<AppUser> login(String user, String pass) {
		List<AppUser> aux = null;
		try (JMSContext context = connectionFactory.createContext("Antonio", "Antoniomaria2");){
			JMSProducer messageProducer = context.createProducer();
			TextMessage msg = context.createTextMessage();
			Destination tmp = context.createTemporaryQueue();
			msg.setJMSReplyTo(tmp);
			msg.setText("Login:"+user+":"+pass);
			messageProducer.send(destination,msg);
			
			JMSConsumer cons = context.createConsumer(tmp);
			ObjectMessage objmsg = (ObjectMessage) cons.receive(timeout);
			aux= (List<AppUser>) objmsg.getObject();
			System.out.println("Mensagem recebida: "  );
			
			
			
		}
		catch (Exception re){
			System.out.println("Nenhum admin ativo! Tente novamente mais tarde");
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
