package taptap.api.luckySpin.test;

import core.api.APIBaseTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import taptap.api.luckySpin.actions.SendQcReportAction;

public class SendQcReport extends APIBaseTest {

    SendQcReportAction sendQcReportAction;

    String qcReportFile = "qc-report.zip";

    @BeforeTest
    public void initTest() {
        sendQcReportAction = new SendQcReportAction();
    }

    @Test
    public void sendQcReport() {

        sendQcReportAction.sendQcReportSuccess(qcReportFile);
    }
}
