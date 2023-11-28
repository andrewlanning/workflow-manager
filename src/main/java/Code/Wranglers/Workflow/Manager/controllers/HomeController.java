package Code.Wranglers.Workflow.Manager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/admin")
    public String admin(){
        return "This is the Admin Portal";
    }

    @GetMapping("/manager")
    public String manager(){
        return "This is the manager Portal";
    }

    @GetMapping("/member")
    public String member(){
        return "This is the Quality/Assembly Portal";
    }
}

