package br.sp.lmxlinares.rest;

import static io.restassured.RestAssured.*;
import static java.util.Arrays.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {
	
	@Test
	public void testOlaMundo  () {
		Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue(response.getStatusCode()==(200));
		Assert.assertTrue("O SatusCode devria ser 200", response.statusCode()==(200));
		Assert.assertEquals(200, response.statusCode()); 	
		
    	ValidatableResponse validacao = response.then();
    	validacao.statusCode(200);
	}
	
	@Test
	public void devoConhecerOutrasFormasRestAssured (){
		Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
    	ValidatableResponse validacao = response.then();
    	validacao.statusCode(200);
    	
    	get("http://restapi.wcaquino.me/ola").then().statusCode(200);		
    	
    	given()
    	.when()
    		.get("http://restapi.wcaquino.me/ola")
    	.then()
    		 .statusCode(200);
	
	}
	
	@Test
	public void devoConhecerMatchersHamcrest() {
		assertThat("Maria", is("Maria"));
		assertThat(128, isA(Integer.class));
		assertThat(128d, isA(Double.class));//Verifica o tipo do elemento
		assertThat(128, greaterThan(120));//Verifica se o primeiro numero é maior que o segundo
		assertThat(128, lessThan(130));//Verifica se o primeiro numero é menor que o segundo
		
		List<Integer> impares = asList(1,3,5,7,9);
		
		assertThat(impares, hasSize(5)); //Verifica o tamanho da lista
		assertThat(impares, contains(1,3,5,7,9)); //Lista inteira e na mesma ordem.
		assertThat(impares, containsInAnyOrder(1,3,5,9,7)); //Lista inteira em qualquer ordem.
		assertThat(impares, hasItem(1));//Verifica se a lista contém um item específico
		assertThat(impares, hasItems(1,5));
		
		//Verificando varios matchers em uma linha
		assertThat("Maria", is(not("João")));
		assertThat("Maria", not("João"));
		assertThat("Joaquina", anyOf(is("Maria"), is("Joaquina")));
		assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui")));
	}
	
	@Test
	public void devoValidarBody () {
		given()
    	.when()
    		.get("http://restapi.wcaquino.me/ola")
    	.then()
    		 .statusCode(200)
    		 .body(is("Ola Mundo!"))
    		 .body(containsString("Mundo"))
    		 .body(is(not(nullValue())));
	}
	
}
