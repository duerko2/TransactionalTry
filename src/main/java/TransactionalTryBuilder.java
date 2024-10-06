import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionalTryBuilder {
    List<Runnable> actions = new ArrayList<>();
    List<Runnable> rollbackActions = new ArrayList<>();
    int retryCount = 0;
    List<Runnable> finallyActions = new ArrayList<>();

    public TransactionalTryBuilder() {
    }
    public TransactionalTryBuilder addAction(Runnable o) {
        actions.add(o);
        return this;
    }
    public TransactionalTryBuilder addRollBackAction(Runnable o) {
        rollbackActions.add(o);
        return this;
    }
    public TransactionalTry build() {
        return DefaultTransactionalTry.create(actions, rollbackActions, finallyActions, retryCount);
    }

    public TransactionalTryBuilder retry(int i) {
        retryCount = i;
        return this;
    }

    public TransactionalTryBuilder finallyAction(Runnable o) {
        finallyActions.add(o);
        return this;
    }
}
