package main;

import java.util.List;

import javax.ejb.Local;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import classes.AppUser;
import classes.Publication;

@Local
public interface actionbeanLocal {

	public List<Publication> GetallPubs();
	public List<Publication> GetPublicationByNome(String name);
	List<AppUser> login(String AppUsername, String password);


}
