package com.homework.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.homework.model.view.Views;


@Entity
@Table(name = "persons")
@SecondaryTable(name = "salary", 
	pkJoinColumns = { @PrimaryKeyJoinColumn(name = "person_id", referencedColumnName = "id") })
public class EPerson extends EntityBase {
	
	@Id
	@JsonView(Views.Public.class)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;	
	
	@JsonView(Views.Public.class)
	@Column(name = "name", nullable = false)
	private String name;
	
	@JsonView(Views.Public.class)
    @Column(name="amount", table="salary")
	private Double amount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}	

}
