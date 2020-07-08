package com.saidinit.random.amdocs.excersice3.domains;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class UserDomain {

	@Id
	// I was going to put Long and some autogenerated id, but most places nowadays
	// use alphanumeric for their ids anyways...
	private String id;

	@Column(name = "some_fields_here")
	private String someFieldsHere;

	@Column(name = "user_pic")
	private byte[] userPic;

}
