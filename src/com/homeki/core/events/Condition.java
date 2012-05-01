package com.homeki.core.events;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.homeki.core.triggers.Trigger;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Condition {
	public static final int EQ = 0;
	public static final int LT = 1;
	public static final int GT = 2;
	
	@SuppressWarnings("unused")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToMany(mappedBy = "action", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
	protected Set<Trigger> triggers;
	
	public abstract boolean check(Event e);
}
