package br.com.acredita.applicationlog.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.acredita.applicationlog.config.RestControllerPath;

@RestController
public class HostCheckController {

	@GetMapping(RestControllerPath.HostCheck_PATH)
	public String checkHost() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress()
				+ " - " + InetAddress.getLocalHost().getHostName();
	}
	
}