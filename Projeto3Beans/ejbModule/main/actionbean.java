package main;

import java.io.Serializable;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.JMSRuntimeException;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import classes.AppUser;
import classes.Publication;

/**
 * Session Bean implementation class actionbean
 */
@Stateless
@LocalBean
public class actionbean implements actionbeanRemote, actionbeanLocal {
	@PersistenceContext(name = "Loader")
	private EntityManager em;
	
	
    /**
     * Default constructor. 
     * @throws NamingException 
     */
    public actionbean() {
        // TODO Auto-generated constructor stub
    }
	
	
	/*
	public static void main(String[] args) throws NamingException, IOException{
		AsyncReceiver asyncReceiver = new AsyncReceiver();
		asyncReceiver.launch_and_wait();
	}*/
	
	//=========================GET DATABASE INFO=============================================================

				//GET ALL PUBS
				@Override
				public List<Publication> GetallPubs(){
					

				    // Define query String
				    String jpql = "SELECT r FROM Publication r";
				    // Create a (typed) query
				    TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
				    // Query and get result
				    List<Publication> mylist = typedQuery.getResultList();
				    return mylist;
				 }

				//GET PUBS BY NOME
				@Override
				public List<Publication> GetPublicationByNome(String nome){
					

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
				

				//LOGIN
				@Override
				public List<AppUser> login(String AppUsername, String password) {
					

					// Define query String
					String jpql = "SELECT u FROM AppUser u where u.username=:name AND u.password=:pass";
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

    
    


