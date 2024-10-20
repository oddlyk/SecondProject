package com.kdigital.SecondProject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
	 * @return true/false
	 */
	public boolean join(UserDTO userDTO) {
		// 가입하려는 id가 이미 사용중인지 확인 : 사용중인 아이디이면 true를 받아오기에 가입 실패 return
		boolean isExistUser = userRepository.existsById(userDTO.getUserId());
		log.info("id 사용중 여부: {}",isExistUser);
		if(isExistUser) return false;		// 이미 사용중인 아이디이므로 가입 실패
		
		// 비밀번호 암호화
		userDTO.setUserPwd(bCryptPasswordEncoder.encode(userDTO.getUserPwd()));
		
		UserEntity userEntity = UserEntity.toEntity(userDTO);
		log.info("새로 가입한 사람의 정보: {}",userEntity.toString());
		UserEntity temp = userRepository.save(userEntity);  	// 가입 성공
		if(temp!=null) {
			log.info("가입 성공!");
			return true;
		}
		else {
			log.info("가입 실패!");
			return false;
		}
		
	}
	
	/**
	 * useId에 해당하는 사용자 존재 여부 확인
	 * 
	 * @param userId
	 * @return true/false
	 */
	public boolean existId(String userId) {
		log.info("검색할 아이디 : {}",userId);
		boolean isExistUser = userRepository.existsById(userId);
		log.info("id 사용중 여부: {}",isExistUser);
		return isExistUser;
	}
	
	/**
	 * 로그인 처리
	 * @param userId
	 * @param userPwd
	 * @return
	 */
	
	public boolean login(String userId, String userPwd) {
	    log.info("로그인 시도 - userId: {}", userId);
	    Optional<UserEntity> userEntity = userRepository.findById(userId);
	    if (userEntity.isEmpty()) {
	        log.info("로그인 실패 - 아이디 없음");
	        return false;
	    } else {
	        UserEntity user = userEntity.get();
	        if (bCryptPasswordEncoder.matches(userPwd, user.getUserPwd())) {
	            log.info("로그인 성공");
	            return true;
	        } else {
	            log.info("로그인 실패 - 비밀번호 불일치");
	            return false;
	        }
	    }
	}
	
	/**
	 * 특정 아이디의 회원 반환
	 * */
	public UserDTO selectOne(String userId) {
		UserEntity userEntity = userRepository.findById(userId)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));
		
		return UserDTO.toDTO(userEntity);
		
	}
	
	/**
	 * 이메일 고유 여부
	 * @return 사용중 true / 미사용 false
	 * */
	public boolean existEmail(String email) {
		log.info("이메일 고유 여부 확인: {}",email);
		Optional<UserEntity> userEntity = userRepository.findByEmail(email);
		if(userEntity.isPresent()) {
			log.info("존재하는 아이디 입니다.");
			return true;
		}
		return false;
	}
}
