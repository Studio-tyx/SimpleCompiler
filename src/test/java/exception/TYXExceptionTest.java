package exception;

import org.junit.jupiter.api.Test;

class TYXExceptionTest {
    @Test
    void testException() {
        TYXException TYXException =new TYXException("error");
        TYXException.show();
        
    }
}