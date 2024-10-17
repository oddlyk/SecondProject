package com.kdigital.SecondProject.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kdigital.SecondProject.dto.AccidentStatusDTO;
import com.kdigital.SecondProject.dto.FavoriteVoyageDTO;
import com.kdigital.SecondProject.dto.LoginUserDetails;
import com.kdigital.SecondProject.dto.VoyageDTO;
import com.kdigital.SecondProject.service.AISService;
import com.kdigital.SecondProject.service.AccidentStatusService;
import com.kdigital.SecondProject.service.FavoriteVoyageService;
import com.kdigital.SecondProject.service.VoyageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {
	final VoyageService voyageService;
	final AISService aisService;
	final AccidentStatusService asService;
	final FavoriteVoyageService fvService;
	/**
	 * 첫 화면 요청
	 * @return "main.html"
	 * */
	@GetMapping({"","/"})
	public String main(
			@AuthenticationPrincipal  LoginUserDetails loginUser, //인증받은 사용자가 있다면 그 정보를 담아옴
			@RequestParam(name="search_ship", defaultValue="집갈래") String shipInfo, //검색버튼 클릭 시
			Model model
			) {
		// 인증을 받은 사용자라면 그 이름 저장 
		if(loginUser!=null) {
			model.addAttribute("loginName", loginUser.getUserName()); //사용자의 이름
			log.info("로그인됨!:{}",loginUser.getUsername());	//사용자의 아이디
		}else {
			log.info("로그인 안됨");
		}
		
		// 검색을 통해 접근했는지 여부 파악
			// 검색하지 않은 접근시, 바로 메인
		if(shipInfo.equals("집갈래")) {
			// 로그인된 사용자라면...
			if(loginUser!=null) {
				// 즐겨찾기 항해 정보 전달
				FavoriteVoyageDTO fvDTO = fvService.getTopFavoriteVoyage();
				// 즐겨찾기 항해가 없는 경우 랜덤한 항해 1개 전달
				if(fvDTO==null) {
					List<FavoriteVoyageDTO> fvDTOs = fvService.findAll();
					if(fvDTOs!=null) {
						Random random = new Random();
				        int randomIndex = random.nextInt(fvDTOs.size());
				        fvDTO = fvDTOs.get(randomIndex);
					}
				}
				//저장된 항해가 하나도 없는 경우 그냥 초기화면으로.
				if(fvDTO==null) {
					model.addAttribute("search", 0); 
					return "main";
				}

				VoyageDTO dto = VoyageDTO.toDTO(fvDTO.getVoyageEntity());
				log.info("사용자의 메인 항해: {}",dto);
				model.addAttribute("voyage", dto);
				model.addAttribute("search", 1); //사용자의 즐겨찾기 항해 정보를 포함함.
				// 항해 진행률 생성 및 전달
				String voyagePer = getVoyagePer(dto.getVNumber());
				model.addAttribute("voyagePer", voyagePer);
				
				// 목적항 사고 현황 전달
				AccidentStatusDTO asDTO = asService.getAccidentStatusByPortCode(dto.getPort().getPortCode()).get(0);
				model.addAttribute("accidentStatus", asDTO);
				
				// 항만 이용료 전달
				int usingFee = dto.getAnchorageFee()+dto.getBerthingFee()+dto.getEntryExitFee()+dto.getEntryExitFee()+dto.getSecurityFee();
				String portFee = String.format("%,d", usingFee);
				model.addAttribute("portFee", portFee);
				model.addAttribute("search_ship",dto.getShip().getCallSign());
				return "main";
			}
			else {
				model.addAttribute("search", 0); //검색 하지 않고 접근함.
				return "main";
			}
		}
		
		// 항해 정보 저장
		VoyageDTO voyageDTO = new VoyageDTO();
		log.info("(Controller) 데이터를 입력 받기 전의 항해 정보: {}",voyageDTO.toString());
			// 검색을 통해 접근
		VoyageDTO temp = voyageService.selectVoyageWithCallSign(shipInfo);
		log.info("(Controller) call sign으로 찾아온 항해 정보: {}",temp);
		if(temp!=null) voyageDTO = temp;
		
		temp = voyageService.selectVoyageWithMmsi(shipInfo);
		log.info("(Controller) MMSI로 찾아온 항해 정보: {}",temp);
		if(temp!=null) voyageDTO = temp;
		
		temp = voyageService.selectVoyageWithImo(shipInfo);
		log.info("(Controller) IMO로 찾아온 항해 정보: {}",temp);
		if(temp!=null) voyageDTO = temp;
		
		// 해당 선박 또는 항해가 없는 경우
		if(voyageDTO.getVNumber()==null) {
			model.addAttribute("search", 2);
			return "main";
		}
		
		// 항해가 제대로 존재함
		model.addAttribute("search", 1);
		// 추출된 항해 전달
		model.addAttribute("voyage", voyageDTO);
		
		// 항해 진행률 생성 및 전달
		String voyagePer = getVoyagePer(voyageDTO.getVNumber());
		model.addAttribute("voyagePer", voyagePer);
		
		// 목적항 사고 현황 전달
		AccidentStatusDTO asDTO = asService.getAccidentStatusByPortCode(voyageDTO.getPort().getPortCode()).get(0);
		model.addAttribute("accidentStatus", asDTO);
		
		// 항만 이용료 전달
		int usingFee = voyageDTO.getAnchorageFee()+voyageDTO.getBerthingFee()+voyageDTO.getEntryExitFee()+voyageDTO.getEntryExitFee()+voyageDTO.getSecurityFee();
		String portFee = String.format("%,d", usingFee);
		model.addAttribute("portFee", portFee);
		model.addAttribute("search_ship",voyageDTO.getShip().getCallSign());
		return "main";
	}

	private String getVoyagePer(Long vNumber) {
		LocalDateTime arrivalDate = voyageService.selectOne(vNumber).getArrivalDate();
		LocalDateTime departureDate = voyageService.selectOne(vNumber).getDepartureDate();
		LocalDateTime currentSignal = aisService.currentAISsignal(vNumber).getSignalDate();
		
		long totalVoyageMinutes = Duration.between(departureDate, arrivalDate).toMinutes();
		long untilTodayMinutes = Duration.between(currentSignal, arrivalDate).toMinutes();
		
		//현재 항해일 수 / 총 항해일 수 * 100 = 항해 진행률
		double voyageProgress = ((double) untilTodayMinutes / totalVoyageMinutes) * 100;
		log.info("현재 항해에 대한 항해 진행률: {}",voyageProgress);
		return String.format("%.2f", voyageProgress);
	}
	
	/**
	 * 항해 저장 요청
	 * */
	@PostMapping("/save")
	@ResponseBody
	public String saveFV(@RequestParam(name="vNumber") String SvNumber) {
		Long vNumber = Long.parseLong(SvNumber);
		boolean result = false;
		
		//이미 존재하는지 확인
		result = fvService.isExist(vNumber);
		if(result) return "exist";
		result = false;
		result = fvService.favorite(vNumber);
		if(result) {
			return "OK";
		}
		return "fail";
	}
	/*
	 * @PostMapping("/")
	public String predict(
			@RequestParam(name="callSign") String callSign, // 등록 버튼 클릭 시
			Model model) {
		log.info("String : {}",callSign);
		if(callSign=="-1") {
			return "main";
		}
		boolean result = fvService.favorite(callSign);
		if(result) {
			return "main";
		}
	}
	 * */
}
