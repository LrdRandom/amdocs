package com.saidinit.random.amdocs.excersice3.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.saidinit.random.amdocs.excersice3.models.UserProfile;
import com.saidinit.random.amdocs.excersice3.services.AuthenticationService;
import com.saidinit.random.amdocs.excersice3.services.Excersice3Service;

@RunWith(SpringRunner.class)
@WebMvcTest
public class Excersice3ControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthenticationService authService;

	@MockBean
	public Excersice3Service service;

	private ResponseEntity<String> authResponse = new ResponseEntity<>(" ", HttpStatus.OK);
	private MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
	private UserProfile someValue = UserProfile.builder().build();

	@Test
	public void insertUserPic_ok() throws Exception {
		when(authService.validateJwt(anyString(), anyString())).thenReturn(authResponse);
		when(service.updateUserProfilePicture(anyString(), any())).thenReturn(someValue);

		mockMvc.perform(MockMvcRequestBuilders.multipart("/amdocs/user/123456789/profile/picture")
				.file(file)
				.header("Authorization", "token"))
				.andExpect(status().is(202));

	}
	
	@Test
	public void insertUserPic_unauthorised() throws Exception{
		when(authService.validateJwt(anyString(), anyString())).thenReturn(new ResponseEntity<>(" ", HttpStatus.UNAUTHORIZED));
		mockMvc.perform(MockMvcRequestBuilders.multipart("/amdocs/user/123456789/profile/picture")
				.file(file)
				.header("Authorization", "token"))
				.andExpect(status().is(401));
	}
	
	@Test
	public void insertUserPic_forbidden() throws Exception{
		when(authService.validateJwt(anyString(), anyString())).thenReturn(new ResponseEntity<>(" ", HttpStatus.FORBIDDEN));
		mockMvc.perform(MockMvcRequestBuilders.multipart("/amdocs/user/123456789/profile/picture")
				.file(file)
				.header("Authorization", "token"))
				.andExpect(status().is(403));
	}
	
	@Test
	public void insertUserPic_validationError() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.multipart("/amdocs/user/abcd/profile/picture")
				.file(file)
				.header("Authorization", "token"))
				.andExpect(status().is(400));
	}
	
	/*
	 * @Ignore
	 * 
	 * @Test public void insertUserPic_emptyFile() throws Exception{
	 * mockMvc.perform(MockMvcRequestBuilders.multipart(
	 * "/amdocs/user/123456789/profile/picture") .file(null)
	 * .header("Authorization", "token")) .andExpect(status().is(500)); }
	 */
	
	@Test
	public void requestAccess_ok() throws Exception{
		when(authService.validateJwt(anyString(), anyString())).thenReturn(authResponse);
		when(service.requestInternetAccess(anyString(), any())).thenReturn(someValue);
		
		mockMvc.perform(put("/amdocs/user/123456789/internet-service/")
				.param("type", "someType")
				.header("Authorization", "token"))
				.andExpect(status().is(201));
	}
	
	@Test
	public void cancelSubscription_ok() throws Exception{
		when(authService.validateJwt(anyString(), anyString())).thenReturn(authResponse);
		when(service.cancelSubscription(anyString(), any())).thenReturn(someValue);
		
		mockMvc.perform(delete("/amdocs/user/123456789/internet-service/")
				.param("type", "someType")
				.header("Authorization", "token"))
				.andExpect(status().is(202));
	}
	

}
