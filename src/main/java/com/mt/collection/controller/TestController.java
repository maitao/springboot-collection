package com.mt.collection.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mt.collection.domain.IShaWuRepository;
import com.mt.collection.domain.User;
import com.mt.collection.domain.UserRepository;
import com.mt.collection.processor.ishawu.IShaWuSpiderTest;

@RestController
public class TestController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private IShaWuRepository iShaWuRepository;

	@GetMapping(value = "/getMsg")
	public String getMsg(@RequestParam Integer id) {
		return "mt collection";
	}
	
	@GetMapping(value = "/getUser")
	@ResponseBody
	public Map<String, Object> getUser(@RequestParam Integer id) {
		Map<String, Object> data = new HashMap<>();
		data.put("id", id);
		data.put("userName", "admin");
		data.put("from", "provider-A");
		userRepository.save(new User(5L, "didi", 30));

		return data;
	}

	@GetMapping(value = "/ishawu")
	@ResponseBody
	public Map<String, Object> getUser() {
		Map<String, Object> data = new HashMap<>();
		data.put("userName", "admin");
		data.put("from", "provider-A");
		new Thread(new IShaWuSpiderTest(iShaWuRepository)).start();
		return data;
	}
}