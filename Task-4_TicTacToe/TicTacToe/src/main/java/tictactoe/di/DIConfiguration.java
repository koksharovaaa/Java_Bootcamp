package tictactoe.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tictactoe.datasource.repository.Repository;
import tictactoe.datasource.repository.RepositoryImp;
import tictactoe.datasource.repository.Storage;
import tictactoe.domain.service.Service;
import tictactoe.domain.service.ServiceImp;
import tictactoe.web.controller.Controller;

@Configuration
public class DIConfiguration {
  @Bean
  public Storage storage() {
    return new Storage();
  }

  @Bean
  public Repository repository(Storage storage) {
    return new RepositoryImp(storage);
  }

  @Bean
  public Service service(Repository repository) {
    return new ServiceImp(repository);
  }

  @Bean
  public Controller controller(Service service) {
    return new Controller(service);
  }
}
