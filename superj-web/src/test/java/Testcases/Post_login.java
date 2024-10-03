package Testcases;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.poi.hssf.record.PageBreakRecord.Break;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import Baseclass.Basetest;

public class Post_login extends Basetest {

    // Generate random mobile number
    public static String randomMobileNumber() {
        Random generator = new Random();
        generator.setSeed(System.currentTimeMillis());
        int num1 = generator.nextInt(900) + 100;
        int num2 = generator.nextInt(9000000) + 1000000;
        return "99" + num1 + num2;
    }

    // Generate a random 6-digit OTP
    public static String generateRandomOTP() {
        Random rand = new Random();
        int otp = rand.nextInt(900000) + 100000;
        return String.valueOf(otp);
    }

    // Enter OTP method
    public void enterOTP(String otp) throws Exception {
        for (int i = 0; i < otp.length(); i++) {
            String locator = prop.getProperty("OTP_" + (i + 1));
            driver.findElement(By.xpath(locator)).sendKeys(Character.toString(otp.charAt(i)));
        }
    }

    // Check if Home Page text is displayed
    public boolean isHomePageTextDisplayed() {
        try {
            return driver.findElement(By.xpath(prop.getProperty("Homepagetext"))).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Scroll to survey cards
    public void scrollToSurveyCards() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement surveyCardsElement = driver.findElement(By.xpath(prop.getProperty("Survey_cards")));

        while (!surveyCardsElement.isDisplayed()) {
            js.executeScript("window.scrollBy(0, 250);");
            Thread.sleep(500);
        }
    }
    
 // Helper method to check if element is present
    public boolean isElementPresent(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Helper method to select dropdown options
    private void selectDropdownOption(String clickXpath, String optionXpath) throws InterruptedException {
        driver.findElement(By.xpath(prop.getProperty(clickXpath))).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath(prop.getProperty(optionXpath))).click();
        Thread.sleep(2000);
    }

    // Login process
    private void loginProcess(String randomMobileNumber, String otp) throws Exception {
        driver.findElement(By.xpath(prop.getProperty("Login"))).click();
        driver.findElement(By.xpath(prop.getProperty("Mobilenumber"))).sendKeys(randomMobileNumber);
        Thread.sleep(1000);
        driver.findElement(By.xpath(prop.getProperty("Mobilenumber_Continue"))).click();
        enterOTP(otp);
        driver.findElement(By.xpath(prop.getProperty("OTP_Continue"))).click();
        Thread.sleep(2500);
    }

    @Test(priority = 1, enabled = true, retryAnalyzer = Retry.class)
    public void verify_Mobile_OTP_HomePage() throws Exception {
        String randomMobileNumber = randomMobileNumber();
        String otp = "777777";
        loginProcess(randomMobileNumber, otp);
        
        // Validate if homepage text is displayed
        Boolean isHomePageTextDisplayed = isHomePageTextDisplayed();
        Assert.assertTrue(isHomePageTextDisplayed, "Homepage text is not displayed.");
        scrollToSurveyCards();

        // Fetch and validate survey cards
        List<WebElement> surveyCards = driver.findElements(By.xpath(prop.getProperty("Survey_cards")));
        int totalCards = surveyCards.size();
        System.out.println("Total Completed number of cards: " + totalCards);
        Assert.assertTrue(totalCards > 0, "No completed survey cards found.");
    }

    @Test(priority = 2, enabled = true, retryAnalyzer = Retry.class)
    public void check_transactioncoupons() throws Exception {
        String randomMobileNumber = randomMobileNumber();
        String otp = "777777";
        loginProcess(randomMobileNumber, otp);
        
        // Select required options (gender, year, location)
        selectDropdownOption("Click_Gender", "Male");
        selectDropdownOption("Click_Gender", "2002");
        selectDropdownOption("Click_Gender", "Adilabad1");

        // Select and collect onboarding coupons
        driver.findElement(By.xpath(prop.getProperty("Coupon_1"))).click();
        driver.findElement(By.xpath(prop.getProperty("Coupon_2"))).click();
        Thread.sleep(3000);
        driver.findElement(By.xpath(prop.getProperty("Collect_Onboarding_coupons"))).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath(prop.getProperty("Earn_more_rewards!"))).click();
        Thread.sleep(1000);

        // Navigate to Profile and view transaction coupons
        driver.findElement(By.xpath(prop.getProperty("Profile"))).click();
        driver.findElement(By.xpath(prop.getProperty("view_all"))).click();

        List<WebElement> transactionCoupons = driver.findElements(By.xpath(prop.getProperty("Transaction_Coupons")));
        int transactionCount = transactionCoupons.size();
        System.out.println("Number of Transaction Coupons: " + transactionCount);
        
        Assert.assertTrue(transactionCount > 0, "No transaction coupons found.");
        for (WebElement coupon : transactionCoupons) {
            System.out.println("Coupon Details: " + coupon.getText());
        }
    }

    @Test(priority = 3, enabled = true, retryAnalyzer = Retry.class)
    public void Wallet_transaction() throws Exception {
        String randomMobileNumber = randomMobileNumber();
        String otp = "777777";
        loginProcess(randomMobileNumber, otp);
        
        // Select required options (gender, year, location)
        selectDropdownOption("Click_Gender", "Male");
        selectDropdownOption("Click_Gender", "2002");
        selectDropdownOption("Click_Gender", "Adilabad1");

        // Select and collect onboarding coupons
        driver.findElement(By.xpath(prop.getProperty("Coupon_1"))).click();
        driver.findElement(By.xpath(prop.getProperty("Coupon_2"))).click();
        Thread.sleep(3000);
        driver.findElement(By.xpath(prop.getProperty("Collect_Onboarding_coupons"))).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath(prop.getProperty("Earn_more_rewards!"))).click();
        Thread.sleep(1000);

        // Navigate to Wallet and verify coupons
        driver.findElement(By.xpath(prop.getProperty("Wallet"))).click();
        List<WebElement> walletCoupons = driver.findElements(By.xpath(prop.getProperty("Recent_coupons")));
        int walletCount = walletCoupons.size();
        System.out.println("Number of Wallet Coupons: " + walletCount);

        // Navigate to Profile and check transaction coupons count
        driver.findElement(By.xpath(prop.getProperty("Profile"))).click();
        driver.findElement(By.xpath(prop.getProperty("view_all"))).click();
        List<WebElement> transactionCoupons = driver.findElements(By.xpath(prop.getProperty("Transaction_Coupons")));
        int transactionCount = transactionCoupons.size();
        System.out.println("Number of Transaction Coupons: " + transactionCount);

        // Validate that wallet coupons and transaction coupons count are equal
        Assert.assertEquals(walletCount, transactionCount, "Wallet coupons count does not match transaction coupons count.");
    }
    
    @Test(priority = 4, enabled = true, retryAnalyzer = Retry.class)
    public void check_transactincoupons() throws Exception {
        String randomMobileNumber = randomMobileNumber();
        String otp = "777777";
        loginProcess(randomMobileNumber, otp);
        
        // Navigate to Profile
        driver.findElement(By.xpath(prop.getProperty("Profile"))).click();
        Thread.sleep(1000);
        
        // Check and click 'Learn more about DID' and verify its presence
        if (isElementPresent(By.xpath(prop.getProperty("Learn_more_about_DID")))) {
            driver.findElement(By.xpath(prop.getProperty("Learn_more_about_DID"))).click();
            driver.findElement(By.xpath(prop.getProperty("DID_back"))).click();
        } else {
            System.out.println("'Learn more about DID' option is not available");
        }
        
        Thread.sleep(1500);
        // Check and click Privacy Policy
        if (isElementPresent(By.xpath(prop.getProperty("Click_privacypolicy")))) {
            driver.findElement(By.xpath(prop.getProperty("Click_privacypolicy"))).click();
            driver.findElement(By.xpath(prop.getProperty("Back_to_Profile"))).click();
        } else {
            System.out.println("Privacy Policy link is not available");
        }
        
               
        Thread.sleep(1500);
        // Check and click 'Help & Support'
        if (isElementPresent(By.xpath(prop.getProperty("Help_support")))) {
            driver.findElement(By.xpath(prop.getProperty("Help_support"))).click();
            driver.findElement(By.xpath(prop.getProperty("Back_to_Profile01"))).click();
        } else {
            System.out.println("'Help & Support' option is not available");
        }
    }

	    @Test(priority = 5, enabled = true, retryAnalyzer = Retry.class)
		public void Complete_onboardin_checking_coupons() throws Exception {
			String randomMobileNumber = randomMobileNumber();
			String otp = "777777";
			loginProcess(randomMobileNumber, otp);
	
			selectDropdownOption("Click_Gender", "Male");
			selectDropdownOption("Click_Gender", "2002");
			selectDropdownOption("Click_Gender", "Adilabad1");
	
			// Select and collect onboarding coupons
			driver.findElement(By.xpath(prop.getProperty("Coupon_1"))).click();
			driver.findElement(By.xpath(prop.getProperty("Coupon_2"))).click();
			Thread.sleep(3000);
			driver.findElement(By.xpath(prop.getProperty("Collect_Onboarding_coupons"))).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath(prop.getProperty("Earn_more_rewards!"))).click();
			Thread.sleep(1000);
	
			// Navigate to Wallet and verify coupons
			driver.findElement(By.xpath(prop.getProperty("Wallet"))).click();
			driver.findElement(By.xpath(prop.getProperty("Cashout"))).click();
	
			// Find all wallet gift cards
			List<WebElement> walletCoupons = driver.findElements(By.xpath(prop.getProperty("Giftcards")));
	
			// Click each coupon based on the number of gift cards found
			int totalCoupons = walletCoupons.size();
			System.out.println("Total number of gift cards: " + totalCoupons);
	
			// Loop through the list and click on each coupon
			for (int i = 0; i < totalCoupons; i++) {
				walletCoupons.get(i).click();
				Thread.sleep(1000);
			}
			System.out.println("Clicked all available gift cards.");
		}
    
	    @Test
	    public void Wallet_Balance () throws Exception{
	    	String randomMobileNumber = randomMobileNumber();
	    	String otp = "777777";
	    	loginProcess(randomMobileNumber, otp);
	    	
	    	//navigate to Wallet
	    	driver.findElement(By.xpath(prop.getProperty("Wallet"))).click();
	        Thread.sleep(1000);
	        
	        //Click On Cash out.
	        driver.findElement(By.xpath(prop.getProperty("Cashout"))).click();
	        Thread.sleep(1000);
	        
	        //Number of times to click the element
	        int numberofclicks = 5;
	        for (int i = 0; i< numberofclicks; i++) {
	        	try {
					WebElement giftcardclicks = driver.findElement(By.xpath(prop.getProperty("Clickon_Giftcards")));
					giftcardclicks.click();
				} catch (Exception e) {
					break;
				}
				
				
			}
	        
	    }
	    
}

