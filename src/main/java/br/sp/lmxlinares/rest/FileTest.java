package br.sp.lmxlinares.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

public class FileTest {

	@Test
	public void deveObrigarEnvioDeArquivo () {
		given()
			.log().all()
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404)
			.body("error", is("Arquivo não enviado"));
	}
	
	@Test
	public void deveFazerUploadArquivo () {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/users.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("users.pdf"));
	}
	
	@Test
	public void naoDeveFazerUploadArquivoGrande () {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/ArquivoGrande.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.time(lessThan(7000L))
			.statusCode(413)
			;
	}
	
	@Test
	public void deveFazerDownloadArquivo () throws IOException {
		byte[] image = given()
		.log().all()
	.when()
		.get("http://restapi.wcaquino.me/download")
	.then()
		.statusCode(200)
		.extract().asByteArray();
	File imagem = new File("src/main/resources/file.jpg");
	OutputStream out = new FileOutputStream(imagem);
	out.write(image);
	out.close();
	
	System.out.println(imagem.length());
	assertThat(imagem.length(), lessThan(100000L));
	}
	
}
