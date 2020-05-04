package br.sp.lmxlinares.rest;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;

import org.junit.Test;

import org.xml.sax.SAXParseException;

import io.restassured.module.jsv.JsonSchemaValidator;



public class SchemaTest {
	
	@Test
	public void deveValidarSchemaXML () {
		given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(200)
			.body(matchesXsdInClasspath("users.xsd"));
	}
	
	@Test(expected=SAXParseException.class)
	public void naoDeveValidarSchemaXMLInvalido () {
		given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/invalidUsersXML")
		.then()
			.log().all()
			.statusCode(200)
			.body(matchesXsdInClasspath("users.xsd"));
	}
	
	@Test
	public void deveValidarSchemaJSON () {
		given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(200)
			.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("users.json"));
	}
	
		
}
