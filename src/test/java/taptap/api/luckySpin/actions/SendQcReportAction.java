package taptap.api.luckySpin.actions;

import core.Log;
import core.api.API;
import core.api.APIBaseTest;
import io.restassured.response.Response;

public class SendQcReportAction extends APIBaseTest {

    protected final String SEND_QC_REPORT_URL;

    private final long maxResponseTime = 1000L;

    public SendQcReportAction() {
        SEND_QC_REPORT_URL = qcReportHost ;
    }

    private String sendQcReport(String fileName) {
        Log.debug("Start send qc report");

        String filePath = System.getProperty("user.dir") + "/log/qcReport/" + fileName;

        Response response = API.uploadFile(SEND_QC_REPORT_URL, filePath);

        int statusCode = response.statusCode();
        String result = response.body().asString();

        if( API.isStatusCode200(statusCode) && API.isResponseTimeLessThan(response, maxResponseTime) ) {
            Log.highlight("Send qc report success: " + result);
        } else {
            Log.errorAndStop("Send qc report fail: " + result);
        }

        return result;
    }

    public void sendQcReportSuccess(String fileName) {
        String result = sendQcReport(fileName);
    }

}
