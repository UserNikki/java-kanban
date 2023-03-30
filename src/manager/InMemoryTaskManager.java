package manager;
import task.Epic;
import task.SubTask;
import task.Task;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    public Map<Integer,Task> taskMap = new HashMap<>();
    public Map<Integer,Epic> epicMap = new HashMap<>();
    public Map<Integer, SubTask> subTaskMap = new HashMap<>();
    private final Comparator<Task> comparator = Comparator.comparing(Task::getStartTime);
    protected Set<Task> taskSet = new TreeSet<>(comparator);
    private int id = 1;
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    public HistoryManager getHistory() {
         return historyManager;
    }



    @Override
    public Epic createEpic(Epic epic) {
        if (timeValidator(epic)) {
            int epicId = generateId();
                epic.setTaskId(epicId);
                    epic.setStatus(Task.Status.NEW);
                        epicMap.put(epicId, epic);
                            countEpicEndTime(epic.getTaskId());
                                taskSet.add(epic);
                            } else {
                        System.out.println("Указанное время начала совпадает с уже существующей задачей");
                        }
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subtask) {
        if (timeValidator(subtask)) {
            int subtaskId = generateId();
                subtask.setTaskId(subtaskId);
                    subTaskMap.put(subtaskId, subtask);
                        taskSet.add(subtask);
        if (epicMap.containsKey(subtask.getEpicId())) {
            epicMap.get(subtask.getEpicId()).setSubTaskIdList(subtask.getTaskId());
                countEpicStatus(subtask.getEpicId());
                    countEpicStartTime(subtask.getEpicId());
                        countEpicDuration(subtask.getEpicId());
                            countEpicEndTime(subtask.getEpicId());
        } else {
            System.out.println("Epic does not exist");
        }
    }
        else {
            System.out.println("Указанное время начала совпадает с уже существующей задачей");
        }
        return subtask;
    }

    private void countEpicStatus(int epicId) {
        boolean isDone = false;
        boolean isNew = false;
        for (SubTask subtask : subTaskMap.values()) {
            if (subtask.getEpicId() == epicId) {
                if (epicMap.containsKey(epicId)) {
                    if (subtask.getStatus().equals(Task.Status.DONE)) {
                        epicMap.get(epicId).setStatus(Task.Status.DONE);
                        isDone = true;
                    }
                    if (subtask.getStatus().equals(Task.Status.NEW)) {
                        epicMap.get(epicId).setStatus(Task.Status.NEW);
                        isNew = true;
                    }
                    if (isNew == isDone) {
                        epicMap.get(epicId).setStatus(Task.Status.IN_PROGRESS);
                    }
                }
                else {
                    System.out.println("Epic does not exist...");
                }
            }
        }

    }

    @Override
    public Task createTask(Task task) {
        if (timeValidator(task)) {
            int taskId = generateId();
                task.setTaskId(taskId);
                    taskMap.put(taskId, task);
                        taskSet.add(task);
        }
        else {
            System.out.println("Указанное время начала совпадает с уже существующей задачей");
        }
        return task;
    }

    public void countEpicDuration(int id) {
       List<Integer> list = epicMap.get(id).getSubTaskIdList();
       long duration = 0;
        for (Integer i : list) {
            duration += subTaskMap.get(i).getDuration();
        }
        epicMap.get(id).setDuration(duration);
    }

    public void countEpicStartTime(int id) {
        List <LocalDateTime> startTime = new ArrayList<>();
        List<Integer> list = epicMap.get(id).getSubTaskIdList();
        if (list.size() == 1) {
            epicMap.get(id).setStartTime(subTaskMap.get(list.get(0)).getStartTime());
            return;
        }
            for (Integer i : list) {
                startTime.add(subTaskMap.get(i).getStartTime());
            }
            epicMap.get(id).setStartTime(startTime.stream()
                    .filter(Objects::nonNull)
                    .min(Comparator.naturalOrder()).get());

    }

    public void countEpicEndTime(int id) {
        if (epicMap.get(id).getSubTaskIdList().isEmpty()) {
            long duration = epicMap.get(id).getDuration();
            LocalDateTime endTime = epicMap.get(id).getStartTime().plusMinutes(duration);
            epicMap.get(id).setEndTimeForEpic(endTime);
            return;
        }
            List<SubTask> subTasks = new ArrayList<>();
            List<Integer> list = epicMap.get(id).getSubTaskIdList();
            if (list.size() == 1) {
                epicMap.get(id).setEndTimeForEpic(subTaskMap.get(list.get(0)).getEndTime());
                return;
            }
            for (Integer i : list) {
                subTasks.add(subTaskMap.get(i));
            }
            epicMap.get(id).setEndTimeForEpic(subTasks.stream()
                    .filter(Objects::nonNull).map(Task::getEndTime)
                    .max(Comparator.naturalOrder()).get());

        /*List <LocalDateTime> endTime = new ArrayList<>();
        List<Integer> list = epicMap.get(id).getSubTaskIdList();
        for (Integer i : list) {
            endTime.add(subTaskMap.get(i).getEndTime());
        }
        epicMap.get(id).setEndTimeForEpic(endTime.stream()
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder()).get());*/
    }

    @Override
    public void deleteAllTasks() {
        taskMap.clear();
    }

    @Override
    public void deleteAllEpics() {
        epicMap.clear();
        subTaskMap.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subTaskMap.clear();
    }

    @Override
    public void deleteTaskById(int id) {
        taskMap.remove(id);
        getHistory().remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        for (SubTask subtask : subTaskMap.values()) {
            if (subtask.getEpicId() == id) {
                getHistory().remove(subtask.getTaskId());
            }
        }
        epicMap.remove(id);
        subTaskMap.values().removeIf(value -> value.getEpicId() == id);
        getHistory().remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        subTaskMap.remove(id);
        getHistory().remove(id);
    }

    @Override
    public String getAllTasks() {
        return taskMap.values().toString();
    }

    @Override
    public String getAllEpics() {
        return epicMap.values().toString();
    }

    @Override
    public String getAllSubtasks() {
        return subTaskMap.values().toString();
    }

    @Override
    public String displaySubtaskByEpicId(int epicId) {
        ArrayList <SubTask> subtasksList = new ArrayList<>();
        for (SubTask subtask : subTaskMap.values()) {
            if (subtask.getEpicId() == epicId) {
                subtasksList.add(subtask);
            }
        }
        if (subtasksList.isEmpty()) {
            System.out.println("Epic does not exist or contain any subtasks...");
        }
        return subtasksList.toString();
    }

    @Override
    public Epic getEpicById(int epicId) {
        historyManager.add(epicMap.get(epicId));
        return epicMap.get(epicId);
    }

    @Override
    public Task getTaskById(int taskId) {
        historyManager.add(taskMap.get(taskId));
        return taskMap.get(taskId);
    }

    @Override
    public SubTask getSubtaskById(int subtaskId) {
        historyManager.add(subTaskMap.get(subtaskId));
        return subTaskMap.get(subtaskId);
    }

    @Override
    public Task updateTask(int taskId, Task task) {
        taskSet.remove(taskMap.get(taskId));
        taskMap.put(taskId, task);
        taskSet.add(task);
        return task;
    }

    @Override
    public Epic updateEpic(int epicId, Epic epic) {
        taskSet.remove(epicMap.get(epicId));
        epicMap.put(epicId, epic);
        countEpicStatus(epicId);
        taskSet.add(epicMap.get(epicId));
        return epic;
    }

    @Override
    public SubTask updateSubtask(int subtaskId, SubTask subtask) {
        taskSet.remove(subTaskMap.get(subtaskId));
        subTaskMap.put(subtaskId, subtask);
        countEpicStatus(subtask.getEpicId());
        taskSet.add(subtask);
        return subtask;
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(taskSet);
    }
    public boolean timeValidator(Task task) {
        boolean isValid = true;
        LocalDateTime start = task.getStartTime();
        for (Task taskToCompareWith  : taskMap.values()) {
            if (start.isAfter(taskToCompareWith.getStartTime())
                    && start.isBefore(taskToCompareWith.getEndTime())) {
                        isValid = false;
            }
            if (start.equals(taskToCompareWith.getStartTime())
                    || start.equals(taskToCompareWith.getEndTime())) {
                isValid = false;
            }
        }
        for (Task taskToCompareWith  : subTaskMap.values()) {
            if (start.isAfter(taskToCompareWith.getStartTime())
                    && start.isBefore(taskToCompareWith.getEndTime())) {
                        isValid = false;
            }
            if (start.equals(taskToCompareWith.getStartTime())
                    || start.equals(taskToCompareWith.getEndTime())) {
                isValid = false;
            }
        }
        for (Epic taskToCompareWith  : epicMap.values()) {
            if (start.isAfter(taskToCompareWith.getStartTime())
                    && start.isBefore(taskToCompareWith.getEndTimeForEpic())) {
                        isValid = false;
            }
            if (start.equals(taskToCompareWith.getStartTime())
                    || start.equals(taskToCompareWith.getEndTime())) {
                isValid = false;
            }
        }   return isValid;
    }
    private int generateId () {
        return id++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
