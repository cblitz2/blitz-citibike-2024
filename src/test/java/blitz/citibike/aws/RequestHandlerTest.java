package blitz.citibike.aws;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.Context;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RequestHandlerTest {

    @Test
    void handleRequest() throws IOException {
        // given
        String position = Files.readString(Path.of("request.json"));
        Context context = mock(Context.class);
        APIGatewayProxyRequestEvent event = mock(APIGatewayProxyRequestEvent.class);
        when(event.getBody()).thenReturn(position);
        CitiBikeRequestHandler handler = new CitiBikeRequestHandler();

        // when
        CitiBikeResponse response = handler.handleRequest(event, context);

        // then
        assertEquals("Lenox Ave & W 146 St", response.start.name);
        assertEquals("Berry St & N 8 St", response.end.name);
    }
}
