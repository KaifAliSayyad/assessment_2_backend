import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class ServiceStarter {
    private static final String JAVA_CMD = "java";
    private static final String JAR_OPTION = "-jar";
    private static final int DELAY_BETWEEN_SERVICES = 0; // seconds
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");

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
            if (hasChangesInServiceDirectory(baseDir, service)) {
                System.out.println("Changes detected in " + service + " directory. Building...");
                createJarFile(baseDir, service);
            }
            startService(baseDir, service);
            System.out.println("Waiting " + DELAY_BETWEEN_SERVICES + " seconds before starting next service...");
            sleep(DELAY_BETWEEN_SERVICES);
        }

        System.out.println("All services have been started!");
    }

    private static boolean hasChangesInServiceDirectory(String baseDir, String serviceName) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                "git",
                "status",
                "--porcelain",
                serviceName + "/"
            );
            processBuilder.directory(new File(baseDir));
            Process process = processBuilder.start();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean hasChanges = false;
            
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    hasChanges = true;
                    break;
                }
            }
            
            process.waitFor();
            return hasChanges;
            
        } catch (IOException | InterruptedException e) {
            System.err.println("Error checking git status for " + serviceName + ": " + e.getMessage());
            return false;
        }
    }

    private static void startService(String baseDir, String serviceName) {
        try {
            String jarPath = findJarFile(new File(baseDir + File.separator + serviceName + File.separator + "target"));
            
            if (jarPath == null) {
                System.err.println("JAR file not found for service: " + serviceName);
                System.out.println("Building jar file for this service");
                boolean jarCreated = createJarFile(baseDir, serviceName);
                if(!jarCreated){
                    return;
                }else{
                    jarPath = findJarFile(new File(baseDir + File.separator + serviceName + File.separator + "target"));
                }
            }

            ProcessBuilder processBuilder = new ProcessBuilder(
                JAVA_CMD,
                JAR_OPTION,
                jarPath
            );

            processBuilder.directory(new File(baseDir + File.separator + serviceName));
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            System.out.println("Starting " + serviceName + " service...");
            Process process = processBuilder.start();

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

    private static boolean createJarFile(String baseDir, String serviceName) {
        try {
            String mvnwCommand = IS_WINDOWS ? 
                baseDir + File.separator + serviceName + File.separator + "mvnw.cmd" :
                baseDir + File.separator + serviceName + File.separator + "mvnw";

            ProcessBuilder processBuilder = new ProcessBuilder(
                mvnwCommand,
                "clean",
                "package",
                "-DskipTests"
            );

            processBuilder.directory(new File(baseDir + File.separator + serviceName));
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);  
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            
            System.out.println("Building jar file for " + serviceName + " service...");
            Process process = processBuilder.start();
            boolean completed = process.waitFor(10, TimeUnit.MINUTES);
            
            if (!completed) {
                System.err.println("Build timed out for " + serviceName);
                process.destroy();
                return false;
            }
            
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            System.err.println("Error building jar file for " + serviceName + " service: " + e.getMessage());
            return false;
        }
    }
}

