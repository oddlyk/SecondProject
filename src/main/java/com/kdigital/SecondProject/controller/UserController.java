package com.kdigital.SecondProject.controller;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kdigital.SecondProject.dto.AISDTO;
import com.kdigital.SecondProject.dto.FavoriteVoyageDTO;
import com.kdigital.SecondProject.dto.UserDTO;
import com.kdigital.SecondProject.dto.VoyageDTO;
import com.kdigital.SecondProject.entity.PortEntity;
import com.kdigital.SecondProject.entity.ShipEntity;
import com.kdigital.SecondProject.entity.VoyageEntity;
import com.kdigital.SecondProject.service.AISService;
import com.kdigital.SecondProject.service.FavoriteVoyageService;
import com.kdigital.SecondProject.service.UsersService;
import com.kdigital.SecondProject.service.VoyageService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

	final UsersService userService;
	final private VoyageService voyageService;
	final private FavoriteVoyageService favoriteVoyageService;
	final private AISService aisService;

	/**
	 * 로그인 실패 시 처리 화면
	 * @param error
	 * @param errMessage
	 * @param model
	 * @return
	 */
	@GetMapping("/login")
	public String login(
			@RequestParam(value="error", required=false) String error 
			, @RequestParam(value="errMessage", required=false) String errMessage 
			, Model model,
			HttpSession session
			) {
		System.out.println(error);
		System.out.println(errMessage);

		model.addAttribute("error", error);
		model.addAttribute("errMessage", errMessage);
		//기존 세션 확인 및 값 전달
		if(session.getAttributeNames().hasMoreElements()) {
			model.addAttribute("session_port",(String) session.getAttribute("session_port"));
			model.addAttribute("session_callsign",(String) session.getAttribute("session_callSign"));
		}
		return "pages/login";
	}

	
	@GetMapping("/mypage")
	public String mypage(Model model, Principal principal, HttpSession session) {
	    // 1. 로그인한 유저의 정보 가져오기
	    String userId = principal.getName();
	    UserDTO user = userService.selectOne(userId);  // userId 가져오기
	    
	    // 1-1. 유저의 userType 가져오기
	    String userType = "";
	    switch(user.getUserType()) {
	        case 1: userType = "선박 운영자";
	                break;
	        case 2: userType = "화물 소유자";
	                break;
	        case 3: userType = "무역업 종사자";
	                break;
	        case 4: userType = "일반 사용자";
	                break;
	    }
	    
	    // 2. 선호 선박으로 등록된 항해 정보 가져오기
	    List<FavoriteVoyageDTO> favoriteVoyages = favoriteVoyageService.findAll();
	    if (favoriteVoyages == null) {
	    	favoriteVoyages = new ArrayList<>();
	    }
	    
	    // TopFavorite 값 추가
	    List<Integer> favoriteList = new ArrayList<>();
	    for (FavoriteVoyageDTO favoriteVoyage : favoriteVoyages) {
	        int favorite = Integer.parseInt(favoriteVoyage.getTopFavorite());
	        favoriteList.add(favorite);
	    }
	    
	    // 3. 각 vNumber로 Voyage 및 ship 정보 조회
	    List<VoyageDTO> voyages = new ArrayList<>();
	    List<String> voyagePerList = new ArrayList<>();
	    for (FavoriteVoyageDTO favoriteVoyage : favoriteVoyages) {
	        VoyageEntity voyageEntity = favoriteVoyage.getVoyageEntity();
	        
	        if (voyageEntity == null) {
	            continue;
	        }
	        
	        // call-sign, 출발지, 도착지, 선박명 추출
	        String callSign = voyageEntity.getShip().getCallSign();
	        String departure = voyageEntity.getDeparture();
	        String shipName = voyageEntity.getShip().getShipName();
	        
	        Long vNumber = voyageEntity.getVNumber();
	        
	        // 운행률 계산
	        String voyagePer = getVoyagePer(vNumber);
	        voyagePerList.add(voyagePer);
	        
	        // VoyageDTO 생성 후 리스트에 추가
	        ShipEntity shipEntity = voyageEntity.getShip();
	        PortEntity portEntity = voyageEntity.getPort();
	        
	        VoyageDTO voyageDTO = new VoyageDTO();
	        voyageDTO.setVNumber(vNumber);
	        voyageDTO.setShip(shipEntity);
	        voyageDTO.setDeparture(departure);
	        
	        // PortEntity가 null이 아닌 경우에만 설정
	        if (portEntity != null) {
	            voyageDTO.setPort(portEntity);
	        } else {
	            log.warn("Voyage {} has no port information", voyageEntity.getVNumber());
	        }
	        
	        voyages.add(voyageDTO);
	    }
	    
	    // 모델에 목록 추가
	    model.addAttribute("userName", user.getUserName());
	    model.addAttribute("user", user.getUserId());
	    model.addAttribute("userType", userType);
	    model.addAttribute("voyages", voyages);
	    model.addAttribute("voyagePer", voyagePerList);
	    model.addAttribute("favoriteList", favoriteList);
	    
	    

		//기존 세션 확인 및 값 전달
		if(session.getAttributeNames().hasMoreElements()) {
			model.addAttribute("session_port",(String) session.getAttribute("session_port"));
			model.addAttribute("session_callsign",(String) session.getAttribute("session_callSign"));
		}
	    
	    return "pages/mypage";
	}

	
	private String getVoyagePer(Long vNumber) {
		VoyageDTO voyage = voyageService.selectOne(vNumber);
		LocalDateTime arrivalDate = voyage.getArrivalDate();
		LocalDateTime departureDate = voyage.getDepartureDate();
		AISDTO aisDTO = aisService.currentAISsignal(vNumber);
		if(aisDTO==null) {
			log.info("항해 시작 전");
			return String.format("%.2f", 0.0);
		}
		LocalDateTime  currentSignal = aisDTO.getSignalDate(); // aisDTO가 null이 아닐 때만 호출
		// 전체 항해 시간(출발일 ~ 도착일)을 분 단위로 계산
		long totalVoyageMinutes = Duration.between(departureDate, arrivalDate).toMinutes();
		// 현재까지의 항해 시간(출발일 ~ 현재 위치 시점)을 분 단위로 계산
		long untilTodayMinutes = Duration.between(departureDate, currentSignal).toMinutes(); 
		
		//현재 항해일 수 / 총 항해일 수 * 100 = 항해 진행률
		double voyageProgress = ((double) untilTodayMinutes / totalVoyageMinutes) * 100;
		if(voyageProgress==0) {
	         voyageProgress=100;
	      }
		log.info("현재 항해에 대한 항해 진행률: {}",voyageProgress);
		return String.format("%.2f", voyageProgress);
	}
	
	

	/**
	 * 즐겨찾기 변경 - 즐겨찾기 상태 조회와 변경
	 * @param vNumber
	 * @return
	 */

    @GetMapping("favorite/status")
    @ResponseBody
    public Map<String, Object> getFavoriteStatus(@RequestParam("vNumber") Long vNumber) {
        Map<String, Object> response = new HashMap<>();
        // 즐겨찾기 상태 확인
        boolean alreadyFavorite = favoriteVoyageService.isAlreadyFavorite(vNumber); // 특정 항해가 즐겨찾기로 등록되었는지 확인
        boolean hasTopFavorite = favoriteVoyageService.getTopFavoriteVoyage() != null; // 이미 다른 항해가 최상위로 등록되었는지 확인

        // 결과를 response에 추가
        response.put("alreadyFavorite", alreadyFavorite);
        response.put("hasTopFavorite", hasTopFavorite);

        return response;
    }
	
	@PostMapping("/favorite")
	@ResponseBody
	public Map<String, Object> changeFavorite(@RequestParam("vNumber") Long vNumber) {
		Map<String, Object> response = new HashMap<>();
		boolean success = favoriteVoyageService.changeTopFavorite(vNumber);
		response.put("success", success);
		return response;
	}
	
	
	@PostMapping("/deleteFavorite")
	public String deleteFavorite(@RequestParam("vNumbers") List<Long> vNumbers,
			HttpSession session,
			RedirectAttributes rttr //redirect는 model을 받을 수 없기에 redirect attribute가 필요
			) {

		for (Long vNumber : vNumbers) {
			favoriteVoyageService.deleteFev(vNumber);
			}
		
		//기존 세션 확인 및 값 전달
		if(session.getAttributeNames().hasMoreElements()) {
			rttr.addAttribute("session_port",(String) session.getAttribute("session_port"));
			rttr.addAttribute("session_callsign",(String) session.getAttribute("session_callSign"));
		}
		return "redirect:/user/mypage";
		}
		
	
	/**
	 * 회원가입을 위한 화면 요청
	 * @return join.html
	 * */
	@GetMapping("/join")
	public String join() {
		return "/pages/join";
	}
	
	/**
	 * 아이디 중복 체크
	 * */
	@GetMapping("/idCheck")
	@ResponseBody
	public String idCheck(@RequestParam(name="userid") String id) {
		log.info("아이디 중복을 체크합니다. 아이디: {}",id);
		boolean exist = userService.existId(id);
		if(!exist) {
			return "OK";
		}
		return "not";
	}
	
	/**
	 * 이메일 중복 체크
	 * */
	@GetMapping("/emailCheck")
	@ResponseBody
	public String emailCheck(@RequestParam(name="email")String email) {
		log.info("이메일 중복을 체크합니다. 이메일: {}",email);
		boolean exist = userService.existEmail(email);
		if(!exist) {
			return "OK";
		}
		return "not";
	}
	
	
	/**
	 * 회원 가입 요청
	 * @return login or join
	 * */
	@PostMapping("/join")
	public String joinin(@ModelAttribute UserDTO userDTO) {
		log.info("가입 요청 도착: {}",userDTO.toString());
		boolean result = userService.join(userDTO);
		if(result) {
			return "/pages/login";
		}
		return "/pages/join";
	}
}
