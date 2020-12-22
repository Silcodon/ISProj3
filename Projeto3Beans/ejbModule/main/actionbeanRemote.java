package main;

import java.util.List;

import javax.ejb.Remote;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import classes.AppUser;
import classes.Publication;

@Remote
public interface actionbeanRemote {
	
	public List<Publication> GetallPubs();
	public List<Publication> GetPublicationByNome(String name);
	List<AppUser> login(String AppUsername, String password);
	
}
