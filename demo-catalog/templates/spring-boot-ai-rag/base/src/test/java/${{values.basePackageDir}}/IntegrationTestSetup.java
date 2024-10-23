package ${{ values.basePackage }};

import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
{%- if values.llmProvider == "ollama" %}
import org.testcontainers.ollama.OllamaContainer;
{%- endif %}

@TestConfiguration(proxyBeanMethods = false)
public class IntegrationTestSetup {

  {% if values.llmProvider == "ollama" %}
  @Bean
  @RestartScope
  @ServiceConnection
  OllamaContainer ollama() {
    return new OllamaContainer("ollama/ollama").withReuse(true);
  }
  {%- endif %}

}

