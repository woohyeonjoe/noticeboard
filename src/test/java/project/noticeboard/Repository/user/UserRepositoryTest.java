package project.noticeboard.Repository.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.noticeboard.domain.user.Role;
import project.noticeboard.domain.user.User;
import project.noticeboard.domain.user.UserRepository;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail() {
        //given
        User user = User.builder()
                .name("name")
                .role(Role.USER)
                .email("email@gmail.com")
                .picture("picture")
                .build();

        userRepository.save(user);

        //when
        User user1 = userRepository.findByEmail("email@gmail.com").orElse(null);

        //then
        Assertions.assertThat(user.getId()).isEqualTo(user1.getId());
    }
}
