package common;

import java.util.List;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import classes.AsyncReceiver;
import classes.Publication;

public class ActionThread extends Thread{
	public void run() {
		
		AsyncReceiver asyncReceiver = null;
		try {
			asyncReceiver = new AsyncReceiver();
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true) {

			try {
				//asyncReceiver.receive_and_reply();
				asyncReceiver.give_publications(GetallPubs());
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	
	
	
	
//=========================GET DATABASE INFO=============================================================

	
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

}
