package manager;

import task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.*;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static final String path = "C:\\Users\\Пользователь\\dev\\java-kanban\\src\\data.csv" ;
    private File file;
    public FileBackedTasksManager(File file) {
        this.file = file;
    }
    private void save() {
        try (FileWriter fileWriter = new FileWriter(
                path, false)) {

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



     public void fromString(String value)  {
        try {
            if (value.contains("TASK") || value.contains("EPIC")) {
                String[] dividedByComma = value.split(",");
                int id = Integer.parseInt(dividedByComma[0]);
                Task.Type type = Task.Type.valueOf(dividedByComma[1]);
                String name = dividedByComma[2];
                String description = dividedByComma[3];
                Task.Status status = Task.Status.valueOf(dividedByComma[4]);
                int epicId = 0;
                if (dividedByComma.length > 5) {
                    epicId = Integer.parseInt(dividedByComma[5]);
                }
                setId(id);
                switch (type) {
                    case TASK:
                        Task task = new Task(type, name, description, status);
                            task.setTaskId(id);
                                taskMap.put(id, task);
                                    break;
                    case EPIC:
                        Epic epic = new Epic(type, name, description, status, new ArrayList<>());
                            epic.setTaskId(id);
                                epicMap.put(id, epic);
                                    break;
                    case SUBTASK:
                        SubTask subTask = new SubTask(type, name, description, status, epicId);
                            subTask.setTaskId(id);
                                subTaskMap.put(id, subTask);
                                    break;
                }
            }
        } catch (NumberFormatException e) {
            e.getStackTrace();
        }
    }

    public void  loadFromFile(File file) throws ManagerSaveException {
        try (FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);
             BufferedReader bf = new BufferedReader(fileReader)) {
        while (bf.ready()) {
                String line = bf.readLine();
                if (line.contains("TASK") || line.contains("EPIC") || line.contains("SUBTASK")) {
                    fromString(line);
                }
                else {
                    List<Integer> list = new ArrayList<>(historyFromString(line));
                    for (Integer id : list) {
                        if (taskMap.containsKey(id)) {
                            historyManager.add(taskMap.get(id));
                        }
                        if (epicMap.containsKey(id)) {
                            historyManager.add(epicMap.get(id));
                        }
                        if (subTaskMap.containsKey(id)) {
                            historyManager.add(subTaskMap.get(id));
                        }
                    }
                }
            }
        }
            catch (IOException e) {
                throw new ManagerSaveException();
            }

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
         /*String[] values = value.split(",");
         List<Integer> list = new ArrayList<>();
         for (String str : values) {
             if (!str.isEmpty()) {
                 list.add(Integer.parseInt(str));
             }
         }*/
         String[] values = value.split(","); //решил проблему с пропуском
         return Arrays.stream(values)             //и переписал с учетом новых знаний ))
                 .filter(v -> !v.isEmpty())
                 .map(Integer::parseInt)
                 .collect(Collectors.toList());

    }


    @Override
    public Epic createEpic(Epic epic)  {
        super.createEpic(epic);
        save();
        return epic;
        }


    @Override
    public SubTask createSubTask(SubTask subtask) {
        super.createSubTask(subtask);
        save();
        return subtask;
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
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
    public void displaySubtaskByEpicId(int epicId) {
        super.displaySubtaskByEpicId(epicId);
            save();
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



}
