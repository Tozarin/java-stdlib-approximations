package generated.org.springframework.boot;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
public class TestClass {

    @Autowired
    private MockMvc mockMvc;

    public static void ignoreResult(Object result) {
    }
}
