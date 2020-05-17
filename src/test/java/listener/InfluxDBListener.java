package listener;

import org.influxdb.dto.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.UpdateResults;

import java.util.concurrent.TimeUnit;

public class InfluxDBListener implements ITestListener {


	@Override
	public void onTestStart(ITestResult iTestResult) {
	}

	@Override
	public void onTestSuccess(ITestResult iTestResult) {
		this.postTestMethodStatus(iTestResult, "PASS");
	}

	@Override
	public void onTestFailure(ITestResult iTestResult) {
		this.postTestMethodStatus(iTestResult, "FAIL");
	}

	@Override
	public void onTestSkipped(ITestResult iTestResult) {
		this.postTestMethodStatus(iTestResult, "SKIPPED");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
	}

	@Override
	public void onStart(ITestContext iTestContext) {
	}

	@Override
	public void onFinish(ITestContext iTestContext) {
		this.postTestClassStatus(iTestContext);
	}

	private void postTestMethodStatus(ITestResult iTestResult, String status) {
		String executedBrowser = String.format("%s_%s", ((RemoteWebDriver) iTestResult.getTestContext().getAttribute("driver")).getCapabilities().getBrowserName()
				, ((RemoteWebDriver) iTestResult.getTestContext().getAttribute("driver")).getCapabilities().getVersion());
		String errorMessage = "";
		if (!status.equals("PASS"))
			errorMessage = iTestResult.getThrowable().getMessage();
		Point point = Point.measurement("testmethod").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				.tag("testclass", iTestResult.getTestClass().getName())
				.tag("name", iTestResult.getName())
				.tag("description", iTestResult.getMethod().getDescription())
				.tag("error", errorMessage)
				.tag("browser", executedBrowser)
				.tag("result", status)
				.addField("duration", (iTestResult.getEndMillis() - iTestResult.getStartMillis()))
				.build();
		UpdateResults.post(point);
	}

	private void postTestClassStatus(ITestContext iTestContext) {
		Point point = Point.measurement("testclass").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				.tag("name", iTestContext.getAllTestMethods()[0].getTestClass().getName())
				.addField("duration", (iTestContext.getEndDate().getTime() - iTestContext.getStartDate().getTime()))
				.build();
		UpdateResults.post(point);
	}

}
