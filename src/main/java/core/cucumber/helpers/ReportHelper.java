package core.cucumber.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.masterthought.cucumber.*;
import net.masterthought.cucumber.presentation.PresentationMode;
import net.masterthought.cucumber.sorting.SortingMethod;

public class ReportHelper{

    public static void generateCucumberReport() {
        File reportOutputDirectory = new File("target");
        List<String> jsonFiles = new ArrayList<>();
        String path = System.getProperty("user.dir") + File.separator + "target" +
                File.separator + "cucumber-reports" + File.separator;
        jsonFiles.add(path + "cucumber.json");

        String projectName = "guardian";

        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        configuration.setSortingMethod(SortingMethod.NATURAL);
        configuration.addPresentationModes(PresentationMode.EXPAND_ALL_STEPS);

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();
    }

}