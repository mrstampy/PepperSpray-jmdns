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

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mrstampy.kitchensync.netty.channel.KiSyChannel;
import com.github.mrstampy.pprspray.core.discovery.PepperSprayDiscoveryService;
import com.github.mrstampy.pprspray.core.streamer.MediaStreamType;

// TODO: Auto-generated Javadoc
/**
 * The Class JmDNSPepperSprayDiscovery.
 */
public class JmDNSPepperSprayDiscovery implements PepperSprayDiscoveryService<ServiceInfo> {
	private static final Logger log = LoggerFactory.getLogger(JmDNSPepperSprayDiscovery.class);

	/** The Constant SERVICE_TYPE. */
	public static final String SERVICE_TYPE = "_pepperspray._udp.local.";

	/** The Constant DEFAULT_NAME. */
	public static final String DEFAULT_NAME = "PepperSpray-jmdns";

	private static JmDNS DNS;

	/** The Constant DISCOVERY. */
	public static final JmDNSPepperSprayDiscovery DISCOVERY = new JmDNSPepperSprayDiscovery();

	static {
		try {
			initDns();
		} catch (Exception e) {
			log.error("Could not register/lookup service", e);
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.mrstampy.pprspray.core.discovery.PepperSprayDiscoveryService
	 * #createServiceName
	 * (com.github.mrstampy.pprspray.core.streamer.MediaStreamType)
	 */
	public String createServiceName(MediaStreamType type) {
		return DEFAULT_NAME + ":" + type.name();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.mrstampy.pprspray.core.discovery.PepperSprayDiscoveryService
	 * #registerPepperSprayService
	 * (com.github.mrstampy.kitchensync.netty.channel.KiSyChannel)
	 */
	public boolean registerPepperSprayService(KiSyChannel channel) {
		return registerPepperSprayService(channel, DEFAULT_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.mrstampy.pprspray.core.discovery.PepperSprayDiscoveryService
	 * #registerPepperSprayServices
	 * (com.github.mrstampy.kitchensync.netty.channel.KiSyChannel,
	 * com.github.mrstampy.pprspray.core.streamer.MediaStreamType[])
	 */
	public boolean registerPepperSprayServices(KiSyChannel channel, MediaStreamType... types) {
		for (MediaStreamType type : types) {
			if (!registerPepperSprayService(channel, createServiceName(type))) return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.mrstampy.pprspray.core.discovery.PepperSprayDiscoveryService
	 * #registerPepperSprayService
	 * (com.github.mrstampy.kitchensync.netty.channel.KiSyChannel,
	 * java.lang.String)
	 */
	public boolean registerPepperSprayService(KiSyChannel channel, String name) {
		try {
			name = isEmpty(name) ? DEFAULT_NAME : name;
			DNS.registerService(createServiceInfo(channel, name));
			return true;
		} catch (IOException e) {
			log.error("Could not create PepperSpray service {} on port {}", name, channel.getPort(), e);
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.mrstampy.pprspray.core.discovery.PepperSprayDiscoveryService
	 * #unregisterPepperSprayService
	 * (com.github.mrstampy.kitchensync.netty.channel.KiSyChannel)
	 */
	public boolean unregisterPepperSprayService(KiSyChannel channel) {
		return unregisterPepperSprayService(channel, DEFAULT_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.mrstampy.pprspray.core.discovery.PepperSprayDiscoveryService
	 * #unregisterPepperSprayServices
	 * (com.github.mrstampy.kitchensync.netty.channel.KiSyChannel,
	 * com.github.mrstampy.pprspray.core.streamer.MediaStreamType[])
	 */
	public boolean unregisterPepperSprayServices(KiSyChannel channel, MediaStreamType... types) {
		for (MediaStreamType type : types) {
			unregisterPepperSprayService(channel, createServiceName(type));
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.mrstampy.pprspray.core.discovery.PepperSprayDiscoveryService
	 * #unregisterPepperSprayService
	 * (com.github.mrstampy.kitchensync.netty.channel.KiSyChannel,
	 * java.lang.String)
	 */
	public boolean unregisterPepperSprayService(KiSyChannel channel, String name) {
		try {
			name = isEmpty(name) ? DEFAULT_NAME : name;
			DNS.unregisterService(createServiceInfo(channel, name));
			return true;
		} catch (Exception e) {
			log.error("Could not unregister PepperSpray service {} on port {}", name, channel.getPort(), e);
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.mrstampy.pprspray.core.discovery.PepperSprayDiscoveryService
	 * #getRegisteredPepperSprayServices()
	 */
	public List<ServiceInfo> getRegisteredPepperSprayServices() {
		return Arrays.asList(DNS.list(SERVICE_TYPE));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.mrstampy.pprspray.core.discovery.PepperSprayDiscoveryService
	 * #getRegisteredPepperSprayServices(java.net.InetAddress)
	 */
	public List<ServiceInfo> getRegisteredPepperSprayServices(InetAddress address) {
		//@formatter:off
		return getRegisteredPepperSprayServices()
				.stream()
				.filter(t -> containsAddress(t.getInetAddresses(), address))
				.collect(Collectors.toList());
		//@formatter:on
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.mrstampy.pprspray.core.discovery.PepperSprayDiscoveryService
	 * #getRegisteredPepperSprayServices(java.lang.String)
	 */
	@Override
	public List<ServiceInfo> getRegisteredPepperSprayServices(String identifier) {
		try {
			return getRegisteredPepperSprayServices(InetAddress.getByName(identifier));
		} catch (UnknownHostException e) {
			log.error("Unexpected exception", e);
		}

		return Collections.emptyList();
	}

	private boolean containsAddress(InetAddress[] inetAddresses, InetAddress address) {
		//@formatter:off
		return Arrays
				.asList(inetAddresses)
				.stream()
				.anyMatch(t -> t.equals(address));
		//@formatter:on
	}

	private static void initDns() throws IOException {
		DNS = JmDNS.create(SERVICE_TYPE);

		DNS.addServiceListener(SERVICE_TYPE, createServiceListener());

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					post(JmDNSDiscoveryEventType.SHUTDOWN, null);

					DNS.close();
				} catch (IOException e) {
					log.error("Unexpected exception", e);
				}
			}
		});
	}

	private static ServiceInfo createServiceInfo(KiSyChannel channel, String name) {
		return ServiceInfo.create(SERVICE_TYPE, name, channel.getPort(), name);
	}

	private static void post(JmDNSDiscoveryEventType type, ServiceEvent event) {
		JmDNSDiscoveryEventBus.post(new JmDNSDiscoveryEvent(type, event));
	}

	private static ServiceListener createServiceListener() {
		return new ServiceListener() {

			@Override
			public void serviceResolved(ServiceEvent event) {
				log.debug("Resolved: {}", event.getInfo());
				post(JmDNSDiscoveryEventType.RESOLVED, event);
			}

			@Override
			public void serviceRemoved(ServiceEvent event) {
				log.debug("Removed: {}", event.getInfo());
				post(JmDNSDiscoveryEventType.REMOVED, event);
			}

			@Override
			public void serviceAdded(ServiceEvent event) {
				log.debug("Added: {}", event.getInfo());

				DNS.requestServiceInfo(SERVICE_TYPE, event.getName());
				post(JmDNSDiscoveryEventType.ADDED, event);
			}
		};
	}

	private JmDNSPepperSprayDiscovery() {
	}

}
