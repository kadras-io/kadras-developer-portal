package ${{ values.basePackage }};

import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
{%- if values.vectorStore == "postgresql" %}
import org.testcontainers.containers.PostgreSQLContainer;
{%- endif %}
import org.testcontainers.grafana.LgtmStackContainer;
{% if values.llmProvider == "ollama" %}
import org.testcontainers.ollama.OllamaContainer;
{%- endif %}
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	{% if values.llmProvider == "ollama" %}
	@Bean
	@RestartScope
	@ServiceConnection
	@Profile("ollama-image")
	OllamaContainer ollama() {
		return new OllamaContainer(DockerImageName.parse("ghcr.io/thomasvitale/ollama-mistral")
			.asCompatibleSubstituteFor("ollama/ollama"));
	}
	{%- endif %}

	{% if values.vectorStore == "postgresql" %}
	@Bean
	@RestartScope
	@ServiceConnection
	PostgreSQLContainer<?> postgresContainer() {
		return new PostgreSQLContainer<>(DockerImageName.parse("pgvector/pgvector:pg16"));
	}
	{%- endif %}

	@Bean
	@RestartScope
	@ServiceConnection
	LgtmStackContainer lgtmContainer() {
		return new LgtmStackContainer("grafana/otel-lgtm:0.7.1")
				.withStartupTimeout(Duration.ofMinutes(2))
				.withReuse(true);
	}

}
