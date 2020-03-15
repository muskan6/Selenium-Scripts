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
		// Auto It script running -> see readme.md to know about AutoIt installation
		Thread.sleep(8000); // You need to add this sleep to close any popups that appear on the screen.
		// This command is used to run a file and compile it using command line -> here
		// i have created a scite file named "upload_files.au3" in the respective path
		// folders as shown.
		// After you install the AutoIt software into your system, the code is compiled
		// using this path in your system(path to aut2exe.exe)
		// -> C:\\Program Files (x86)\\AutoIt3\\SciTE\\..\\aut2exe\\aut2exe.exe
		String command = "C:\\Program Files (x86)\\AutoIt3\\SciTE\\..\\aut2exe\\aut2exe.exe /in C:\\Users\\MUSKAAN\\Desktop\\selenium_upload\\upload_files.au3";
		// exec(command) function helps in executing the command line command at runtime, which helps in compiling the .au3 script file.
		Runtime.getRuntime().exec(command);
		Thread.sleep(2000); // this sleep is temporary and avoidable, but you need some explicit wait to have the compiled upload_files.exe file in your system
		// executing the AutoIt file executes the file which adds all the paths of the image files to the non-browser file name input box.
		Runtime.getRuntime().exec("C:\\Users\\MUSKAAN\\Desktop\\selenium_upload\\upload_files.exe");
		//If you have a file that tells you who you need to tag on with the post, read the file and add all its names into the names list.
		ArrayList<String> names = new ArrayList<>();
		try {
			//Path of file i created having the names of people to be tagged -> tagnames.txt
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
		Thread.sleep(5000); // this sleep is editable/avoidable depending on how long your browser takes to load all the images.
		for (int i = 0; i < names.size(); i++) {
			WebElement tagbox = null;
			tagbox = driver.findElement(By.xpath(
					"/html/body/div[1]/div[3]/div[1]/div/div[2]/div[2]/div[1]/div[2]/div/div[3]/div/div/div[2]/div[1]/div/div/div/div[2]/div/div[1]/div[2]/div[1]/div/table/tbody/tr/td[2]/div/div/span[2]/label/input"));
			tagbox.sendKeys(names.get(i));
			Thread.sleep(1000); //only for better visibility of how the tagging is happening
			tagbox.sendKeys(Keys.ENTER);
		}
		Thread.sleep(2000); //for visibility
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button/span[.=\"Post\"]"))).click(); // post the post!!
	}
}

