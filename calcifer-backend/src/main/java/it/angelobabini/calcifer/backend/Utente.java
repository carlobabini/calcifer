package it.angelobabini.calcifer.backend;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.codec.digest.DigestUtils;

@Entity(name = "utenti")
public class Utente implements Serializable {
	private static final long serialVersionUID = -1848559949425066508L;
	
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_USER = "USER";

	@Id
	@Column(name = "username", length = 20, nullable = false)
	private String username;

	@Column(name = "password", length = 250, nullable = false)
	private String password;

	@Column(name = "name", length = 50, nullable = false)
	private String name;
	
	@Column(name = "email", length = 50, nullable = false)
	private String email;

	@Column(name = "role", length = 10, nullable = false)
	private String role;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void forcePassword(String password) {
		this.password = Utente.encodePassword(password);
	}
	
	public static String encodePassword(String password) {
		return DigestUtils.sha256Hex(password);
	}
}
