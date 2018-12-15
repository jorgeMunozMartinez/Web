package edu.uclm.esi.test;

//import static org.junit.Assert.assertTrue;

import java.sql.DriverPropertyInfo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class TestGames {
	
	private WebDriver driverPepe, driverAna;
	
	public TestGames() {
		System.setProperty("webdriver.chrome.driver","D://4º//web/chromedriver.exe");
		System.setProperty("webdriver.gecko.driver","D://4º//web/geckodriver.exe");
	}

	public void SetUp() {
		driverPepe= new ChromeDriver();
		driverAna = new ChromeDriver();
	}
	
	public void testGame()throws Exception {
		login ();
		
		WebElement btnJoinGamePepe=driverPepe.findElement(By.id("btnJoinGame"));
		btnJoinGamePepe.click();
		
		WebElement btnJoinGameAna=driverAna.findElement(By.id("btnJoinGame"));
		btnJoinGameAna.click();
		
		Thread.sleep(1000);
		
		WebElement botonPepe=driverPepe.findElement(By.xpath("//*[@id=\"00\"]"));
		botonPepe.click();
		
		Thread.sleep(100);
		
		WebElement botonAna=driverAna.findElement(By.xpath("//*[@id=\"11\"]"));
		botonAna.click();

	}
	
	private void testGames() throws Exception{
		login();
		
		driverPepe.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[1]/a[3]")).click();
		driverAna.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[2]/a[3]")).click();
		
		driverPepe.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[1]/a[4]")).click();
		driverAna.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[2]/a[4]")).click();
		
		driverPepe.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[1]/a[5]")).click();
		driverAna.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[2]/a[5]")).click();
		
		driverPepe.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[1]/a[6]")).click();
		driverAna.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[2]/a[6]")).click();
		
		Thread.sleep(500);
		String texro="\"winner\":{\"userName\":\"ana\"";
		//assertTrue(driverPepe.getPageSource().contains("texro"));
		//assertTrue(driverAna.getPageSource().contains("texro"));
	}


	private void login() throws Exception{
		driverPepe.get("http://localhost:8080/index.html");
		driverPepe.findElement(By.linkText("Piedra, papel, tijera")).click();
		
		driverAna.get("http://localhost:8080/index.html");
		driverAna.findElement(By.linkText("Piedra, papel, tijera")).click();
		
		driverPepe.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[1]/a[2]")).click();
		driverAna.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[2]/a[2]")).click();
		
		//assertTrue(driverPepe.getPageSource().contains("userName"));
		//assertTrue(driverAna.getPageSource().contains("userName"));
	}
	
	
	
}
