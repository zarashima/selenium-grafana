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
		this.postTestBrowser(iTestResult.getTestContext());
	}

	@Override
	public void onTestFailure(ITestResult iTestResult) {
		this.postTestMethodStatus(iTestResult, "FAIL");
		this.postTestBrowser(iTestResult.getTestContext());
	}

	@Override
	public void onTestSkipped(ITestResult iTestResult) {
		this.postTestMethodStatus(iTestResult, "SKIPPED");
		this.postTestBrowser(iTestResult.getTestContext());
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
		Point point = Point.measurement("testmethod").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				.tag("testclass", iTestResult.getTestClass().getName()).tag("name", iTestResult.getName())
				.tag("description", iTestResult.getMethod().getDescription()).tag("result", status)
				.addField("duration", (iTestResult.getEndMillis() - iTestResult.getStartMillis())).build();
		UpdateResults.post(point);
	}

	private void postTestClassStatus(ITestContext iTestContext) {
		Point point = Point.measurement("testclass").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				.tag("name", iTestContext.getAllTestMethods()[0].getTestClass().getName())
				.addField("duration", (iTestContext.getEndDate().getTime() - iTestContext.getStartDate().getTime()))
				.build();
		UpdateResults.post(point);
	}

	private void postTestBrowser(ITestContext iTestContext) {
		Point point = Point.measurement("browser").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				.tag("browser", ((RemoteWebDriver) iTestContext.getAttribute("driver")).getCapabilities().getBrowserName())
				.addField("version", ((RemoteWebDriver) iTestContext.getAttribute("driver")).getCapabilities().getVersion())
				.build();
		UpdateResults.post(point);
	}

}
