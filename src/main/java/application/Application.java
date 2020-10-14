package application;


import application.domain.WaterPipeline;
import application.service.WaterPipelineService;
import application.utils.CommandParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@EnableJpaRepositories
public class Application {

    private static WaterPipelineService waterPipelineService;

    public static void main(String[] args) throws IOException, SQLException, InterruptedException {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        waterPipelineService = context.getBean(WaterPipelineService.class);

        Scanner in = new Scanner(System.in);
        System.out.println("Введите полный путь к файлу с исходными данными ");
        String path = in.nextLine();
        System.out.println("Введите полный путь к файлу с запросами ");
        String fileWithRequests = in.nextLine();
        initWaterPipeline(path);
        List<String> results = executeRequests(fileWithRequests);
        saveResults(results);
    }


    public static void initWaterPipeline(String path) throws IOException {

        System.out.println("Initializing of pipeline...");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path)))) {
            String line;
            //skip headers
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                if (line.matches("\\d+;\\d+;\\d+")) {

                    int[] command = CommandParser.parseCsv(line);
                    waterPipelineService.addWaterPipeline(new WaterPipeline(command[0], command[1], command[2]));
                } else {
                    System.out.println("A line must follow pattern '\\d+;\\d+;\\d+'. The line '" + line + "' will be skipped");
                }
            }
        }
    }


    public static List<String> executeRequests(String fileWithRequests) throws IOException {
        System.out.println("Executing requests...");
        List<String> results = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileWithRequests)))) {
            String line;
            //skip headers
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                if (line.matches("\\d+;\\d+")) {
                    System.out.println("Searching for a segment '" + line + "'");
                    int[] command = CommandParser.parseCsv(line);
                    results.add(waterPipelineService.findWaterPiplinesByPoints(command));
                } else {
                    System.out.println("A line must follow pattern '\\d+;\\d+;\\d+'. The line '" + line + "' will be skipped");
                }
            }
        }
        return results;
    }


    public static void saveResults(List<String> results) throws IOException {

        String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String path = "./RESULTS-" + now + ".csv";
        File file = new File(path);
        file.createNewFile();
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            for (String result : results) {
                result += System.lineSeparator();
                fileWriter.write(result);
            }
            fileWriter.flush();

        }
        System.out.println("The output file is created. " + file.getAbsolutePath());
    }
}
