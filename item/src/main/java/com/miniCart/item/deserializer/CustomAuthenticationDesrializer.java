package com.miniCart.item.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

@JsonComponent
public class CustomAuthenticationDesrializer extends JsonDeserializer<UsernamePasswordAuthenticationToken> {

	@Override
	public UsernamePasswordAuthenticationToken deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JacksonException {
		JsonNode node = jp.getCodec().readTree(jp);

		String name = (node.get("name")).asText();
		// Object principle = (node.get("principal")).asText();
		Object credentials = (node.get("credentials")).asText();
		boolean authenticated = (node.get("authenticated")).asBoolean();

		if (!authenticated) {
			throw new RuntimeException("Not authenticated");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();

		Iterator<JsonNode> elements = node.get("authorities").elements();
		while (elements.hasNext()) {
			JsonNode next = elements.next();
			JsonNode authority = next.get("authority");
			authorities.add(new SimpleGrantedAuthority(authority.asText()));
		}

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(name, credentials,
				authorities);
		return authToken;
	}

}
