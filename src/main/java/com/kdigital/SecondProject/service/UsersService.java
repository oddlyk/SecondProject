package com.kdigital.SecondProject.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kdigital.SecondProject.dto.UserDTO;
import com.kdigital.SecondProject.entity.UserEntity;
import com.kdigital.SecondProject.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {
	final UserRepository userRepository;
	final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	/**
	 * 전달 받은 userDTO를 userEntity로 변경한 후 DB save();
	 * @param userDTO
	 */
	public boolean join(UserDTO userDTO) {
		// 가입하려는 id가 이미 사용중인지 확인 : 사용중인 아이디이면 true
		boolean isExistUser = userRepository.existsById(userDTO.getUserId());
		if(isExistUser) return false;		// 이미 사용중인 아이디이므로 가입 실패
		
		// 비밀번호 암호화
		userDTO.setUserPwd(bCryptPasswordEncoder.encode(userDTO.getUserPwd()));
		
		UserEntity userEntity = UserEntity.toEntity(userDTO);
		userRepository.save(userEntity);  	// 가입 성공
		
		return true;
	}
	
	/**
	 * useId에 해당하는 사용자 존재 여부 확인
	 * 
	 * @param userId
	 * @return
	 */
	public boolean existId(String userId) {
		log.info("검색할 아이디 : {}",userId);
		List<UserEntity> result = userRepository.findByUserId(userId);
		UserDTO dto = UserDTO.toDTO(result.get(0));
		log.info("아이디 검색 결과: {}",dto);
		if (result.size()==1) { // userId가 존재하면 true
			return true;
		}
		else{
			return false;
		}
	}

}
