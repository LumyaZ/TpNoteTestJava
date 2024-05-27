package com.example.tpnotetest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@NotBlank(message =" Le user doit avoir un nom")
	private String name;
	
	@NotBlank(message = "L'email ne peut être vide")
    @Email(message = "Un email valide" )
    @Pattern(regexp = ".*\\.com$", message = "L'email doit se terminer par .com")
	private String email;
	
	@NotBlank(message ="Le user doit avoir un password")
    @Size(min = 3,message = "Le mot de passe doit être plus grand que 3")
	private String password ;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User(Long id, @NotBlank(message = " Le user doit avoir un nom") String name,
			@NotBlank(message = " Le user doit avoir un email") String email,
			@NotBlank(message = "Le user doit avoir un password") @Size(min = 3, message = "Le mot de passe doit être plus grand que 3") String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public User() {
		super();
	}
	
	
}
