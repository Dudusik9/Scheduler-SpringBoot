package scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import scheduler.model.SchedulerRunner;
import scheduler.model.Scripts;
import scheduler.repository.ScriptDAO;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping
public class MainController {
    @Autowired
    private ScriptDAO scriptDAO;

    // Мапа для хранения добавленных скриптов
    private static Set<Scripts> scripts = new TreeSet<>(new Comparator<Scripts>() {
        @Override
        public int compare(Scripts o1, Scripts o2) {
            if(o1.getId() > o2.getId())
                return 1;
            else if(o1.getId() < o2.getId())
                return -1;
            return 0;
        }
    });
    // Уникальный идентификатор для каждого скрипта
    private static Integer id = 1;

    @GetMapping
    public String mainPageDefault(Map<String, Set> model){
        model.put("scriptList", scripts);
        return "main";
    }

    @GetMapping("/main")
    public String mainPage(Map<String, Set> model){
        model.put("scriptList", scripts);
        return "redirect:/main";
    }

    @PostMapping("/add")
    public String add(@RequestParam String pathToFile, @RequestParam String launchTime, @RequestParam String launchDay,
                      Map<String, Set> model) throws IOException {
//        // Создание объект скрипт с текущими параметрами
//        Script currentScript = new Script(id++, pathToFile, launchTime, launchDay);
//        // Добавление созданного скрипта в стат. мапу (аналог репозитория)
//        if(!scripts.contains(currentScript))
//            scripts.add(currentScript);
//        // Добавление мапы в модель для отображения
//        model.put("scriptList", scripts);
//        return "main";
        // Создание объект скрипт с текущими параметрами
        Scripts currentScripts = new Scripts(id++, pathToFile, launchTime, launchDay);
        // Добавление созданного скрипта в стат. мапу (аналог репозитория)
        scriptDAO.save(currentScripts);
        // Добавление мапы в модель для отображения
//        model.put("scriptList", scripts);
        return "main";
    }

    @GetMapping("/delete")
    public String handleDelete(@RequestParam(name="id") int id, Map<String, Set> model){
//        for(Script currEl : scripts){
//            if(id == currEl.getId()){
//                scripts.remove(currEl);
//                break;
//            }
//        }
//        model.put("scriptList", scripts);
//        return "main";
        for(Scripts currEl : scripts){
            if(id == currEl.getId()){
                scripts.remove(currEl);
                break;
            }
        }
        model.put("scriptList", scripts);
        return "main";
    }

    @PostMapping("/create")
    public String startCreateService(Map<String, Set> model){
        model.put("scriptList", scripts);
        // Конфигурация планировщика с текущим Set
        SchedulerRunner schedulerRunner = new SchedulerRunner(scripts);
        schedulerRunner.run();
        return "main";
    }


}
