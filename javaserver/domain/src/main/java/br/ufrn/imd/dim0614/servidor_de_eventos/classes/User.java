/**
 * 
 */
package br.ufrn.imd.dim0614.servidor_de_eventos.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Ailson Forte dos Santos
 *
 */
/**
 * @author jarvis
 *
 */
/**
 * @author jarvis
 *
 */
/**
 * @author jarvis
 *
 */
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private String userName;
	private String password;
	private List<String> interestTopics;
	private Boolean logged;
	private HashMap<Boolean, List<Event>> notifications;

	/**
	 * @param name
	 * @param userName
	 * @param interestTopics
	 * @param password
	 */
	public User(String name, String userName, List<String> interestTopics) {
		this.name = name;
		this.userName = userName;
		this.interestTopics = interestTopics;
		this.password = null;
		this.logged = false;
		this.notifications = new HashMap<Boolean, List<Event>>();
		
		this.notifications.put(true, new ArrayList<Event>());
		this.notifications.put(false, new ArrayList<Event>());
	}
	
	/**
	 * @param name
	 * @param userName
	 * @param interestTopics
	 * @param password
	 */
	public User(String name, String userName, List<String> interestTopics, String password) {
		this.name = name;
		this.userName = userName;
		this.interestTopics = interestTopics;
		this.password = password;
		this.logged = false;
		this.notifications = new HashMap<Boolean, List<Event>>();
		
		this.notifications.put(true, new ArrayList<Event>());
		this.notifications.put(false, new ArrayList<Event>());
	}

	public void login() {
		this.logged = true;
	}

	/**
	 * @param password
	 * @return true if login is successful
	 */
	public boolean login(String password) {
		if(password.equals(this.password)) {
			this.logged = true;
		}
		return this.logged;
	}
	
	public void logout() {
		this.logged = false;
	}
	
	public boolean isLogged() {
		return this.logged;
	}
	
	public void addNotification(Event event) {
		this.notifications.get(false).add(event);
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
	 * @return true if successful
	 */
	public boolean addInterestTopic(String topic) {
		return interestTopics.add(topic);
	}
	
	/**
	 * @return true if successful
	 */
	public boolean addInterestTopic(List<String> topics) {
		return interestTopics.addAll(topics);
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

	public HashMap<Boolean, List<Event>> getNotifications() {
		return notifications;
	}

	public void setNotifications(HashMap<Boolean, List<Event>> notifications) {
		this.notifications = notifications;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1 * ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}


}
