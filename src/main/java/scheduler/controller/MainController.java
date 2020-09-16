package scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import scheduler.model.Role;
import scheduler.model.SchedulerRunner;
import scheduler.model.Scripts;
import scheduler.model.User;
import scheduler.repository.ScriptDAO;
import scheduler.repository.UserDao;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping
public class MainController {
    @Autowired
    private ScriptDAO scriptDAO;

    @Autowired
    private UserDao userDao;

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

    @GetMapping
    public String mainPageDefault(Model model){
        model.addAttribute("scriptList", scriptDAO.findAll());
        return "main";
    }

    @GetMapping("/main")
    public String mainPage(Model model){
        model.addAttribute("scriptList", scriptDAO.findAll());
        return "main";
    }

    @PostMapping("/add")
    public String add(@RequestParam String pathToFile, @RequestParam String launchTime, @RequestParam String launchDay,
                      Model model) throws IOException {
        Scripts currentScripts = new Scripts( pathToFile, launchTime, launchDay);
        scriptDAO.save(currentScripts);
        model.addAttribute("scriptList", scriptDAO.findAll());
        return "main";
    }

    @GetMapping("/delete")
    public String handleDelete(@RequestParam(name="id") Long id, Model model){
        scriptDAO.delete(scriptDAO.findById(id).get());
        model.addAttribute("scriptList", scriptDAO.findAll());
        return "main";
    }

    @PostMapping("/create")
    public String startCreateService(Model model){
        model.addAttribute("scriptList", scriptDAO.findAll());
        // Конфигурация планировщика с текущим Set
        scriptDAO.findAll().forEach(a -> scripts.add(a));
        SchedulerRunner schedulerRunner = new SchedulerRunner(scripts);
        schedulerRunner.run();
        return "main";
    }

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model){
        User userFromDb = userDao.findByUsername(user.getUsername());
        if(userFromDb != null){
            model.addAttribute("message", "User exists!");
            return "registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userDao.save(user);
        return "redirect:/login";
    }
}


