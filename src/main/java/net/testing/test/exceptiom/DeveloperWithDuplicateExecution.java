package net.testing.test.exceptiom;

public class DeveloperWithDuplicateExecution extends RuntimeException {
    public DeveloperWithDuplicateExecution(String message) {
        super(message);
    }
}
