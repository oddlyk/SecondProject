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

import com.kdigital.SecondProject.dto.AISDTO;
import com.kdigital.SecondProject.dto.AccidentStatusDTO;
import com.kdigital.SecondProject.dto.FavoriteVoyageDTO;
import com.kdigital.SecondProject.dto.LoginUserDetails;
import com.kdigital.SecondProject.dto.VoyageDTO;
import com.kdigital.SecondProject.service.AISService;
import com.kdigital.SecondProject.service.AccidentStatusService;
import com.kdigital.SecondProject.service.FavoriteVoyageService;
import com.kdigital.SecondProject.service.VoyageService;

import jakarta.servlet.http.HttpSession;
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
			@RequestParam(name="search_ship", defaultValue="-1") String shipInfo, //검색버튼 클릭 시
			HttpSession session,
			Model model
			) {
		String tempCallSign = "-1";
		// 인증을 받은 사용자라면 그 이름 저장 
		if(loginUser!=null) {
			model.addAttribute("loginName", loginUser.getUserName()); //사용자의 이름
			log.info("로그인됨!:{}",loginUser.getUsername());	//사용자의 아이디
		}else {
			log.info("로그인 안됨");
		}
		
		// 검색을 통해 접근했는지 여부 파악
			// 검색하지 않은 접근시, 바로 메인
		if(shipInfo.equals("-1")) {
			// 로그인된 사용자라면... + 검색한적이 없다면.
			if(session.getAttribute("session_callSign")==null&&loginUser!=null) {
				// 즐겨찾기 항해 정보 전달
				FavoriteVoyageDTO fvDTO = fvService.getTopFavoriteVoyage();
				// 즐겨찾기 항해가 없는 경우 랜덤한 항해 1개 전달
				if(fvDTO==null) {
					List<FavoriteVoyageDTO> fvDTOs = fvService.findAll();
					if(fvDTOs==null) {
						log.info("저장된 항해 없음");
						//저장된 항해가 하나도 없는 경우 그냥 초기화면으로.
						model.addAttribute("search", 0); 
						model.addAttribute("search_ship",tempCallSign);
						return "main";
					}
					if(fvDTOs!=null) {
						Random random = new Random();
				        int randomIndex = random.nextInt(fvDTOs.size());
				        fvDTO = fvDTOs.get(randomIndex);
					}
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
				
				//기존 세션을 덮어씌움
				sessionSave(dto.getPort().getPortCode(),dto.getShip().getCallSign(), session);
				//기존 세션 확인 및 값 전달
				if(session.getAttributeNames().hasMoreElements()) {
					model.addAttribute("session_port",(String) session.getAttribute("session_port"));
					model.addAttribute("session_callsign",(String) session.getAttribute("session_callSign"));
				}
				return "main";
			} 
			else if(session.getAttribute("session_callSign")!=null&&loginUser!=null) { 
				// 로그인된 사용자가 로그인 전 검색한 적이 있었다면
				VoyageDTO dto = voyageService.selectVoyageWithCallSign(session.getAttribute("session_callSign").toString());
				if(dto==null) {
					log.info("검색된 항해 없음");
					//저장된 항해가 하나도 없는 경우 그냥 초기화면으로.
					model.addAttribute("search", 0); 
					model.addAttribute("search_ship",tempCallSign);
					return "main";
				}
				log.info("사용자가 마지막으로 검색한 항해: {}",dto);
				
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
				

				//기존 세션을 덮어씌움
				sessionSave(dto.getPort().getPortCode(),dto.getShip().getCallSign(), session);
				
				//기존 세션 확인 및 값 전달
				if(session.getAttributeNames().hasMoreElements()) {
					model.addAttribute("session_port",(String) session.getAttribute("session_port"));
					model.addAttribute("session_callsign",(String) session.getAttribute("session_callSign"));
				}
				return "main";
			}
			else {
				model.addAttribute("search", 0); //검색 하지 않고 접근함.
				model.addAttribute("search_ship",tempCallSign);
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
		tempCallSign = voyageDTO.getShip().getCallSign();
		model.addAttribute("search_ship",tempCallSign);
		sessionSave(voyageDTO.getPort().getPortCode(),voyageDTO.getShip().getCallSign(), session);
		if(session.getAttributeNames().hasMoreElements()) {
  			model.addAttribute("session_port",(String) session.getAttribute("session_port"));
  			model.addAttribute("session_callsign",(String) session.getAttribute("session_callSign"));
  		}
		return "main";
	}

	/**
	 * 항해 진행률 계산
	 * */
	private String getVoyagePer(Long vNumber) {
		VoyageDTO voyage = voyageService.selectOne(vNumber);
		LocalDateTime arrivalDate = voyage.getArrivalDate();
		LocalDateTime departureDate = voyage.getDepartureDate();
		
		AISDTO aisDTO = aisService.currentAISsignal(vNumber);
		if(aisDTO==null) {
			log.info("항해 시작 전");
			return String.format("%.2f", 0.0);
		}
		LocalDateTime currentSignal = aisDTO.getSignalDate(); // aisDTO가 null이 아닐 때만 호출
		
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
		
		List<FavoriteVoyageDTO> fvdtos = fvService.findAll();
		if(fvdtos!=null && fvdtos.size()>=10) {
			return "over";
		}
		
		result = false;
		result = fvService.favorite(vNumber);
		if(result) {
			return "OK";
		}
		return "fail";
	}
	
	/**
	 * 항해 세션 저장 
	 * */
	public void sessionSave(String port, String callSign, HttpSession session) {
		//기존 세션 확인 및 초기화
		if(session.getAttributeNames().hasMoreElements()) {
			log.info("과거 저장한 항구: {}",(String) session.getAttribute("session_port"));
			log.info("과거 저장한 항해: {}",(String) session.getAttribute("session_callSign"));
		}
		if(session!=null) {
			session.removeAttribute("session_port");
			session.removeAttribute("session_callSign");
		}
		session.setAttribute("session_port", port);
		session.setAttribute("session_callSign", callSign);
		log.info("새로 저장한 항구: {}",port);
		log.info("새로 저장한 항해: {}",callSign);
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
	
	/**
	 * 기업소개 화면 전달
	 * */
	@GetMapping("/aboutUs")
	public String aboutUs(HttpSession session, Model model) {

		//기존 세션 확인 및 값 전달
		if(session.getAttributeNames().hasMoreElements()) {
		   model.addAttribute("session_port",(String) session.getAttribute("session_port"));
		   model.addAttribute("session_callsign",(String) session.getAttribute("session_callSign"));
		}
		return "pages/aboutUs";
	}
}
