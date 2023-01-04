import java.util.ArrayList;

public class Epic {
    String epicName;
    String epicDescription;
    int epicId;
    Status epicStatus;

    public Epic(String epicName, String epicDescription, Status epicStatus) {
        this.epicName = epicName;
        this.epicDescription = epicDescription;
        this.epicStatus = epicStatus;
    }

    public String getEpicName() {
        return epicName;
    }

    public void setEpicName(String epicName) {
        this.epicName = epicName;
    }

    public String getEpicDescription() {
        return epicDescription;
    }

    public void setEpicDescription(String epicDescription) {
        this.epicDescription = epicDescription;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public Status getEpicStatus() {
        return epicStatus;
    }

    public void setEpicStatus(Status epicStatus) {
        this.epicStatus = epicStatus;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "epicName='" + epicName + '\'' +
                ", epicDescription='" + epicDescription + '\'' +
                ", epicStatus=" + epicStatus +
                '}';
    }
}
