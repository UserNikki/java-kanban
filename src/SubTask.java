public class SubTask extends Epic {


    public SubTask( String epicName, String epicDescription, Status epicStatus) {
        super(epicName, epicDescription, epicStatus);
    }


    @Override
    public String toString() {
        return "SubTask{" +
                "epicName='" + epicName + '\'' +
                ", epicDescription='" + epicDescription + '\'' +
                ", epicStatus=" + epicStatus +
                '}';
    }


    @Override
    public String getEpicName() {
        return super.getEpicName();
    }

    @Override
    public void setEpicName(String epicName) {
        super.setEpicName(epicName);
    }

    @Override
    public String getEpicDescription() {
        return super.getEpicDescription();
    }

    @Override
    public void setEpicDescription(String epicDescription) {
        super.setEpicDescription(epicDescription);
    }

    @Override
    public int getEpicId() {
        return super.getEpicId();
    }

    @Override
    public void setEpicId(int epicId) {
        super.setEpicId(epicId);
    }

    @Override
    public Status getEpicStatus() {
        return super.getEpicStatus();
    }

    @Override
    public void setEpicStatus(Status epicStatus) {
        super.setEpicStatus(epicStatus);
    }
}
