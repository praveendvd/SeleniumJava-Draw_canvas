package test_suites;


import java.io.File;
import java.io.IOException;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;



public class Testcase1 {

	WebDriver driver;

	@BeforeMethod
	public void setup() {

		System.setProperty("webdriver.chrome.driver", "D:/selenium-draw-canvas/Selenium_basic/src/files/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();

	}

	@Test(description = "Test case 1 assert True")
	public void loginPageTitleTest2() throws InterruptedException, IOException {
		
		Actions builder = new Actions(driver);
		Action drawAction;
		
		//Create Float array, as getPixel returns a float array containing RGB decimal values
		//you can understand RGB decimal value using http://doc.instantreality.org/tools/color_calculator/
		//for black each r ,b and g will be 0.0
		Float[] p1;
	    
		//read the image file into openimaj
		MBFImage image = ImageUtilities.readMBF(new File("D:\\red.png"));
		
		//detect the edges using canny
		image.processInplace(new CannyEdgeDetector());
		
		//You can display it to see how the edge transformation happened
		DisplayUtilities.display(image);

		// get image width and height
		int width = image.getWidth();
		int height = image.getHeight();

		//Open the canvas and close all pop ups
		driver.get("https://vrobbi-nodedrawing.herokuapp.com/");
		Thread.sleep(6000);

		
		WebElement canvas = driver.findElement(By.cssSelector("canvas#paper"));
		
		//get canvas elements centre location
		int xc = canvas.getRect().getWidth() / 2;
		int yc = canvas.getRect().getHeight() / 2;

		//Now we know that our image has a area of width * height, now we go through each pixel in this area
		for (int i = 0; i < width; i++) {
			for (int i2 = 0; i2 < height; i2++) {
				
				//Get RGB decimal value for the pixel
				p1 = image.getPixel(i, i2);

				//Edge detected will be white and everything else will be black, so we are making sure RGB value is not zero ie not Black
				if ((p1[0] + p1[1] + p1[2]) != 0) {
					
					//if not black just click and drag to x,y to x+1,y ( so that we will have a visible line)
					
					
					
					//note that we are using -xc + 300 and -yc+100 . as moveToelement considers offset from the center of the element
					// so when we do -xc and =yc the offset resets to top left edge , now we add + 300 and +100 .
					// THis makes the location 300 pixels from left and 100 pixels from top 
					// This is constant value and we use this as the boarder for our drawing
					// now from this reference location we will draw points at +i and +i2
					builder = new Actions(driver);
					drawAction = builder.moveToElement(canvas, -xc + 300 + i, -yc + 100 + i2) // start points x
																										// axis and y
																										// axis.
							.clickAndHold().moveToElement(canvas, -xc + 300 + i + 1, -yc + 100 + i2).release() // 2nd
																												// points
																												// (x1,y1)
							.build();
					drawAction.perform();

				}

			}
		}

		Thread.sleep(6000);
	}

	@AfterMethod
	public void tearDown() {
		driver.quit();
	}

}
