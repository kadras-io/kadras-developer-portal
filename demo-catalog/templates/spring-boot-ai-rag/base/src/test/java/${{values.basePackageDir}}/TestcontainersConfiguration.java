package ${{ values.basePackage }};

import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
{%- if values.vectorStore == "postgresql" %}
import org.testcontainers.containers.PostgreSQLContainer;
{%- endif %}
import org.testcontainers.grafana.LgtmStackContainer;

import java.time.Duration;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	{% if values.vectorStore == "postgresql" %}
	@Bean
	@RestartScope
	@ServiceConnection
	PostgreSQLContainer<?> postgresContainer() {
		return new PostgreSQLContainer<>("pgvector/pgvector:pg17");
	}
	{%- endif %}

	@Bean
	@RestartScope
	@ServiceConnection
	LgtmStackContainer lgtmContainer() {
		return new LgtmStackContainer("grafana/otel-lgtm:0.7.6")
				.withStartupTimeout(Duration.ofMinutes(2))
				.withReuse(true);
	}

}
