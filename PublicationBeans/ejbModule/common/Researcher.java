package common;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity(name = "Researcher")
@Table(name = "researcher")
public class Researcher implements Serializable {
	private static final long serialVersionUID = 1L;
	// we use this generation type to match that of SQLWriteStudents
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private long id;
	private String personName;
	private int reads;
	private int citations;
    
	
    @ManyToOne
    private Institution institution;
    @ManyToMany(targetEntity = Publication.class,fetch=FetchType.EAGER)
    private Set<Publication> publications = new HashSet<Publication>();
    @ManyToMany(targetEntity = Skill.class,fetch=FetchType.EAGER)
    private Set<Skill> skills = new HashSet<Skill>();
	
	
	
	public Researcher(){}
	
	public Researcher(String name, int reads, int citations)
	{
	
		this.personName = name;
		this.setReads(reads);
		this.setCitations(citations);
	}
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public String getPersonName() {
        return personName;
    }
	
    public void setPersonName(String value) {
        this.personName = value;
    }
	public int getCitations() {
		return citations;
	}

	public void setCitations(int citations) {
		this.citations = citations;
	}

	public int getReads() {
		return reads;
	}

	public void setReads(int reads) {
		this.reads = reads;
	}
	public 	Institution getInstitutionSet() {
	    return institution;
	}
	public void setInstitutionSet(Institution Institution) {
	    this.institution = Institution;
	 }
	
	public Set<Publication> getResearchSet() {
	    return publications;
	}
	public void addPublication(Publication pub) {
		this.publications.add(pub);
	}
	public void setPublicationSet(Set<Publication> PublicationSet) {
	    this.publications = PublicationSet;
	 }
	

	public Set<Skill> getSkillSet() {
	    return skills;
	}
	public void addSkill(Skill skill) {
		this.skills.add(skill);
	}
	public void setSkillSet(Set<Skill> SkillSet) {
	    this.skills = SkillSet;
	 }
	
    @Override
	public String toString()
	{
		return "Researcher " + id + ": " + personName + "\n";
	}


}
