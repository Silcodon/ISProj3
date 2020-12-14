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

public class ActionThread extends Thread{
	public void run() {
		
		AsyncReceiver asyncReceiver = null;
		try {
			asyncReceiver = new AsyncReceiver();
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//while(true) {
			
			
				try {
					asyncReceiver.launch_and_wait();
				
					
				
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//asyncReceiver.give_publications(GetallPubs());
			
		//}
	}
	


}
