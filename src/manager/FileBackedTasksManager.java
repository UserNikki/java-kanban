package manager;

import task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.*;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static final String PATH = "src/data.csv" ;
    private File file;
    //file не инициализируется. задумка ведь = new File(PATH)?чтоб потом делать file.getPath?
    //если не критично пропусти плиз, если я правильно понял)) 7 спринт дико тяжелый)
    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        FileBackedTasksManager fileman = new FileBackedTasksManager(new File(PATH));
        /*Task task = new Task(Task.Type.TASK,"Task name", "Task description", Task.Status.NEW);
        Epic epic = new Epic(Task.Type.EPIC,"name Epic", "description Epic", Task.Status.NEW, new ArrayList<>());
        SubTask subTask = new SubTask(Task.Type.SUBTASK,"Subtask","description", Task.Status.NEW,2);
        fileman.createTask(task);
        fileman.createEpic(epic);
        fileman.createSubTask(subTask);
        fileman.getTaskById(1);
        fileman.getEpicById(2);
        fileman.getSubtaskById(3);
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


    /*ПРИВЕТ. ТЫ ТАМ НЕМНОГО ОШИБСЯ, Я В ПАЧКЕ НА ВСЯКИЙ НАПИСАЛ СО СКРИНАМИ
    ФРОМСТРИНГ ВОЗВРАШЩАЕТ ТАСКУ ПО ЗАДАНИЮ, А НЕ ЛИСТ
    Напишите метод создания задачи из строки Task fromString(String value).(цитата).
    Я ЕЩЕ ПОДУМАЛ, ЧТО КАК ТО НЕЛОГИЧНО ПОЛУЧАЕТСЯ, ДУМАЮ ДАЙ ЗАГЛЯНУ В ЗАДАНИЕ))БЫВАЕТ...*/
     public Task fromString(String value)  {
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
                                return task;

                    case EPIC:
                        Epic epic = new Epic(type, name, description, status, new ArrayList<>());
                            epic.setTaskId(id);
                                return epic;

                    case SUBTASK:
                        SubTask subTask = new SubTask(type, name, description, status, epicId);
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
            }   //ЗАЦЕНИ НИЖЕ КАК КРУТО ПЕРЕДЕЛАЛ)) ВМЕСТО КИЛОМЕТРА КОДА...
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
