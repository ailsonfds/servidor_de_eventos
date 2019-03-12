/**
 * 
 */
package br.ufrn.imd.dim0614.servidor_de_eventos.classes;

import java.io.Serializable;
import java.util.List;

/**
 * @author Ailson Forte dos Santos
 *
 */
public class Event implements Serializable {

	private static final long serialVersionUID = -5385942054644559040L;
	//	private static final long serialVersionUID = 1L;
	private String name;
	private List<String> topics;
	private String description;
	
	/**
	 * @param name
	 * @param interestTopics
	 * @param description
	 */
	public Event(String name, List<String> topics, String description) {
		this.name = name;
		this.topics = topics;
		this.description = description;
	}

	/**
	 * @return true if successful
	 */
	public boolean addTopics(String topic) {
		return topics.add(topic);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the interestTopics
	 */
	public List<String> getTopics() {
		return topics;
	}

	/**
	 * @param interestTopics the interestTopics to set
	 */
	public void setTopics(List<String> interestTopics) {
		this.topics = interestTopics;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1 * ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Event [name=" + name + ", topics=" + topics + ", description=" + description + "]";
	}
}
