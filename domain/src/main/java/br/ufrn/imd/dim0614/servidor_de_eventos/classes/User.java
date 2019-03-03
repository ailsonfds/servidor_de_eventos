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
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private String userName;
	private List<String> interestTopics;

	/**
	 * @param name
	 * @param userName
	 * @param interestTopics
	 */
	public User(String name, String userName, List<String> interestTopics) {
		this.name = name;
		this.userName = userName;
		this.interestTopics = interestTopics;
	}

	/**
	 * @param name
	 * @param userName
	 */
	public User(String name, String userName) {
		this.name = name;
		this.userName = userName;
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
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the interestTopics
	 */
	public List<String> getInterestTopics() {
		return interestTopics;
	}
	/**
	 * @param interestTopics the interestTopics to set
	 */
	public void setInterestTopics(List<String> interestTopics) {
		this.interestTopics = interestTopics;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		User other = (User) obj;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!name.equals(other.userName))
			return false;
		return true;
	}
	
}
