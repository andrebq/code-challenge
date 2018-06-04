package info.amoraes.n26.challenge.api.impl;

import info.amoraes.n26.challenge.api.ResourceURIBuilder;
import java.net.URI;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class ResourceURIBuilderImpl implements ResourceURIBuilder {
  @Override
  public URI statistics() {
    return ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/../statistics")
        .buildAndExpand()
        .toUri();
  }
}
