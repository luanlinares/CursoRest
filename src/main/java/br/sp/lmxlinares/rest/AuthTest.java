package br.sp.lmxlinares.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;


public class AuthTest {
	
	@Test
	public void deveAcessarSWAPI ()
	{
		given()
			.log().all()
		.when()
			.get("https://swapi.dev/api/people/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Luke Skywalker"))
		;
		
	}
	
	@Test
	public void deveObterClima ()
	{
		given()
			.log().all()
			.queryParam("q", "Bauru")
			.queryParam("appid", "b6e7d7c661ca5ba4e64de0d4b19c69e8")
			.queryParam("units", "metric")
		.when()
			.get("http://api.openweathermap.org/data/2.5/weather")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Bauru"))
			.body("coord.lon", is(-49.06f))
			.body("main.temp", greaterThan(19f));	
	}
	
	
	@Test
	public void naoDeveAcessarSemSenha ()
	{
		given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401);	
	}
	
	@Test
	public void deveFazerAutenticacaoBasica ()
	{
		given()
			.log().all()
		.when()
			.get("http://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"));	
	}
	
	@Test
	public void deveFazerAutenticacaoBasica2 ()
	{
		given()
			.log().all()
			.auth().basic("admin", "senha")
		.when()
			.get("http://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"));	
	}
	
	@Test
	public void deveFazerAutenticacaoBasicaChallenge ()
	{
		given()
			.log().all()
			.auth().preemptive().basic("admin", "senha")
		.when()
			.get("http://restapi.wcaquino.me/basicauth2")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"));	
	}
	
	@Test
	public void deveFazerAutenticaComToken () {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "luanlnrs@gmail.com");
		login.put("senha", "123456");
		
		//Login na API
		//Receber o token
		String token = given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when()
			.post("http://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token");	
		
		//Obter as contas
			given()
				.log().all()
				.header("Authorization", "JWT " + token )
				.body(login)
				.contentType(ContentType.JSON)
			.when()
				.get("http://barrigarest.wcaquino.me/contas")
			.then()
				.log().all()
				.statusCode(200)
				.body("nome", hasItem("Conta Luan"));	
	}
	
	@Test
	public void deveAcessarAplicacaoWeb () {
		//Login
		String cookie = given()
		.log().all()
		.formParam("email", "luanlnrs@gmail.com")
		.formParam("senha", "123456")
		.contentType(ContentType.URLENC.withCharset("UTF-8"))
	.when()
		.post("http://seubarriga.wcaquino.me/logar")
	.then()
		.log().all()
    	.statusCode(200)
    	.extract().header("set-cookie");
		cookie = cookie.split("=")[1].split(";")[0];
		System.out.println(cookie);
		
		
		//Obter conta
		
			String body = given()
				.log().all()
				.cookie("connect.sid", cookie)
			.when()
				.get("http://seubarriga.wcaquino.me/contas")
			.then()
				.log().all()
		    	.statusCode(200)
		    	.body("html.body.table.tbody.tr[0].td[0]", is("Conta Luan"))
		    	.extract().body().asString();
			
			System.out.println("====================================");
			XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML, body);
			System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
				
	}
	
}

//b6e7d7c661ca5ba4e64de0d4b19c69e8
//http://api.openweathermap.org/data/2.5/weather?q=Bauru&appid=b6e7d7c661ca5ba4e64de0d4b19c69e8&units=metric