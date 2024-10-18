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
import org.springframework.web.bind.annotation.PostMapping;
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
			, Model model
			) {
		System.out.println(error);
		System.out.println(errMessage);

		model.addAttribute("error", error);
		model.addAttribute("errMessage", errMessage);

		return "pages/login";
	}

	
	/**
	 * 마이페이지로 연결 + 정보 전달
	 * @param model
	 * @param principal
	 * @return
	 */
	@GetMapping("/mypage")
	public String mypage(Model model, Principal principal) {
		// 1. 로그인한 유저의 정보 가져오기
		String userId = principal.getName();
		UserDTO user = userService.selectOne(userId);		// userId 가져오기
		
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
		
		
		// 3. 각 vNumber로 Voyage 및 ship 정보 조회
		List<VoyageDTO> voyages = new ArrayList<>();
		List<String> voyagePerList = new ArrayList<>();
		List<Integer> favoriteList = new ArrayList<>();
		for (FavoriteVoyageDTO favoriteVoyage : favoriteVoyages) {
			VoyageEntity voyageEntity = favoriteVoyage.getVoyageEntity();
			
			// call-sign, 출발지, 도착지, 선박명 추출
			String callSign = voyageEntity.getShip().getCallSign();
			String departure = voyageEntity.getDeparture();
			String destination = voyageEntity.getPort().getPortCode();
			String shipName = voyageEntity.getShip().getShipName();
			
			Long vNumber = voyageEntity.getVNumber();
			
			// 운행률 계산
			String voyagePer = getVoyagePer(vNumber);
			voyagePerList.add(voyagePer);
			
			// TopFavorite 값 추가
			int favorite = Integer.parseInt(favoriteVoyage.getTopFavorite());
			favoriteList.add(favorite);
			
			// VoyageDTO 생성 후 리스트에 추가
			ShipEntity shipEntity = voyageEntity.getShip();
			PortEntity portEntity = voyageEntity.getPort();
			
			VoyageDTO voyageDTO = new VoyageDTO();
			voyageDTO.setVNumber(vNumber);
			voyageDTO.setShip(shipEntity);
			voyageDTO.setDeparture(departure);
			voyageDTO.setPort(portEntity);
			
			voyages.add(voyageDTO);
		}
		
		// 모델에 목록 추가
		model.addAttribute("userName", userId);
		model.addAttribute("user", user.getUserId());
		model.addAttribute("userType", userType);
		model.addAttribute("voyages", voyages);
		model.addAttribute("voyagePer", voyagePerList);
		
		return "pages/mypage";
	}

	
	private String getVoyagePer(Long vNumber) {
		LocalDateTime arrivalDate = voyageService.selectOne(vNumber).getArrivalDate();
		LocalDateTime departureDate = voyageService.selectOne(vNumber).getDepartureDate();
		LocalDateTime currentSignal = null;
		AISDTO aisDTO = aisService.currentAISsignal(vNumber);
		if(aisDTO==null) {
			log.info("항해 시작 전");
			return String.format("%.2f", 0.0);
		}
		currentSignal = aisDTO.getSignalDate(); // aisDTO가 null이 아닐 때만 호출
		long totalVoyageMinutes = Duration.between(departureDate, arrivalDate).toMinutes();
		long untilTodayMinutes = Duration.between(currentSignal, arrivalDate).toMinutes();
		
		//현재 항해일 수 / 총 항해일 수 * 100 = 항해 진행률
		double voyageProgress = ((double) untilTodayMinutes / totalVoyageMinutes) * 100;
		log.info("현재 항해에 대한 항해 진행률: {}",voyageProgress);
		return String.format("%.2f", voyageProgress);
	}
	
	
	
	/**
	 * 즐겨찾기 변경
	 * @param vNumber
	 * @return
	 */
	@PostMapping("/favorite")
	@ResponseBody
	public Map<String, Object> changeFavorite(@RequestParam("vNumber") Long vNumber) {
		Map<String, Object> response = new HashMap<>();
		boolean success = favoriteVoyageService.changeTopFavorite(vNumber);
		response.put("success", success);
		return response;
	}
	
	
	@PostMapping("/deleteFavorite")
	public String deleteFavorite(@RequestParam("vNumbers") List<Long> vNumbers) {

		for (Long vNumber : vNumbers) {
			favoriteVoyageService.deleteFev(vNumber);
			}
		
		return "redirect:/user/mypage";
		}
		
	
	
}
