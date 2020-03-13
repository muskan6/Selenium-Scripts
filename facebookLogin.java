import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

//PROJECT 1
public class facebookLogin extends logindetails {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\MUSKAAN\\Desktop\\chromeDriver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize(); // maximizes the browser window to cover entire screen
		// link to the page your browser should open -> here this link is the pepcoding
		// -> 'posts' section page 
		driver.get("https://www.facebook.com/pg/pepcoding/posts/?ref=page_internal");
		// enter username and password, here the logindetails file keeps the data stored
		// separately to maintain privacy in code
		driver.findElement(By.id("email")).sendKeys(username);
		driver.findElement(By.name("pass")).sendKeys(password);
		// press the login button to login to your account
		driver.findElement(By.id("loginbutton")).click();
		// JavascriptExecutor is a way to run your JavaScript code in your Java while
		// working with Selenium
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// The below script helps you to scroll through the browser body, since facebook
		// needs to load posts
		// you need to scroll down to the end of the page till the elements are loaded,
		// and wait for more elements to load
		try {
			int start = 0;
			int windowHeight = driver.manage().window().getSize().getHeight() - 300; // -300 because the upper part of
																						// the webpage -> header stays
																						// the same, avoid the header in
																						// scroll height
			int newHeight = windowHeight;
			long lastHeight = (long) js.executeScript("return document.body.scrollHeight"); // get the height of the
																							// entire
																							// loaded page
			while (newHeight <= lastHeight) { // while no more page left to load, i.e. all posts are loaded and visited
				// scrollTo(x,y) is a function that lets you scroll to a particular x,y
				// coordinate of the page
				js.executeScript("window.scrollTo(" + start + "," + newHeight + ");");
				// check if the like button is present on the currently visible webpage
				Boolean popUp = driver.findElements(By.xpath("//*[@class=' _6a-y _3l2t  _18vj']")).size() > 0;
				if (!popUp) {
					// element not found
				} else {
					// click the like button
					driver.findElement(By.xpath("//*[@class=' _6a-y _3l2t  _18vj']")).click();
				}
				// update the parameters
				newHeight += windowHeight;
				Thread.sleep(3000);
				// keep updating the length of the posts now loaded
				lastHeight = (long) js.executeScript("return document.body.scrollHeight");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			//quit() is used to close the browser
			driver.quit();
		}
	}

}