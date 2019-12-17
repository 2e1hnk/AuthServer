package cafe.deadbeef.auth_server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
	
	@GetMapping("")
	public String root() {
		return "index";
	}
	
	@GetMapping("/admin")
	public String adminRoot() {
		return "admin";
	}
	
}
