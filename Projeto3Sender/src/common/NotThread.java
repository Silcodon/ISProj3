package common;

import java.io.IOException;
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

public class NotThread extends Thread{
	
	String username = null;
	
	public NotThread(String user) {
		this.username = user;
	}
	public void run() {
		
		AsyncReceiver asyncReceiver = null;
		
		try {
			asyncReceiver = new AsyncReceiver("topic/playTopic");
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//while(true) {
			
			
				try {
					asyncReceiver.noti(this.username);
					
					
				
				
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//asyncReceiver.give_publications(GetallPubs());
			
		//}
	}
	


}
