package com.test.api;

import com.test.Entity.Car;
import com.test.business.CarBusiness;
import com.test.dto.CarDto;
import com.test.feign.PortFeignClient;
import com.test.mapper.CarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import stcore.commons.traffic.ApiResult;

import java.util.List;

@RestController
@RequestMapping("/carController")
public class CarController {

	@Autowired
	private CarMapper carMapper;

	@Autowired
	private CarBusiness carBusiness;

	@Autowired
	private PortFeignClient portFeignClient;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@GetMapping("/getAllCars")
	public Mono<ApiResult<List<Car>>> view() {
		List cars = carBusiness.carDetails();
		return Mono.just(ApiResult.ok(cars));
	}

	@PutMapping("/changeParam")
	public Mono<ApiResult<CarDto>> changeParam(@RequestBody CarDto carDto) {
		carBusiness.changeParam(carDto);
		return Mono.just(ApiResult.ok(carDto));
	}

	@DeleteMapping("/deleteCar")
	public Mono<ApiResult<Boolean>> deleteCar(@RequestParam int id) {
		carBusiness.deleteCar(id);
		return Mono.just(ApiResult.ok(true));
	}

	@PostMapping("/insertCar")
	public Mono<ApiResult<CarDto>> createCar(@RequestBody CarDto carDto) {
		CarDto carDtoReturn = carBusiness.insertCar(carDto);
		return Mono.just(ApiResult.ok(carDtoReturn));
	}

	@GetMapping("/getPort")
	public String getPort() {
		return portFeignClient.getPort();
	}

	@PostMapping("/transactional")
	public String testTransactional(@RequestBody CarDto carDto) throws Exception {
		System.out.println("HELLO");
		carBusiness.testTransactional(carDto);
		return "done";
	}
}

