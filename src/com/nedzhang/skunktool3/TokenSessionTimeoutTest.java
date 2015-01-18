package com.nedzhang.skunktool3;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.client.ClientProtocolException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.nedzhang.sterlingUtil.SterlingHttpTesterUtil;
import com.nedzhang.util.TextFileUtil;
import com.nedzhang.util.XPathUtil;
import com.nedzhang.util.XmlUtil;

public class TokenSessionTimeoutTest {

	private static final String URL_INTEROP = "http://10.96.87.161:8080/smcfs/interop/InteropHttpServlet";
	
	private static final String USER_NAME = "serviceuser";
	private static final String PASSWORD = "serviceuser";	
	
	private static final String UI_TOKEN = "0vUMlJOMtD6Iw4Sd4RtwSXun9ZIJwvK7hSmiTkqHE2Dsnfpx4t1tEcCjrz3wR5233";
	
	
	/**
	 * @param args
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws XPathExpressionException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IllegalArgumentException
	 */
	public static void main(String[] args) throws ClientProtocolException,
			IOException, XPathExpressionException, IllegalArgumentException,
			ParserConfigurationException, SAXException {



		Runnable tenHourRunner = new Runnable() {

			@Override
			public void run() {
				String tokenLength = "10_hr_token";

				int tokenExpireAfter = 10 * 60 * 60 * 1000; //10 * 60 * 60;  // 0; //36000;

				int runDuration = 24 * 60;
				
				runRepeatTokenTest(tokenLength, null, tokenExpireAfter, runDuration);
			}

		};
		
		
		Runnable defaultRunner = new Runnable() {

			@Override
			public void run() {
				String tokenLength = "default_token";

				int tokenExpireAfter = 0;

				int runDuration = 24 * 60;
				
				runRepeatTokenTest(tokenLength, null, tokenExpireAfter, runDuration);
			}

		};
		
		Runnable fiveMinRunner = new Runnable() {

			@Override
			public void run() {
				String tokenLength = "five_min_token";

				int tokenExpireAfter = 5 * 60 * 1000; //5 * 60;

				int runDuration = 24 * 60;
				
				runRepeatTokenTest(tokenLength, null, tokenExpireAfter, runDuration);
			}

		};

		Runnable uiTokenRunner = new Runnable() {
			
			@Override
			public void run() {
				String tokenLength = "ui_token";

				int tokenExpireAfter = 0;

				int runDuration = 24 * 60;
				
				runRepeatTokenTest(tokenLength, UI_TOKEN, tokenExpireAfter, runDuration);
			}
		};
		
		Thread defaultRunnerThread = new Thread(defaultRunner);
		
		Thread fiveMinRunnerThread = new Thread(fiveMinRunner);
		
		Thread theHourRunThread = new Thread(tenHourRunner);
		
		Thread uiTokenRunnerThread = new Thread(uiTokenRunner);
		
		defaultRunnerThread.start();
		
//		fiveMinRunnerThread.start();
		
		theHourRunThread.start();
		
//		uiTokenRunnerThread.start();
		
		try {
			
			fiveMinRunnerThread.join();
			
			defaultRunnerThread.join();
			
			theHourRunThread.join();
			
			uiTokenRunnerThread.join();
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	private static void runRepeatTokenTest(
			String testName,
			String token,
			int tokenDuration,
			int testDurationInMin) {
		
		String userToken;
		try {
			
			if (token != null && token.length() > 0) {
				userToken = token;
			} else {
				userToken = getAccessToken(USER_NAME, PASSWORD,
					tokenDuration);
			}
			
			for (int i = 0; i < testDurationInMin; i++) {
				String result = testToken(testName, userToken);

				System.out.println(result);

				TextFileUtil.writeTextFile(new File(
						"/home/nzhang/storage/project/american_eagle_outfitters/temp/"
								+ testName + ".txt"), "utf8", true,
						result);

				try {
					Thread.sleep(1 * 60 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String testToken(String tokenLength, String userToken)
			throws ClientProtocolException, IOException {

		// System.out.println(tokenLength + userToken);

		String apiResponse = SterlingHttpTesterUtil.invoke(
				URL_INTEROP,
				USER_NAME, null, userToken, "NedTokenSessionTimeoutTestRun1",
				"getApplicationVersionList", false, "<ApplicationVersion />", null);

		StringBuilder logTextBuilder = new StringBuilder(
				"\n##############################################\n");

		logTextBuilder.append(new Date());
		logTextBuilder.append('\n');
		logTextBuilder.append(tokenLength);
		logTextBuilder.append(": ");
		logTextBuilder.append(userToken);
		logTextBuilder
				.append("\n==============================================\n");
//		if (apiResponse != null && apiResponse.length() > 200) {
//			logTextBuilder.append(apiResponse.substring(0, 199));
//		} else {
			logTextBuilder.append(apiResponse);
//		}
		logTextBuilder
				.append("\n**********************************************\n");

		return logTextBuilder.toString();

	}

	private static String getAccessToken(String loginID, String password,
			int timeOut) throws ClientProtocolException, IOException,
			ParserConfigurationException, SAXException,
			XPathExpressionException {

		StringBuilder apiInputBulder = new StringBuilder("<Login LoginID=\"")
				.append(loginID).append("\" Password=\"").append(password)
				.append("\"");

		if (timeOut > 0) {
			apiInputBulder.append(" Timeout=\"").append(timeOut).append("\"");
		}

		apiInputBulder.append(" />");

		String apiInput = apiInputBulder.toString();

		String response = SterlingHttpTesterUtil.invoke(
				URL_INTEROP,
				loginID, password, null, "NedTokenSessionTimeoutTest",
				"login", false, apiInput, null);

		// System.out.println("*********************************************\nResponse From Server\n-------------------------------------------");
		// System.out.println(response);

		Document responseDoc = XmlUtil.getDocument(response);

		XPathExpression tokenExpression = XPathUtil.getXPathExpression(
				"TokenSessionTimeoutTest->getAccessToken", "/Login");

		Node loginNode = (Node) tokenExpression.evaluate(responseDoc,
				XPathConstants.NODE);

		String userToken = XmlUtil.getAttribute(loginNode, "UserToken");
		return userToken;
	}

}
