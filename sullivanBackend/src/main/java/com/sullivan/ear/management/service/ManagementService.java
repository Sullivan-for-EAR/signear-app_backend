package com.sullivan.ear.management.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sullivan.ear.management.dao.ManagementDTO;
import com.sullivan.ear.management.repository.ManagementHistoryRepository;
import com.sullivan.ear.management.repository.ManagementRepository;
import com.sullivan.ear.vo.Reservation;
import com.sullivan.ear.vo.ReservationHistory;

@Service
@Transactional
public class ManagementService {

	ManagementRepository managementRepository;
	
	ManagementHistoryRepository managementHistoryRepository;
	//1:읽지않음, 2:센터확인중, 3:예약확정, 4.예약취소, 5:예약거절, 
	//6: 통역취소, 7:통역 완료, 8: 긴급통역 연결중, 9: 긴급통역 취소, 10: 긴급통역 승인
	
	//6번 : 통역 취소 
    //-> 1, 2, 4, 5, 9
    //-> 8번 항목, 오늘 날짜 기준으로 24시간 비교 필요
    //     오늘날짜 + start -24 > 예약 날짜 + start

    //7번 : 통역 완료 
    //-> 3, 10 케이스
	
	@Autowired
	public ManagementService(ManagementRepository managementRepository, ManagementHistoryRepository managementHistoryRepository) {

		this.managementRepository = managementRepository;
		this.managementHistoryRepository = managementHistoryRepository;
	}
	
	public List<Reservation> getListByCustomerID(Integer customerID) {
		String localTime = LocalDateTime.now().toString().substring(0, 10).replaceAll("-", "");
		List<Reservation> customerManagementList = managementRepository.findByCustomerID(customerID, localTime);
		List<Reservation> customerManagementReturnList = this.getList(customerManagementList);
		
		return customerManagementReturnList;
	}
	
	public List<Reservation> getListBySignID(Integer signID) {	
		String localTime = LocalDateTime.now().toString().substring(0, 10).replaceAll("-", "");
		List<Reservation> signManagementList = managementRepository.findBySignID(signID, localTime);
		List<Reservation> signManagementReturnList = this.getList(signManagementList);
				
		return signManagementReturnList;
	}
	
	public void delete(Integer Reservation_id) {
		managementRepository.deleteById(Reservation_id);
	}
	
	public int getCurrentTime(int gap) {
		
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat hour = new SimpleDateFormat("HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());		
		cal.add(Calendar.DATE, -gap);	
		String ysTime = date.format(cal.getTime());
		String hourTime = hour.format(new Date()).replaceAll(":", "").substring(0, 2);
		
		String finalTime = ysTime + hourTime;
		
		return Integer.parseInt(finalTime);
	}
	
    public List<Reservation> getList(List<Reservation> ManagementList) {
		
		List<Reservation> returnList = new ArrayList();
		
		for(Reservation management: ManagementList) {
		
            //ManagementDTO managementDTO = this.getManagementMapper(management);
            ReservationHistory reservationHistory = new ReservationHistory();
            reservationHistory.setRsID(management.getRsID());
            reservationHistory.setFromStatus(management.getStatus());
			
			int status = management.getStatus();
		    if (status == 1 || status == 2 || status == 4 || status == 5 || status == 9) {
		    	management.setStatus(6);
		    	reservationHistory.setToStatus(6);
		    	managementHistoryRepository.save(reservationHistory);
		    }
		    
		    if (status == 8) {
		    	int currenTime = this.getCurrentTime(1);
		    	int myDate = Integer.parseInt(management.getDate().replaceAll("-", "") + management.getStart_time().substring(0, 2));
		    	
		    	if (currenTime > myDate) {
		    		management.setStatus(6);
		    		reservationHistory.setToStatus(6);
			    	managementHistoryRepository.save(reservationHistory);
		    	}
		    		
		    }
		    
		    if (status == 3 || status == 10) {
		    	management.setStatus(7);
	    		reservationHistory.setToStatus(7);
		    	managementHistoryRepository.save(reservationHistory);
		    }
		    
		    if (management.getStatus()!= 8) {
		    	returnList.add(management);
		    }
		}
			
		return returnList;
	}
    
    public ManagementDTO getManagementMapper(Reservation management) {

    	ManagementDTO managementDTO = new ManagementDTO();
    	
    	managementDTO.setRsID(management.getRsID());
    	managementDTO.setDate(management.getDate());
    	managementDTO.setStart_time(management.getStart_time());
    	managementDTO.setEnd_time(management.getEnd_time());
    	managementDTO.setArea(management.getArea());
    	managementDTO.setAddress(management.getAddress());
    	managementDTO.setMethod(management.getMethod());
    	managementDTO.setStatus(management.getStatus());
    	managementDTO.setType(management.getType());
    	managementDTO.setRequest(management.getRequest());
    	managementDTO.setReject(management.getReject());
    	
        return managementDTO;
    }
}
