package com.itwillbs.bookjuk.controller.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.event.Notification;
import com.itwillbs.bookjuk.service.event.NotificationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class NotificationController {

	@Autowired
	private NotificationService notiService;

	
	@GetMapping("/{recipient}")
	@ResponseBody
	public List<Notification> getNoti(@PathVariable UserEntity recipient) {
		return notiService.getNoti(recipient);
	}
	
	@PostMapping("/send")
	@ResponseBody
	public void sendNoti(@RequestBody Notification noti) {
		notiService.sendNoti(noti);
	}
	
	@GetMapping("/notification")
	public String notification() {
		return "/notification/notification";
	}
	
	
}
