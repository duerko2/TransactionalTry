import duerko.TransactionalTryBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleTestOfSuperiorTry {
    int localVariableA = 5;
    String localVariableB = "Hello";

    @Test
    public void testAddActionPrimitive(){


        TransactionalTryBuilder builder = new TransactionalTryBuilder();

        builder.addAction(()->localVariableA=10);

        builder.build().execute();

        assertEquals(10, localVariableA);
    }
    @Test
    public void testAddActionObject(){

            TransactionalTryBuilder builder = new TransactionalTryBuilder();

            builder.addAction(()->localVariableB="World");

            builder.build().execute();

            assertEquals("World", localVariableB);
    }
    @Test
    public void testExceptionRollback(){

        TransactionalTryBuilder builder = new TransactionalTryBuilder();

        builder.addAction(()->localVariableA=10)
                .addRollBackAction(()->localVariableA=5)
                .addAction(()->{throw new RuntimeException("Exception");});

        builder.build().execute();

        assertEquals(5, localVariableA);
    }

    @Test
    public void testRetry() {
        final Integer[] counter = {0};
        TransactionalTryBuilder builder = new TransactionalTryBuilder();

        builder.addAction(()->localVariableA=10)
                .addRollBackAction(()->localVariableA=5)
                .addAction(()->{
                    if(counter[0].equals(0)){
                        counter[0] = 1;
                        throw new RuntimeException("Exception");
                    }})
                .retry(1);

        builder.build().execute();

        assertEquals(localVariableA, 10);
    }
    @Test
    public void testFinally() {
        final Integer[] counter = {0};
        TransactionalTryBuilder builder = new TransactionalTryBuilder();

        builder.addAction(()->localVariableA=10)
                .finallyAction(()->localVariableA=5);

        builder.build().execute();

        assertEquals(localVariableA, 5);
    }
}
