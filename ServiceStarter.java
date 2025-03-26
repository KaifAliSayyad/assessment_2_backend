import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ServiceStarter {
    private static final String JAVA_CMD = "java";
    private static final String JAR_OPTION = "-jar";
    private static final int DELAY_BETWEEN_SERVICES = 15; // seconds

    public static void main(String[] args) {
        String baseDir = System.getProperty("user.dir");
        String[] services = {
            "eureka",
            "gateway",
            "history",
            "stock",
            "portfolio",
            "register",
            "trading"
        };

        for (String service : services) {
            startService(baseDir, service);
            System.out.println("Waiting " + DELAY_BETWEEN_SERVICES + " seconds before starting next service...");
            sleep(DELAY_BETWEEN_SERVICES);
        }

        System.out.println("All services have been started!");
    }

    private static void startService(String baseDir, String serviceName) {
        try {
            String jarPath = findJarFile(new File(baseDir + File.separator + serviceName + File.separator + "target"));
            
            if (jarPath == null) {
                System.err.println("JAR file not found for service: " + serviceName);
                return;
            }

            ProcessBuilder processBuilder = new ProcessBuilder(
                JAVA_CMD,
                JAR_OPTION,
                jarPath
            );

            // Set working directory to the service's folder
            processBuilder.directory(new File(baseDir + File.separator + serviceName));
            
            // Redirect error and output streams
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            System.out.println("Starting " + serviceName + " service...");
            Process process = processBuilder.start();

            // // Add shutdown hook to terminate the process when the main program exits
            // Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            //     process.destroy();
            //     System.out.println("Terminated " + serviceName + " service");
            // }));

        } catch (IOException e) {
            System.err.println("Error starting " + serviceName + " service: " + e.getMessage());
        }
    }

    private static String findJarFile(File targetDir) {
        if (!targetDir.exists() || !targetDir.isDirectory()) {
            return null;
        }

        File[] files = targetDir.listFiles((dir, name) -> 
            name.endsWith(".jar") && !name.contains("sources") && !name.contains("javadoc"));

        if (files == null || files.length == 0) {
            return null;
        }

        return files[0].getAbsolutePath();
    }

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}