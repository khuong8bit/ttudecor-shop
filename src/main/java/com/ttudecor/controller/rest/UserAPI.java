package com.ttudecor.controller.rest;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ttudecor.dto.OrderDto;
import com.ttudecor.dto.UserDto;
import com.ttudecor.entity.Order;
import com.ttudecor.entity.User;
import com.ttudecor.service.OrderService;
import com.ttudecor.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserAPI {
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private HttpSession session;

	@GetMapping("all")
	public ResponseEntity<List<UserDto>> getAllUsers(){
		List<UserDto> list = userService.getAllUserDto();
		if(list.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<UserDto>>(list, HttpStatus.OK);
	}
	
	@GetMapping("{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable("userId") Integer userId) {
		UserDto user = userService.findUserDtoById(userId);
		if(user == null) return new ResponseEntity(HttpStatus.NO_CONTENT);
		return new ResponseEntity<UserDto>(user, HttpStatus.OK);
	}
	
	//create new user
	@PostMapping("create")
	public ResponseEntity<UserDto> createNewUser(@RequestBody UserDto userNew) {
		//
		return new ResponseEntity<UserDto>(userNew, HttpStatus.OK);
	}

	//update user
	@PutMapping("{userId}")
	public ResponseEntity<UserDto> updateUserById(@PathVariable("userId") Integer userId, @RequestBody UserDto userUpdate) {
		userService.update(userUpdate, userId);
		userUpdate.setId(userId);
		return new ResponseEntity<UserDto>(userUpdate, HttpStatus.OK);
	}
	
	//delete user
	@DeleteMapping("{userId}")
	public ResponseEntity deleteUserById(@PathVariable("userId") Integer userId) {
		User user = new User();
		user.setId(userId);
		userService.delete(user);
		return new ResponseEntity( HttpStatus.OK);
	}
	
	//Show list order
	@GetMapping("order")
	public String listOrder(Model model) {

		int id = (int) session.getAttribute("userId");

		List<OrderDto> listOrders = orderService.findOrderDtoByUserId(id);
		model.addAttribute("orders", listOrders);
	
		return "shop/profile";
		
	}
	
	//order detail
	@GetMapping("order/detail/{id}")
	public String orderDetail(Model model, @PathVariable("id") Integer orderId) {
		
		try {
			Order order = orderService.findById(orderId).get();
			
			//order of guest
			if(order.getUser() == null) 
				return "redirect:/profile/order"; 
			else {
				int userId = (int) session.getAttribute("userId");
				
				//order of other user
				if(order.getUser().getId() != userId)
					return "redirect:/profile/order";
			}
			
			OrderDto dto = orderService.copy(order);
			
			model.addAttribute("order", dto);
			model.addAttribute("orderDetails", order.getOrderDetails());

			return "shop/profile";
		} catch (Exception e) {
			return "redirect:/profile/order";
		}
		
	}
	
	
	//Change password
	@GetMapping("change-password")
	public String changePassword(Model model) {
		
		model.addAttribute("changePassword", true);
		
		return "shop/profile";
		
	}
	
	@PostMapping("change-password")
	public String changePassword(Model model, @RequestParam String oldPassword,
			@RequestParam String password, @RequestParam String rePassword) {
		
		int id = (int) session.getAttribute("userId");
		userService.changePassword(model, id, oldPassword, password, rePassword);
		
		model.addAttribute("changePassword", true);
		return "shop/profile";
		
	}
	
	
	
	
}
