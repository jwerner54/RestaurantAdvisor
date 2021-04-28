package com.example.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Order;

@Component
public class OrderDAO {
	
	@Autowired
	private orderMapper oMap;

	public void insertOrder(Order order) {
		
		oMap.insertOrder(order);
		
	}
	

}
