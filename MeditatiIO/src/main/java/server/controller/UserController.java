package server.controller;
import server.model.User;
import server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    // NOU: Endpoint pentru UPDATE
    // {id} va fi ID-ul utilizatorului pe care vrei să-l actualizezi
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails)
                .map(user -> ResponseEntity.ok("Utilizator actualizat cu succes!"))
                .orElse(ResponseEntity.notFound().build());
    }

    // NOU: Endpoint pentru DELETE
    // {id} va fi ID-ul utilizatorului pe care vrei să-l ștergi
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok("Cont șters cu succes!");
        }
        return ResponseEntity.notFound().build();
    }
}
