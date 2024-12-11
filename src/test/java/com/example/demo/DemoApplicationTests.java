package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private WebApplicationContext context;
	private MockMvc mvc;


	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}

	@Test
	@WithMockUser(username = "admin", roles={"administrateur"})
	void recupererLesUtilisateurEnTantQueAdministrateur_reponse200oK() throws Exception{

		mvc.perform(get("/utilisateur"))
				.andExpect(status().isOk());
	}

	@Test
	void recupererLesUtilisateurEnTantQueAnonyme_reponse403() throws Exception{

		mvc.perform(get("/utilisateur"))
				.andExpect(status().isForbidden());

	}



}
