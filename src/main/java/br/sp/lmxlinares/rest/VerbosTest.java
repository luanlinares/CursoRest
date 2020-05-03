package br.sp.lmxlinares.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.ContentType;


public class VerbosTest {
	@Test
	public void deveSalvarUsuario () {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Luan Linares\", \"age\": 32, \"salary\" : 9000 }")
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Luan Linares"))
			.body("age", is(32));
	}
	
	@Test
	public void naoDeveSalvarUsuarioSemNome () {
		given()
		.contentType("application/json")
		.body("{\"age\": 32, \"salary\" : 9000 }")
	.when()
		.post("http://restapi.wcaquino.me/users")
	.then()
		.statusCode(400)
		.body("id", is(nullValue()))
		.body("error", is("Name é um atributo obrigatório"));
	}
	
	@Test
	public void deveSalvarUsuarioViaXML () {
		given()
			.contentType(ContentType.XML)
			.body("<user> <name>Luan Carvalho</name> <age>33</age> <salary>19000</salary> </user>}")
		.when()
			.post("http://restapi.wcaquino.me/usersXML")
		.then()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Luan Carvalho"))
			.body("user.age", is("33"));
    }
	
	
	@Test
	public void deveAlterarUsuario () {
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{\"name\": \"Usuario Alterado\", \"age\": 50, \"salary\" : 9000 }")
		.when()
			.put("http://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario Alterado"))
			.body("age", is(50))
			.body("salary", is(9000));
	}
	
	@Test
	public void devoCustomizarURLParte1 () {
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{\"name\": \"Usuario Alterado\", \"age\": 50, \"salary\" : 9000 }")
		.when()
			.put("http://restapi.wcaquino.me/{entidade}/{userID}", "users", "1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario Alterado"))
			.body("age", is(50))
			.body("salary", is(9000));
	}
	
	@Test
	public void devoCustomizarURLParte2 () {
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{\"name\": \"Usuario Alterado\", \"age\": 50, \"salary\" : 9000 }")
			.pathParam("entidade", "users")
			.pathParam("userId", 1)
		.when()
			.put("http://restapi.wcaquino.me/{entidade}/{userId}")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario Alterado"))
			.body("age", is(50))
			.body("salary", is(9000));
	}
	
	@Test
	public void devoRemoverUsuario () {
		given()
			.log().all()
			.contentType(ContentType.JSON)		
		.when()
			.delete("http://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204);
	}
	
	@Test
	public void naodevoRemoverUsuarioInexistente () {
		given()
			.log().all()
			.contentType(ContentType.JSON)		
		.when()
			.delete("http://restapi.wcaquino.me/users/1000")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexistente"));
	}
	
	@Test
	public void deveSalvarUsuarioUsandoMap () {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Usuario via map");
		params.put("age", 25);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(params)
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via map"))
			.body("age", is(25));
	}
	
	@Test
	public void deveSalvarUsuarioUsandoObjeto () {
		User user = new User("Usuario via Objeto", 35);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via Objeto"))
			.body("age", is(35));
	}
	
	@Test
	public void deveDeserealizarObjetoAoSalvarUsuario () {
		
		User user = new User("Usuario Deserealizado", 45);
		
		User usuarioInserido = given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class);
		System.out.println(usuarioInserido);
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertEquals("Usuario Deserealizado", usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getAge(), is(45));
		
	}
	
	@Test
	public void deveSalvarUsuarioViaXMLUsandoObjeto () {
		User user = new User("Usuario XML", 40);
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("http://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Usuario XML"))
			.body("user.age", is("40"));
    }
	
	@Test
	public void deveDeserealizarXMLAoSalvarUsuario () {
		User user = new User("Usuario Deserealizado XML", 45);
		User usuarioInserido = given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("http://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class);
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertEquals("Usuario Deserealizado XML", usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getAge(), is(45));
		Assert.assertThat(usuarioInserido.getSalary(), nullValue());
		
    }
	
 }
