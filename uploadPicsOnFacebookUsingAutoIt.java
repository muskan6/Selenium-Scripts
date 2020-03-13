import java.awt.Desktop.Action;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

//PROJECT 2
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

public class uploadPicsOnFacebookUsingAutoIt extends logindetails {

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\MUSKAAN\\Desktop\\chromeDriver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.facebook.com/");
		driver.findElement(By.id("email")).sendKeys(username);
		driver.findElement(By.name("pass")).sendKeys(password);
		driver.findElement(By.id("loginbutton")).click();
		Thread.sleep(10000);
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Photo/Video"))).click();
		// Run the AutoIt Script
		//Auto It script running -> see readme
		Thread.sleep(8000);
		String command = "C:\\Program Files (x86)\\AutoIt3\\SciTE\\..\\aut2exe\\aut2exe.exe /in C:\\Users\\MUSKAAN\\Desktop\\selenium_upload\\upload_files.au3";
		Runtime.getRuntime().exec(command);
		Thread.sleep(2000);
		Runtime.getRuntime().exec("C:\\Users\\MUSKAAN\\Desktop\\selenium_upload\\upload_files.exe");
		ArrayList<String> names = new ArrayList<>();
		try {
		      File myObj = new File("C:\\Users\\MUSKAAN\\Desktop\\selenium_upload\\tagnames.txt");
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        names.add(data);
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("Couldnt find file.");
		      e.printStackTrace();
		    }
		Thread.sleep(5000);
		for (int i = 0; i < names.size(); i++) {
			WebElement tagbox = null;
			tagbox = driver.findElement(By.xpath(
					"/html/body/div[1]/div[3]/div[1]/div/div[2]/div[2]/div[1]/div[2]/div/div[3]/div/div/div[2]/div[1]/div/div/div/div[2]/div/div[1]/div[2]/div[1]/div/table/tbody/tr/td[2]/div/div/span[2]/label/input"));
			tagbox.sendKeys(names.get(i));
			Thread.sleep(1000);
			tagbox.sendKeys(Keys.ENTER);
		}
		Thread.sleep(10000);
		// By.cssSelector("div[role='textbox']")
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button/span[.=\"Post\"]"))).click();
	}
}

// class name for Tag friends - _m_1 _1pn-
/*
 * try { wait = new WebDriverWait(driver, 300); boolean found =
 * wait.until(ExpectedConditions.elementToBeSelected(By.xpath(
 * "/html/body/div[1]/div[3]/div[1]/div/div[2]/div[2]/div[1]/div[2]/div/div[3]/div/div/div[2]/div[1]/div/div/div/div[2]/div/div[1]/div[2]/div[1]/div/table/tbody/tr/td[2]/div/div/span[2]/label/input"
 * ))); if(found) { tagbox = driver.findElement(By.xpath(
 * "/html/body/div[1]/div[3]/div[1]/div/div[2]/div[2]/div[1]/div[2]/div/div[3]/div/div/div[2]/div[1]/div/div/div/div[2]/div/div[1]/div[2]/div[1]/div/table/tbody/tr/td[2]/div/div/span[2]/label/input"
 * )); tagbox.sendKeys(name); Thread.sleep(1000); tagbox.sendKeys(Keys.ENTER); }
 * }finally{ System.out.println("not found box"); }
 */
