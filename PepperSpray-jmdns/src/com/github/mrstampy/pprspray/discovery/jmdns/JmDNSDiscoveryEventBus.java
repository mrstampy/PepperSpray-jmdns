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

import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.AsyncEventBus;

// TODO: Auto-generated Javadoc
/**
 * The Class JmDNSDiscoveryEventBus.
 */
public class JmDNSDiscoveryEventBus {
	private static final Logger log = LoggerFactory.getLogger(JmDNSDiscoveryEventBus.class);

	private static final AsyncEventBus BUS = new AsyncEventBus("JmDNS Discovery Event Bus",
			Executors.newCachedThreadPool());

	/**
	 * Post.
	 *
	 * @param event
	 *          the event
	 */
	public static void post(JmDNSDiscoveryEvent event) {
		BUS.post(event);
	}

	/**
	 * Register.
	 *
	 * @param o
	 *          the o
	 */
	public static void register(Object o) {
		BUS.register(o);
	}

	/**
	 * Unregister.
	 *
	 * @param o
	 *          the o
	 */
	public static void unregister(Object o) {
		try {
			BUS.unregister(o);
		} catch (Exception e) {
			log.debug("Object not unregistered: {}", o, e);
		}
	}

	private JmDNSDiscoveryEventBus() {
	}

}
