package com.homeki.core.actions;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.Session;

@Entity
public class TriggerActionGroupAction extends Action {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "action_group_id")
	private ActionGroup actionGroup;

	public TriggerActionGroupAction() {
		
	}
	
	public TriggerActionGroupAction(ActionGroup actionGroup) {
		this.actionGroup = actionGroup;
	}
	
	@Override
	public void execute(Session ses) {
		actionGroup.execute(ses);
	}

	@Override
	public String getType() {
		return "triggeractiongroup";
	}

	public void setActionGroup(ActionGroup actionGroup) {
		this.actionGroup = actionGroup;
	}
	
	public int getActionGroupId() {
		return actionGroup.getId();
	}
}
