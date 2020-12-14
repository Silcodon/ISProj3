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
import javax.persistence.Query;
import javax.persistence.TypedQuery;


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
	
	public void give_publications( TextMessage msg) throws JMSException {
		try (JMSContext context = connectionFactory.createContext("Antonio", "Antoniomaria2");){
			
			List<Publication> lista = GetallPubs();
			JMSProducer producer = context.createProducer();
			ObjectMessage objmessage = context.createObjectMessage();
			objmessage.setObject((Serializable) lista);
			producer.send( msg.getJMSReplyTo(), objmessage );
			
		}
		catch (JMSRuntimeException e){
			e.printStackTrace();
		}
	}
	public void give_publicationByTitle( TextMessage msg) throws JMSException {
		try (JMSContext context = connectionFactory.createContext("Antonio", "Antoniomaria2");){
			String aux[];
			aux = msg.getText().split(":");
			
			List<Publication> lista = GetPublicationByNome(aux[1]);
			JMSProducer producer = context.createProducer();
			ObjectMessage objmessage = context.createObjectMessage();
			objmessage.setObject((Serializable) lista);
			producer.send( msg.getJMSReplyTo(), objmessage );
			
		}
		catch (JMSRuntimeException e){
			e.printStackTrace();
		}
	}
	public void login( TextMessage msg) throws JMSException {
		try (JMSContext context = connectionFactory.createContext("Antonio", "Antoniomaria2");){
			String aux[];
			aux = msg.getText().split(":");
			
			List<AppUser> lista = login(aux[1],aux[2]);
			JMSProducer producer = context.createProducer();
			ObjectMessage objmessage = context.createObjectMessage();
			objmessage.setObject((Serializable) lista);
			producer.send( msg.getJMSReplyTo(), objmessage );
			
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
	
	//=========================GET DATABASE INFO=============================================================





				//GET ALL AppUserS
				public static List<AppUser> GetallAppUsers(){
					EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
					EntityManager em = emfactory.createEntityManager( );
				    // Define query String
				    String jpql = "SELECT u FROM AppUser u";
				    // Create a (typed) query
				    TypedQuery<AppUser> typedQuery = em.createQuery(jpql, AppUser.class);
				    // Query and get result
				    List<AppUser> mylist = typedQuery.getResultList();
				    return mylist;
				 }

				//GET ALL PUBS
				public static List<Publication> GetallPubs(){
					EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
					EntityManager em = emfactory.createEntityManager( );
				    // Define query String
				    String jpql = "SELECT r FROM Publication r";
				    // Create a (typed) query
				    TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
				    // Query and get result
				    List<Publication> mylist = typedQuery.getResultList();
				    return mylist;
				 }

				//GET PUBS BY NOME
				public List<Publication> GetPublicationByNome(String nome){
					EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
					EntityManager em = emfactory.createEntityManager( );
					// Define query String
					String jpql = "SELECT r FROM Publication r where r.name=:name";
					// Create a (typed) query
					TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
					// Set parameter
					typedQuery.setParameter("name", nome);
					// Query and get result
					List<Publication> mylist = typedQuery.getResultList();
					return mylist;
				}

				//ADD AppUser
				public static void AddAppUser(AppUser st) {
					EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
					EntityManager entitymanager = emfactory.createEntityManager( );
					entitymanager.getTransaction( ).begin( );
					List<AppUser> mylist = GetallAppUsers();
					if (mylist.size()==0) {
						st.setAdmin(true);
					}
					entitymanager.persist(st);
					entitymanager.getTransaction( ).commit( );
					entitymanager.close( );
					emfactory.close( );
				}

				//ADD PUBLICATION
				public static void AddPublication(Publication st) {
					EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
					EntityManager entitymanager = emfactory.createEntityManager( );
					entitymanager.getTransaction( ).begin( );
					entitymanager.persist(st);
					entitymanager.getTransaction( ).commit( );
					entitymanager.close( );
					emfactory.close( );
				}

				//UPDATE PUBLICATION
				public static void UpdatePublication(Publication st, String old) {
					EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
					EntityManager em = emfactory.createEntityManager( );
				    em.getTransaction().begin();
				    if(st.getName().compareTo("")!=0) {
				    	Query query = em.createQuery("UPDATE Publication p SET p.name =: nome"
					            + "WHERE p.name = :oldname");
				    	query.setParameter("nome", st.getName());
					    query.setParameter("oldname", old);
					    query.executeUpdate();
				    }
				    if(st.getType().compareTo("")!=0) {
				    	Query query = em.createQuery("UPDATE Publication p SET p.type =: tipo "
					            + "WHERE p.name = :oldname");
				    	query.setParameter("tipo", st.getType());
					    query.setParameter("oldname", old);
					    query.executeUpdate();
				    }
				    if(st.getDate().compareTo("")!=0) {
				    	Query query = em.createQuery("UPDATE Publication p SET p.date =: data "
					            + "WHERE p.name = :oldname");
				    	query.setParameter("data", st.getDate());
					    query.setParameter("oldname", old);
					    query.executeUpdate();
				    }
				    em.getTransaction().commit();
				    em.close();
				}

				//REMOVE A PUBLICATION
				public static void RemovePublication(String pubname) {
					EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
					EntityManager em = emfactory.createEntityManager( );
					em.getTransaction().begin();
				    Query query = em.createQuery("DELETE FROM Publication p WHERE p.name = :Name ");
				    query.setParameter("Name", pubname);
				    query.executeUpdate();
				    em.getTransaction().commit();
				    em.close();
				}

				//ACTIVATE OR DEACTIVATE A AppUser
				public void ActivateAppUser(AppUser st) {
					EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
					EntityManager em = emfactory.createEntityManager( );
					em.getTransaction().begin();
					if(st.isActivated()==false) {
						Query query = em.createQuery("UPDATE AppUser u SET u.activated = TRUE "
					            + "WHERE u.AppUsername = :Name");
					    query.setParameter("Name", st.getUsername());
					    query.executeUpdate();
					}
					else {
						Query query = em.createQuery("UPDATE AppUser u SET u.activated = FALSE "
					            + "WHERE u.AppUsername = :Name");
					    query.setParameter("Name", st.getUsername());
					    query.executeUpdate();
					}

				    em.getTransaction().commit();
				    em.close();
				}

				//LOGIN
				public List<AppUser> login(String AppUsername, String password) {
					EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
					EntityManager em = emfactory.createEntityManager( );
					// Define query String
					String jpql = "SELECT u FROM AppUser u where u.AppUsername=:name AND u.password=:pass";
					// Create a (typed) query
					TypedQuery<AppUser> typedQuery = em.createQuery(jpql, AppUser.class);
					// Set parameter
					typedQuery.setParameter("name", AppUsername);
					typedQuery.setParameter("pass", password);
					// Query and get result
					List<AppUser> mylist = typedQuery.getResultList();
					return mylist;
					

				}


	
}
