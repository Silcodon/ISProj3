package ejb;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import common.Publication;

/**
 * Session Bean implementation class PublicationBean
 */
@Stateless
@LocalBean
public class PublicationBean implements PublicationBeanRemote, PublicationBeanLocal {

	@PersistenceContext(name = "Loader")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public PublicationBean() {
        // TODO Auto-generated constructor stub
    }
    
    
    @Override
    public List<Publication> Getall(){
    	// Define query String
    	String jpql = "SELECT r FROM Publication r";
    	// Create a (typed) query
    	TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
    	// Query and get result
    	List<Publication> mylist = typedQuery.getResultList();
    	return mylist;
    }
    
	
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
	
	@Override
    public List<Publication> GetPublicationByResearcher(String nome){
		// Define query String
		String jpql = "SELECT p FROM Publication p, Researcher r join r.publications rp where r.personName=:name and rp.id=p.id";
		// Create a (typed) query
		TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
		// Set parameter
		typedQuery.setParameter("name", nome);
		// Query and get result
		List<Publication> mylist = typedQuery.getResultList();
		return mylist;
    }

}
