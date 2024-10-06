package duerko;

import java.util.List;

class DefaultTransactionalTry implements TransactionalTry {

    List<Runnable> actions;
    List<Runnable> rollbackActions;
    List<Runnable> finallyActions;
    int retryCount;
    private DefaultTransactionalTry(List<Runnable> actions, List<Runnable> rollbackActions, List<Runnable> finallyActions, int retryCount) {
        this.actions = actions;
        this.rollbackActions = rollbackActions;
        this.finallyActions = finallyActions;
        this.retryCount = retryCount;
    }
    @Override
    public void execute() {
        try {
            actions.forEach(Runnable::run);
        } catch (Exception e) {
            rollbackActions.forEach(Runnable::run);
            if (retryCount > 0) {
                retryCount--;
                execute();
            }
        } finally {
            finallyActions.forEach(Runnable::run);
        }
    }
    static TransactionalTry create(List<Runnable> actions, List<Runnable> rollbackActions, List<Runnable> finallyActions, int retryCount) {
        return new DefaultTransactionalTry(actions, rollbackActions, finallyActions, retryCount);
    }
}
