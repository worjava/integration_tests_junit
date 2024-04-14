package net.testing.test.exceptiom;

public class DeveloperNotFoundException extends RuntimeException {
    public DeveloperNotFoundException(String developerNotFound) {
        super(developerNotFound);
    }
}
