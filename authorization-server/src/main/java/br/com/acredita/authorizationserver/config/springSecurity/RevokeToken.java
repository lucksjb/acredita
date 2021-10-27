package br.com.acredita.authorizationserver.config.springSecurity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.acredita.authorizationserver.config.security.SegurancaProperties;
import br.com.acredita.authorizationserver.enums.SimNao;

@RestController
@RequestMapping("/tokens")
public class RevokeToken {
	@Autowired
	private SegurancaProperties properties;

	@PostMapping("/logout")
	public void logout(HttpServletRequest req, HttpServletResponse resp) {
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);

		cookie.setSecure(properties.getHabilitaHttps() == SimNao.SIM);
		cookie.setPath(req.getContextPath() + "/oauth/token");
		cookie.setMaxAge(0);

		resp.addCookie(cookie);
		resp.setStatus(HttpStatus.NO_CONTENT.value());
	}

}