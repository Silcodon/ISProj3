package ejb;

import java.util.List;

import javax.ejb.Local;

import common.Publication;

@Local
public interface PublicationBeanLocal {

	List<Publication> Getall();

	List<Publication> GetPublicationByNome(String nome);

	List<Publication> GetPublicationByResearcher(String nome);

}
