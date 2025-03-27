import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServiceStopper {
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");

    public static void main(String[] args) {
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
            stopService(service);
        }
        System.out.println("All services have been stopped!");
    }

    private static void stopService(String serviceName) {
        try {
            String jarPattern = serviceName + "-0.0.1-SNAPSHOT.jar";
            
            if (IS_WINDOWS) {
                // Windows command to find and kill the process
                ProcessBuilder findProcess = new ProcessBuilder(
                    "cmd", "/c",
                    "wmic", "process", "where",
                    "commandline like '%" + jarPattern + "%'",
                    "get", "processid"
                );

                Process process = findProcess.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                boolean headerSkipped = false;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    // Skip the header line and empty lines
                    if (!headerSkipped) {
                        headerSkipped = true;
                        continue;
                    }
                    if (!line.isEmpty()) {
                        String pid = line;
                        // Kill the process
                        ProcessBuilder killProcess = new ProcessBuilder("taskkill", "/F", "/PID", pid);
                        killProcess.start().waitFor();
                        System.out.println("Stopped " + serviceName + " service (PID: " + pid + ")");
                    }
                }
            } else {
                // Unix/Linux command to find and kill the process
                ProcessBuilder findProcess = new ProcessBuilder(
                    "bash", "-c",
                    "ps aux | grep '" + jarPattern + "' | grep -v grep | awk '{print $2}'"
                );

                Process process = findProcess.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        String pid = line;
                        // Kill the process
                        ProcessBuilder killProcess = new ProcessBuilder("kill", "-9", pid);
                        killProcess.start().waitFor();
                        System.out.println("Stopped " + serviceName + " service (PID: " + pid + ")");
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error stopping " + serviceName + " service: " + e.getMessage());
        }
    }
}