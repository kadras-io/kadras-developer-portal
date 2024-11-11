package ${{ values.basePackage }};

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.source.DocumentRetriever;
import org.springframework.ai.rag.retrieval.source.VectorStoreDocumentRetriever;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	ApplicationListener<ApplicationReadyEvent> logbackOtelAppenderInitializer(OpenTelemetry openTelemetry) {
		return _ -> OpenTelemetryAppender.install(openTelemetry);
	}

}

@RestController
class ChatController {

	private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

	private final ChatClient chatClient;
	private final DocumentRetriever documentRetriever;

	ChatController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
		this.chatClient = chatClientBuilder.build();
		this.documentRetriever = VectorStoreDocumentRetriever.builder()
			.vectorStore(vectorStore)	
			.build();
	}

	@PostMapping("/chat")
	String chatWithDocument(@RequestBody String message) {
		logger.info("Received user message: {}", message);
		return chatClient.prompt()
				.advisors(RetrievalAugmentationAdvisor.builder()
					.documentRetriever(documentRetriever)
					.build())
				.user(message)
				.call()
				.content();
	}

}

@Component
class IngestionPipeline {

	private static final Logger logger = LoggerFactory.getLogger(IngestionPipeline.class);
	private final JdbcClient jdbcClient;
	private final VectorStore vectorStore;

	@Value("classpath:documents/story.md")
	Resource textFile;

	public IngestionPipeline(JdbcClient jdbcClient, VectorStore vectorStore) {
		this.jdbcClient = jdbcClient;
		this.vectorStore = vectorStore;
	}

	@PostConstruct
	public void run() {
		if ((long) jdbcClient.sql("select count(*) from vector_store").query().singleValue() > 0) {
			return;
		}

		logger.info("Loading text files as Documents");
		var documents = new TextReader(textFile).get();

		logger.info("Creating and storing Embeddings from Documents");
		vectorStore.add(new TokenTextSplitter().split(documents));
	}

}
