package manager;

import task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static final String PATH = "src/data.csv" ;
    private File file;
    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
       FileBackedTasksManager fileman = new FileBackedTasksManager(new File(PATH));
        Task task = new Task
                (Task.Type.TASK,"name","description", Task.Status.NEW,100,"20/03/2023/18:59");
        Epic epic = new Epic
                (Task.Type.EPIC,"name Epic", "description Epic", Task.Status.NEW,90,"20/03/2023/07:00");
        SubTask subTask = new SubTask
                (Task.Type.SUBTASK,"Subtask","description", Task.Status.NEW,10,"20/03/2023/11:00",2);
        SubTask subTask1 = new SubTask
                (Task.Type.SUBTASK,"Subtask1","description1", Task.Status.NEW,20,"20/03/2023/12:00",2);
        SubTask subTask2 = new SubTask
                (Task.Type.SUBTASK,"Subtask2","description2", Task.Status.NEW,30,"20/03/2023/19:00",2);
        fileman.createTask(task);
        fileman.createEpic(epic);
        fileman.createSubTask(subTask);
        fileman.createSubTask(subTask1);
        fileman.createSubTask(subTask2);
        System.out.println(fileman.getPrioritizedTasks());
        /*fileman.getTaskById(1);
        fileman.getEpicById(2);
        fileman.getSubtaskById(3);
        fileman.getSubtaskById(4);
        fileman.getSubtaskById(5);
        System.out.println(fileman.getHistory());
        System.out.println(fileman.getAllTasks());
        System.out.println(fileman.getAllEpics());
        System.out.println(fileman.getAllSubtasks());*/

        /*FileBackedTasksManager fileman1 = loadFromFile(new File(PATH));
        System.out.println(fileman1.getHistory());
        System.out.println(fileman1.getAllTasks());
        System.out.println(fileman1.getAllEpics());
        System.out.println(fileman1.getAllSubtasks());*/

    }
    private void save() {
        try (FileWriter fileWriter = new FileWriter(
                PATH, false)) {

           for (Task task : taskMap.values()) {
               fileWriter.write(task.toString() + "\n");
           }
            for (Epic task : epicMap.values()) {
                fileWriter.write(task.toString() + "\n");
            }
            for (SubTask subTask : subTaskMap.values()) {
                fileWriter.write(subTask.toString() + "\n");
            }
                fileWriter.write("\n" + "\n" + historyToString(historyManager));
            }
         catch (IOException exp) {
            throw new ManagerSaveException();
        }
    }

     public Task fromString(String value)  {
        try {
            if (value.contains("TASK") || value.contains("EPIC")) {
                String[] dividedByComma = value.split(",");
                int id = Integer.parseInt(dividedByComma[0]);
                Task.Type type = Task.Type.valueOf(dividedByComma[1]);
                String name = dividedByComma[2];
                String description = dividedByComma[3];
                Task.Status status = Task.Status.valueOf(dividedByComma[4]);
                long duration = Long.parseLong(dividedByComma[5]);
                String startTime = dividedByComma[6];
                int epicId = 0;
                LocalDateTime endTime = null;
                if (dividedByComma.length > 7) {
                    if (type.equals(Task.Type.SUBTASK)) {
                        epicId = Integer.parseInt(dividedByComma[7]);
                    }
                    //epicId = Integer.parseInt(dividedByComma[7]);
                    if (type.equals(Task.Type.EPIC)) {
                        endTime = LocalDateTime.parse(dividedByComma[7], Task.DATE_TIME_FORMATTER);
                    }
                    //endTime = LocalDateTime.parse(dividedByComma[7], Task.DATE_TIME_FORMATTER);

                }
                 setId(id);
                 switch (type) {
                    case TASK:
                        Task task = new Task(type, name, description, status,duration,startTime);
                            task.setTaskId(id);
                                return task;

                    case EPIC:
                        Epic epic = new Epic(type, name, description, status, duration, startTime);
                            epic.setTaskId(id);
                                epic.setEndTimeForEpic(endTime);
                                return epic;

                    case SUBTASK:
                        SubTask subTask = new SubTask(type, name, description, status, duration,startTime, epicId);
                            subTask.setTaskId(id);
                                return subTask;

                }
            }
        } catch (NumberFormatException e) {
            e.getStackTrace();
        }
        return null;
    }

    public static FileBackedTasksManager  loadFromFile(File file) throws ManagerSaveException {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        List<Task> tasks = new ArrayList<>();
        try (FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);
             BufferedReader bf = new BufferedReader(fileReader)) {
        while (bf.ready()) {
            String line = bf.readLine();
            if (line.contains("TASK") || line.contains("EPIC") || line.contains("SUBTASK")) {
                tasks.add(manager.fromString(line));
            }
            else {
                List<Integer> list = new ArrayList<>(historyFromString(line));
                for (Integer id : list) {
                    if (manager.taskMap.containsKey(id)) {
                        manager.historyManager.add(manager.taskMap.get(id));
                    }
                    if (manager.epicMap.containsKey(id)) {
                        manager.historyManager.add(manager.epicMap.get(id));
                    }
                    if (manager.subTaskMap.containsKey(id)) {
                        manager.historyManager.add(manager.subTaskMap.get(id));
                    }
                }
            }
            }
            manager.taskMap = tasks.stream()
                    .filter(v -> v.getType().equals(Task.Type.TASK))
                    .collect(Collectors.toMap(Task::getTaskId, task -> task));
            manager.epicMap = tasks.stream()
                    .filter(v -> v.getType().equals(Task.Type.EPIC)).map(Epic.class::cast)
                    .collect(Collectors.toMap(Task::getTaskId, epic -> epic));
            manager.subTaskMap = tasks.stream()
                    .filter(v -> v.getType().equals(Task.Type.SUBTASK)).map(SubTask.class::cast)
                    .collect(Collectors.toMap(Task::getTaskId, subtask -> subtask));
        }
            catch (IOException e) {
                throw new ManagerSaveException();
            }
        return manager;
        }

    static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        List<String> id = new ArrayList<>();
            for (Task task : history) {
                String[] idFromString = task.toString().split(",");
                    id.add(idFromString[0]);
        }
        return id.toString()
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "");
    }

     static List<Integer> historyFromString(String value) {
         String[] values = value.split(",");
         return Arrays.stream(values)
                 .filter(v -> !v.isEmpty())
                 .map(Integer::parseInt)
                 .collect(Collectors.toList());
    }
    @Override
    public Epic createEpic(Epic epic)  {
        if (timeValidator(epic)) {
            super.createEpic(epic);
            save();
        }
        return epic;
        }


    @Override
    public SubTask createSubTask(SubTask subtask) {
        if (timeValidator(subtask)) {
            super.createSubTask(subtask);
            save();
        }
        return subtask;
    }

    @Override
    public Task createTask(Task task) {
        if (timeValidator(task)) {
            super.createTask(task);
            save();
        }
        return task;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
            save();

    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
            save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
            save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
            save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
            save();
    }

    @Override
    public String displaySubtaskByEpicId(int epicId) {
            save();
            return super.displaySubtaskByEpicId(epicId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        super.getEpicById(epicId);
            save();
                return epicMap.get(epicId);
    }

    @Override
    public Task getTaskById(int taskId) {
        super.getTaskById(taskId);
        save();
        return taskMap.get(taskId);
    }

    @Override
    public SubTask getSubtaskById(int subtaskId) {
        super.getSubtaskById(subtaskId);
        save();
        return subTaskMap.get(subtaskId);
    }

    @Override
    public Task updateTask(int taskId, Task task) {
        super.updateTask(taskId,task);
        save();
        return task;
    }

    @Override
    public Epic updateEpic(int epicId, Epic epic) {
        super.updateEpic(epicId,epic);
        save();
        return epic;
    }

    @Override
    public SubTask updateSubtask(int subtaskId, SubTask subtask) {
        super.updateSubtask(subtaskId,subtask);
        save();
        return subtask;
    }
    @Override
    public boolean timeValidator(Task task) {
        return super.timeValidator(task);
    }



}
