package com.homeki.core.actions;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.Session;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Action {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	public abstract void execute(Session ses);
}
