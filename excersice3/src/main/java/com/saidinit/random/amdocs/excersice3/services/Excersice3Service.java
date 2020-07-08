package com.saidinit.random.amdocs.excersice3.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.saidinit.random.amdocs.excersice3.domains.UserDomain;
import com.saidinit.random.amdocs.excersice3.exceptions.UserInternalServerException;
import com.saidinit.random.amdocs.excersice3.exceptions.UserNotFoundException;
import com.saidinit.random.amdocs.excersice3.models.UserProfile;
import com.saidinit.random.amdocs.excersice3.repositories.UserRepository;

import lombok.Data;

@Service
@Data
public class Excersice3Service {

	private final UserRepository repo;

	public UserProfile updateUserProfilePicture(String id, MultipartFile file) {
		// TODO this throws NoSuchElementException, we need it to throw the correct
		// exception that we want
		UserDomain user = repo.findById(id).orElseThrow(UserNotFoundException::new);
		try {
			user.setUserPic(file.getBytes());
		} catch (IOException e) {
			throw new UserInternalServerException(e.getMessage());
		}

		return transform(repo.save(user));
	}

	private UserProfile transform(UserDomain ud) {
		// TODO Auto-generated method stub
		return UserProfile.builder()
				.id(ud.getId())
				.build();
	}

	public UserProfile requestInternetAccess(String id, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	public UserProfile cancelSubscription(String id, String type) {
		// TODO Auto-generated method stub
		return null;
	}

}
