package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
@RestController
public class Application {

  static class Self {
    public String href;
  }

  static class Links {
    public Self self;
  }

  static class PlayerState {
    public Integer x;
    public Integer y;
    public String direction;
    public Boolean wasHit;
    public Integer score;
  }

  static class Arena {
    public List<Integer> dims;
    public Map<String, PlayerState> state;
  }

  static class ArenaUpdate { 
    public Links _links;
    public Arena arena;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.initDirectFieldAccess();
  }

  @GetMapping("/")
  public String index() {
    return "Let the battle begin!";
  }

  public Boolean canShoot(PlayerState me, ArenaUpdate arenaUpdate) {
     if (me.direction == "N") {
      for (String key : arenaUpdate.arena.state.keySet()) {
        PlayerState enemy = arenaUpdate.arena.state.get(key);
        if (enemy.x == me.x && enemy.y == me.y)
          continue;
        if (me.x == enemy.x && me.y - enemy.y <= 3) 
          return true;
      }
     } else if (me.direction == "E") {
        for (String key : arenaUpdate.arena.state.keySet()) {
          PlayerState enemy = arenaUpdate.arena.state.get(key);
          if (enemy.x == me.x && enemy.y == me.y)
            continue;
          if (me.y == enemy.y && enemy.x - me.x <= 3) 
            return true;
        }
     } else if (me.direction == "S") {
        for (String key : arenaUpdate.arena.state.keySet()) {
          PlayerState enemy = arenaUpdate.arena.state.get(key);
          if (enemy.x == me.x && enemy.y == me.y)
            continue;
          if (me.x == enemy.x && enemy.y - me.y <= 3) 
            return true;
        }
     } else {
      for (String key : arenaUpdate.arena.state.keySet()) {
        PlayerState enemy = arenaUpdate.arena.state.get(key);
        if (enemy.x == me.x && enemy.y == me.y)
          continue;
        if (me.y == enemy.y && me.x - enemy.x <= 3) 
          return true;
      }
     }
  }


  public PlayerState getMyLocation (ArenaUpdate arenaUpdate) {
    PlayerState me = arenaUpdate.arena.state.get(arenaUpdate._links.self.href);
    return me;
  }

  @PostMapping("/**")
  public String index(@RequestBody ArenaUpdate arenaUpdate) {
    System.out.println(arenaUpdate);
    
    String[] commands = new String[]{"F", "R", "L", "T"};

    PlayerState me = getMyLocation (arenaUpdate);

    if (canShoot(me, arenaUpdate)) {
      return commands[3];
    }

    if (me.wasHist) {
      return commands[0];
    }

    int i = new Random().nextInt(4);
    return commands[0];


  }

}

