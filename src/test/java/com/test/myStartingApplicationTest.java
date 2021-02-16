package com.test;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.test.dto.CarDto;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import stcore.commons.traffic.ApiResult;
import sx2.commons.runtime.util.TestUtil;

//@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class myStartingApplicationTest {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	public JdbcTemplate jdbcTemplate;

	public CarDto carDto;

	@BeforeAll
	public void initialiseCarDto() {
		//setting up a carDto object to pass as a body for later use
		carDto = new CarDto();
		carDto.setBrand(TestData.carDtoBrand);
		carDto.setColor(TestData.carDtoColor);
		carDto.setFuelCapacity(TestData.carDtoFuelCapacity);
		carDto.setSeats(TestData.carDtoSeats);
		String baseUri = "http://localhost:" + "8080/carController";
		this.webTestClient = WebTestClient.bindToServer().baseUrl(baseUri).build();
	}

	@Nested
	public class insertCarTest {
		@BeforeEach
		public void initialise() {
			System.out.println("Starting insertCarTest");
			String sql1 = "SELECT COUNT(ID) FROM CAR";
			int count1 = jdbcTemplate.queryForObject(sql1, new Object[]{}, Integer.class);
			System.out.println("count1 : " + count1 );
		}

		@Test
		public void insertCar() throws Exception{
			System.out.println("Inserting a Car");
			String uri = "/insertCar";

			String sql1 = "SELECT COUNT(ID) FROM CAR";
			int count1 = jdbcTemplate.queryForObject(sql1, new Object[]{}, Integer.class);
			System.out.println("count1 : " + count1 );

			ApiResult<CarDto> result = TestUtil.reactiveApiPostBody(uri, webTestClient, carDto, CarDto.class, true);
			//test if it was a success
			System.out.println(result);
			Assertions.assertTrue(result.resultOk());

			//Extracting the carDto object from the ApiResult<CarDto>
			CarDto retrievedDto = result.getData();
			System.out.println(retrievedDto);
			//Using getter methods to verify that the retrieved carDto was the same one that we sent
			Assertions.assertTrue(retrievedDto.getBrand().equalsIgnoreCase(TestData.carDtoBrand));
			Assertions.assertTrue(retrievedDto.getColor().equalsIgnoreCase(TestData.carDtoColor));

			//counting the number of records in the table after adding the new car
			String sql2 = "SELECT COUNT(ID) FROM CAR ";
			int count2 = jdbcTemplate.queryForObject(sql2, new Object[]{}, Integer.class);
			System.out.println("count2 : " + count2 );
			//checking whether the new count is +1 of the old count
			Assertions.assertTrue(count2 == count1 + 1);
		}
		@AfterEach
		public void deleteTestCar() {
			String cleanupDb = "DELETE FROM CAR";
			jdbcTemplate.update(cleanupDb);
		}
	}

	@Nested
	public class deleteCarTest{
		@BeforeEach
		public void initialise() {
			System.out.println("Starting deleteCarTest()");
			jdbcTemplate.execute("INSERT INTO CAR (ID, BRAND, COLOR, FUELCAPACITY, SEATS) VALUES (car_sequence.nextval, '" + TestData.carDtoBrand +"', '"+ TestData.carDtoColor +"', " +
					TestData.carDtoFuelCapacity +", "+ TestData.carDtoSeats + ")");
		}

		@Test
		public void deleteCar() {
			System.out.println("I MADE IT HERE");
			int countBefore = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM CAR", Integer.class);
			String uri = "/deleteCar";
			int createdId = jdbcTemplate.queryForObject("SELECT id FROM CAR WHERE BRAND = '" + TestData.carDtoBrand + "'",Integer.class);

			Map<String,Object> newMap = new HashMap<String, Object>();
			newMap.put("id", createdId);
			System.out.println(newMap);

			ApiResult<Boolean> deleteResult = TestUtil.reactiveApiDelete(uri, webTestClient, new HashMap<String, String>(),
					newMap, Boolean.class, true, 200);


			Assertions.assertTrue(deleteResult.resultOk());

			int countAfter = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM CAR", Integer.class);

			Assertions.assertTrue(countAfter + 1 == countBefore);

		}
	}

	@Nested
	public class getAllCarTest {
		@BeforeEach
		public void insertTestCar() throws Exception{
			jdbcTemplate.execute("INSERT INTO CAR (ID, BRAND, COLOR, FUELCAPACITY, SEATS) VALUES (car_sequence.nextval, '" + TestData.carDtoBrand +"', '"+ TestData.carDtoColor +"', " +
					TestData.carDtoFuelCapacity +", "+ TestData.carDtoSeats + ")");
		}

		@Test
		public void getAllCars() throws Exception {
			//message to show on the console that it is working
			System.out.println("Beginning getAllCars test");
			//uri for the reactiveApiGet method later
			String uri = "/getAllCars";
			//TestUtil.reactiveApiGet requires a uri, a webtestclient and an expected class
			ApiResult<List> result = TestUtil.reactiveApiGet(uri, webTestClient, List.class);
			//This assertion checks whether we can get the result
			Assertions.assertTrue(result.resultOk());
			//This assertion checks that it is NOT empty
			Assertions.assertFalse(result.getData().isEmpty());
			//Retrieves the results from before
			List results = result.getData();
			//Casting a set of data into an object in the results list
			Object obj = results.get(0);
			//Casts the object into a map because it is currently like this: {id=1, brand=Mercedes, color=White, fuelCapacity=3000, seats=1}
			Map<String, Object> objMap = (Map<String,Object>) obj;
			//Using map methods such as .get("name of the column name") we can retrieve the field values, and make it into a String
			String color = (String.valueOf(objMap.get("color")));
			//Checks whether the field we retrieved is equals to what we expect it to be. In this case, we expect it to be the color of the
			//first entry that we have added. (might have to add your own before starting this if there are no entries)
			assertEquals(TestData.carDtoColor, color);
		}

		@AfterEach
		public void deleteTestCar() throws Exception{
			//clean up initial test data member
			String deletesql = "DELETE FROM CAR";
			jdbcTemplate.update(deletesql);
		}
	}

	@Nested
	public class changeParamTest {

		@BeforeEach
		public void insertTestCar() {
			jdbcTemplate.execute("INSERT INTO CAR (ID, BRAND, COLOR, FUELCAPACITY, SEATS) VALUES (car_sequence.nextval, '" + TestData.carDtoBrand +"', '"+ TestData.carDtoColor +"', " +
					TestData.carDtoFuelCapacity +", "+ TestData.carDtoSeats + ")");
		}

		@Test
		public void changeParam() throws Exception{
			System.out.println("Beginning changeParam test");

			String uri = "/changeParam";

			CarDto carDto2 = new CarDto();
			carDto2.setBrand("HONDA V2");
			carDto2.setColor("BLACK V2");
			carDto2.setFuelCapacity(5000);
			carDto2.setSeats(6);

			ApiResult<CarDto> result = TestUtil.reactiveApiPutBody(uri, webTestClient, carDto2, CarDto.class, true);
			CarDto resultCar = result.getData();

			Assertions.assertTrue(resultCar.getBrand().equals("HONDA V2"));
			Assertions.assertTrue(resultCar.getColor().equals("BLACK V2"));
			Assertions.assertTrue(resultCar.getFuelCapacity().equals(5000));
			Assertions.assertTrue(resultCar.getSeats().equals(6));
		}

		@AfterEach
		public void deleteTestCar() throws Exception{
			//clean up initial test data member
			String deletesql = "DELETE FROM CAR";
			jdbcTemplate.update(deletesql);
		}
	}

//	@Nested
//	public class inspectDb {
//		@Test
//		public void checkDb() {
//			String sqlStatement = "SELECT COUNT(*) FROM CAR";
//			int counter = jdbcTemplate.queryForObject(sqlStatement, new Object[]{}, Integer.class);
//			System.out.println("Rows in database:" + counter);
//		}
//	}
//	@AfterAll
//	public void cleanup() {
//		String sqlStatement = "SELECT COUNT(*) FROM CAR";
//		int counter = jdbcTemplate.queryForObject(sqlStatement, new Object[]{}, Integer.class);
//		System.out.println("Rows in database:" + counter);
//		//delete all rows in Car database
//		String cleanupDb = "DELETE FROM CAR";
//		jdbcTemplate.update(cleanupDb);
//	}

}
