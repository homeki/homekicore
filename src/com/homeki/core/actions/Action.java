package com.homeki.core.actions;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.Session;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Action {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "action_id")
	private Action action;
	
	public int getId() {
		return id;
	}
	
	public void setParent(Action action) {
		this.action = action;
	}
	
	public abstract void execute(Session ses);
	
	public abstract String getType();
}
