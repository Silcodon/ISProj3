package ejb;

import java.util.List;

import javax.ejb.Remote;

import common.Publication;

@Remote
public interface PublicationBeanRemote {
	
	List<Publication> Getall();
	
	List<Publication> GetPublicationByNome(String nome);

	List<Publication> GetPublicationByResearcher(String nome);
}
