package account.account.service.impl;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {

  @NonNull
  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of("admin"); // Or get from Spring Security
  }
}
