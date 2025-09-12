package com.kadioglumf.kafkaeventrouter;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "event-routing", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RouterController {
  private final RouterRules routerRules;

  @GetMapping(value = "/rules")
  public String rules() {
    Optional<String> rulesJson = routerRules.getRulesJson();
    if (rulesJson.isPresent()) {
      return rulesJson.get();
    }
    return null;
  }

  @GetMapping(value = "/reload")
  public String reload() {
    routerRules.loadRules();
    return rules();
  }
}
