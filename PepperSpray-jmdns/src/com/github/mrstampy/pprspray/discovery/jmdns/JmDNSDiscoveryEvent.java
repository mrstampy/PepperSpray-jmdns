/*
 * PepperSpray-jmdns - JmDNS discovery for PepperSpray-core
 * 
 * Copyright (C) 2014 Burton Alexander
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * 
 */
package com.github.mrstampy.pprspray.discovery.jmdns;

import javax.jmdns.ServiceEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class JmDNSDiscoveryEvent.
 */
public class JmDNSDiscoveryEvent {

	private JmDNSDiscoveryEventType type;
	private ServiceEvent event;

	/**
	 * The Constructor.
	 *
	 * @param type
	 *          the type
	 * @param event
	 *          the event
	 */
	public JmDNSDiscoveryEvent(JmDNSDiscoveryEventType type, ServiceEvent event) {
		this.type = type;
		this.event = event;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public JmDNSDiscoveryEventType getType() {
		return type;
	}

	/**
	 * Gets the event.
	 *
	 * @return the event
	 */
	public ServiceEvent getEvent() {
		return event;
	}

}
