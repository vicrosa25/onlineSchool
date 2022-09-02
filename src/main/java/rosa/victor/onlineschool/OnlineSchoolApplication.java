package rosa.victor.onlineschool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("rosa.victor.onlineschool.repository")
@EntityScan("rosa.victor.onlineschool.model")
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class OnlineSchoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineSchoolApplication.class, args);
	}

}
