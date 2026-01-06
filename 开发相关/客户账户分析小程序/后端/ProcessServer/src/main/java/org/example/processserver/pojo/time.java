package org.example.processserver.pojo;

/**
 * @author PY.Lu
 * @date 2024/11/4
 * @Description
 */
public class time {
    String time_InitialProcess;
    String time_IncreProcess;
    String time_buildCSV;
    String time_CSVPersistence;

    public time() {

    }

    public time(String time_InitialProcess, String time_IncreProcess, String time_buildCSV, String time_CSVPersistence) {
        this.time_InitialProcess = time_InitialProcess;
        this.time_IncreProcess = time_IncreProcess;
        this.time_buildCSV = time_buildCSV;
        this.time_CSVPersistence = time_CSVPersistence;
    }

    public String getTime_InitialProcess() {
        return time_InitialProcess;
    }

    public void setTime_InitialProcess(String time_InitialProcess) {
        this.time_InitialProcess = time_InitialProcess;
    }

    public String getTime_IncreProcess() {
        return time_IncreProcess;
    }

    public void setTime_IncreProcess(String time_IncreProcess) {
        this.time_IncreProcess = time_IncreProcess;
    }

    public String getTime_buildCSV() {
        return time_buildCSV;
    }

    public void setTime_buildCSV(String time_buildCSV) {
        this.time_buildCSV = time_buildCSV;
    }

    public String getTime_CSVPersistence() {
        return time_CSVPersistence;
    }

    public void setTime_CSVPersistence(String time_CSVPersistence) {
        this.time_CSVPersistence = time_CSVPersistence;
    }

    @Override
    public String toString() {
        return "time{" +
                "time_InitialProcess='" + time_InitialProcess + '\'' +
                ", time_IncreProcess='" + time_IncreProcess + '\'' +
                ", time_buildCSV='" + time_buildCSV + '\'' +
                ", time_CSVPersistence='" + time_CSVPersistence + '\'' +
                '}';
    }
}
