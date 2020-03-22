import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class whatsappBroadcast {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\MUSKAAN\\Desktop\\chromeDriver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("https://web.whatsapp.com/");
		// This timeout is included so that you can login using the QR code in watsapp
		// implicit wait is used so that the process starts once you login
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		// keep keyword as the value with which the groups you need to send message to start with.
		String keyword = "Important";
		// keep message as the message you want to send.
		String message = "Your message was sent using selenium";
		// access all the chats, this will take you to the section where all chats are displayed.
		driver.findElement(By.cssSelector("#pane-side > div:nth-child(1) > div > div"));
		// Java script executor is used to java script code with java.
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// This set is used so that same message is not sent multiple times to a specific person.
		HashSet<String> setofnames = new HashSet<>();
		try {
			// keeping the max element to be at index 0 initially, update when found an element with height greater than this.
			int maxindex = 0;
			// while there are elements in the active contacts list in your watsapp.
			while (true) {
				// At one time, web.whatsapp shows only 16 chats as web elements, each having common identifier '.X7YrQ' and have the transform: translateY property different.
				// We are first using the javascript to get all the 16 accessible web elements, and iterating over each.
				// If the contact name starts with your given keyword, it sends it a message. 
				List<WebElement> elements = (List<WebElement>) js.executeScript("var elements = document.querySelectorAll('.X7YrQ'); return elements;");
				for(int i=0; i<elements.size(); i++) {
					// get the current element
					WebElement ele = elements.get(i);
					// get the text of the element. For eg: Name_of_contact: Pep coding batch 1, text_in_the_last_msg: Pep coding has a new online batch.
					// So the text will have "Pep coding
					//						  Pep coding has a new online batch." 
					// You only need the name of contact. Hence the following processing is for getting the name.
					String text = ele.getText();
					int k = 0;
					while (k < text.length() && text.charAt(k) != '\n') {
						k++;
					}
					String nameofcontact = ele.getText().substring(0, k);
					// The next line is just used to print the names of contacts -> just for check. 
					System.out.println(nameofcontact);
					// if the nameofcontact starts with the keyword you want, click and send message.
					// setofnames is used to not send the same msg to the same contact more than once.
					if (nameofcontact.startsWith(keyword) && !setofnames.contains(nameofcontact)) {
						setofnames.add(nameofcontact);
						// click on the web element to open chat.
						ele.click();
						// click on the message box and use sendKeys to send the message.
						driver.findElement(
								By.xpath("/html/body/div[1]/div/div/div[4]/div/footer/div[1]/div[2]/div/div[2]"))
								.sendKeys(message);
						// click on the send button to send the message.
						driver.findElement(By.xpath("/html/body/div[1]/div/div/div[4]/div/footer/div[1]/div[3]/button"))
								.click();
						// This sleep is used so that we can see properly how the message has been sent to the contact.
						Thread.sleep(2000);
					}
				}
				
				// Get the web elements again, and get the element with the maximum translateY property, in the transform section.
				elements = (List<WebElement>) js.executeScript("var elements = document.querySelectorAll('.X7YrQ'); return elements;");
				for (int i = 0; i < elements.size(); i++) {
					// get height of current element -> gives translateY of current element as string.
					String currheight = elements.get(i).getCssValue("transform");
					// get the double value out of string.
					double ch = Double.parseDouble(currheight.substring(22, currheight.length() - 1));
					// compare it with the height of max height element
					String maxeleheight = elements.get(maxindex).getCssValue("transform");
					double mh = Double.parseDouble(maxeleheight.substring(22, maxeleheight.length() - 1));
					// if current element has height greater than the max element height till now, update max.
					if (mh < ch) {
						maxindex = i;
					}
				}
				// scrollToView is used to scroll the page till that element appears on the screen.
				js.executeScript("var elements = document.querySelectorAll('.X7YrQ');elements[" + maxindex + "].scrollIntoView();");
				// This sleep is used so that we can see the scroll properly.
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.quit();
		}
	}
}


/*
 String datastring = driver.findElement(By.cssSelector("#pane-side > div:nth-child(1) > div > div")).getAttribute("style");
		//String heightstring = datastring.substring(8);
		//int height = Integer.parseInt(heightstring.substring(0, heightstring.length() - 3)); */
