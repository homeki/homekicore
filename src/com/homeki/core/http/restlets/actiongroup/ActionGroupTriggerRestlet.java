package com.homeki.core.http.restlets.actiongroup;

import org.hibernate.Session;

import com.homeki.core.actions.ActionGroup;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.logging.L;
import com.homeki.core.storage.Hibernate;

public class ActionGroupTriggerRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		final int actionGroupId = getInt(c, "actiongroupid");
		
		ActionGroup actgrp = (ActionGroup)c.ses.get(ActionGroup.class, actionGroupId);
		
		if (actgrp == null || !actgrp.isExplicit())
			throw new ApiException("No action group with the specified ID found.");
		
		new Thread() {
			public void run() {
				setName("ManualTriggerThread");
				L.i("Manually triggering action group in spawned thread...");
				Session ses = Hibernate.openSession();
				try { 
					ActionGroup actgrp = (ActionGroup)ses.get(ActionGroup.class, actionGroupId);
					actgrp.execute(ses);
				} catch (Exception e) {
					L.e("Error occured during manual trigger of action group.", e);
				} finally {
					Hibernate.closeSession(ses);
				}
				L.i("Manual trigger of action group in spawned thread completed.");
			};
		}.start();
		
		set200Response(c, msg("Action group executed successfully."));
	}
}
