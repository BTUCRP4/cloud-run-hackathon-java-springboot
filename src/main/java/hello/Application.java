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
    System.out.println("Me:"+me.x+"|"+me.y+"|"+me.direction);
    for (String key : arenaUpdate.arena.state.keySet()) {
      PlayerState enemy = arenaUpdate.arena.state.get(key);
      
      System.out.println("Enemry:"+enemy.x+"|"+enemy.y);
      if (enemy.x == me.x && enemy.y == me.y)
          continue;
     if (me.direction == "N") {
        if (me.x == enemy.x && me.y - enemy.y <= 3) 
          return true;
     } else if (me.direction == "E") {
          if (me.y == enemy.y && enemy.x - me.x <= 3) 
            return true;
     } else if (me.direction == "S") {
       
          if (me.x == enemy.x && enemy.y - me.y <= 3) 
          return true;
     } else {
        if (me.y == enemy.y && me.x - enemy.x <= 3) 
        return true;
      }
     }
     return false;
  }


  public PlayerState getMyLocation (ArenaUpdate arenaUpdate) {
    PlayerState me = arenaUpdate.arena.state.get(arenaUpdate._links.self.href);
    return me;
  }

  @PostMapping("/**")
  public String index(@RequestBody ArenaUpdate arenaUpdate) {
    String result = "T";
    try {
        System.out.println(arenaUpdate);
        String[] commands = new String[]{"F", "R", "L", "T"};
        int i = new Random().nextInt(4);
        //if (true)
        //  return commands[i];
        

        PlayerState me = getMyLocation (arenaUpdate);

        if (canShoot(me, arenaUpdate)) {
          result =  "T";
        } else {

          if (me.wasHit) {
            if (me.direction == "N") {
              if (me.y>1)
                result = "F";
            } else if (me.direction == "W") {
              if (me.x>1)
                result = "F";
            }
            result = "R";
          } else {
             result = commands[new Random().nextInt(4)];
          }
        }
      } catch (Exception e) {
        result =  "T";
      }

      System.out.println("Action:"+ result);
      return result;

  }

}

