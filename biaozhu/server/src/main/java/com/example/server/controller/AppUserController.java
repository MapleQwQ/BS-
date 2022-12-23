package com.example.server.controller;

import com.example.server.entity.AppUser;
import com.example.server.service.AppUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
//@RequestMapping("/users")
public class AppUserController {

  private AppUserService service;
  private BCryptPasswordEncoder bCryptPasswordEncoder;


  public AppUserController(AppUserService service, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.service = service;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @PostMapping("/users/register")
  int register(@Valid @RequestBody AppUser appUser) {
    System.out.println("register");
    appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
    System.out.println(appUser.getPassword());
    AppUser newAppUser = new AppUser(appUser.getUserName(), appUser.getEmail(), appUser.getIsAdmin(),
        appUser.getPassword());
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/register").toUriString());
    int result=service.create(newAppUser);
    return result;
  }

  @GetMapping("/users/getUser")
  AppUser GetOneByName(String userName) {
    return service.getByAppUserName(userName);
  }

  @PostMapping("/users/{id}/forget/")
  AppUser FindPassword(@PathVariable Long id, @Valid @RequestBody AppUser newAppUser) {
    return service.updateById(id, newAppUser);
  }

  @GetMapping("/users")
  List<AppUser> all() {
    return service.getAll();
  }
//    public ResponseEntity<List<AppUser>>getAppUsers(){
//        return ResponseEntity.ok().body(service.getAll());
//    }

  @GetMapping("/users/{id}")
  AppUser getOne(@PathVariable Long id) {
    return service.getById(id);
  }

  @PutMapping("/users/{id}")
  AppUser updateOne(@PathVariable Long id, @Valid @RequestBody AppUser newAppUser) {
    return service.updateById(id, newAppUser);
  }

  @DeleteMapping("/users/{id}")
  void delete(@PathVariable Long id) {
    service.deleteById(id);
  }
}
