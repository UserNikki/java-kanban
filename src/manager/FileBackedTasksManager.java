package manager;

import task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;
    public FileBackedTasksManager(File file) {
        this.file = file;
    }
    public void save() throws IOException {
        try (FileWriter fileWriter = new FileWriter(
                "C:\\Users\\Пользователь\\dev\\java-kanban\\alltasks.csv", false)) {

           for (Task task : taskMap.values()) {
               fileWriter.write(task.toString() + "\n");
           }
            for (Epic task : epicMap.values()) {
                fileWriter.write(task.toString() + "\n");
            }
            for (SubTask subTask : subTaskMap.values()) {
                fileWriter.write(subTask.toString() + "\n");
            }
                fileWriter.write(historyToString(historyManager));
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
            System.out.println("-_-");
        }
    }

    public  void  loadFromFile(File file) throws IOException{
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
                throw new IOException();
            }

    }

    static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        List<String> id = new ArrayList<>();
            for (Task task : history) {
                String[] idFromString = task.toString().split(",");
                    id.add(idFromString[0]);
        }
        return id.toString().replace("[", "").replace("]", "").replace(" ", "");
    }

     static List<Integer> historyFromString(String value) {
        //возможно создать счетчик кол-ва линий(строк) в файле
        //и при загрузке/считывании истории использовать счетчик для пропуска записей
        //напр. до тех пор пока файл.райт то читать, как только равно -1 то прибавить/пропустить две строки
         String[] values = value.split(",");
         List<Integer> list = new ArrayList<>();
         for (String str : values) {
             list.add(Integer.parseInt(str));
         }

        return list;
    }

    @Override
    public Epic createEpic(Epic epic)  {

            super.createEpic(epic);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return epic;
        }


    @Override
    public SubTask createSubTask(SubTask subtask) {
        super.createSubTask(subtask);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return subtask;
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void displaySubtaskByEpicId(int epicId) {
        super.displaySubtaskByEpicId(epicId);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Epic getEpicById(int epicId) {
        super.getEpicById(epicId);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return epicMap.get(epicId);
    }

    @Override
    public Task getTaskById(int taskId) {
        super.getTaskById(taskId);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return taskMap.get(taskId);
    }

    @Override
    public SubTask getSubtaskById(int subtaskId) {
        super.getSubtaskById(subtaskId);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return subTaskMap.get(subtaskId);
    }

    @Override
    public Task updateTask(int taskId, Task task) {
        super.updateTask(taskId,task);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    @Override
    public Epic updateEpic(int epicId, Epic epic) {
        super.updateEpic(epicId,epic);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return epic;
    }

    @Override
    public SubTask updateSubtask(int subtaskId, SubTask subtask) {
        super.updateSubtask(subtaskId,subtask);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return subtask;
    }



}
